package dev.jihogrammer.redistudy.localsearch.adaptor.redis;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jihogrammer.redistudy.localsearch.LocalsearchBatchApplication;
import dev.jihogrammer.redistudy.poi.domain.vo.Location;
import dev.jihogrammer.redistudy.poi.domain.PointOfInterest;
import dev.jihogrammer.redistudy.poi.domain.vo.PointOfInterestId;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Point;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest(classes = {LocalsearchBatchApplication.class, RedisConfig.class, RedisPointOfInterestTest.TestConfig.class})
class RedisPointOfInterestTest {

    @Autowired
    RedisPoiRepository repository;

    @Autowired
    API.Geocoding geocoding;

    @Autowired
    API.DaeguFood daeguFood;

    @Test
    void insertToRedis() {
        Stream.of("중구", "동구", "남구")
                .map(daeguFood::fetchData)
                .flatMap(Collection::stream)
                .map(restaurant -> {
                    try {
                        var addr = restaurant.getAddr();
                        var addressInfo = geocoding.fetchData(addr);
                        return this.toRestaurant(restaurant, addressInfo.value, addressInfo.point);
                    } catch (Exception e) {
                        log.warn("Failed converting address to coordinates.", e);
                        log.info("Retry converting...");

                        try {
                            var addr = restaurant.getAddr();
                            var addressInfo = geocoding.fetchData(addr);
                            return this.toRestaurant(restaurant, addressInfo.value, addressInfo.point);
                        } catch (Exception e2) {
                            log.warn("Failed converting address to coordinates.", e2);
                        }
                    }

                    return this.toRestaurant(restaurant, restaurant.addr, null);
                })
                .peek(restaurant -> log.info("restaurant: {}", restaurant))
                .map(Restaurant::toPoi)
                .forEach(repository::save);
    }

    record Restaurant(
            Integer id,
            String addr,
            Point point,
            String name,
            String category,
            String operatingInfo,
            String menuInfo,
            String description
    ) {

        PointOfInterest toPoi() {
            return new PointOfInterest(
                    new PointOfInterestId(this.id),
                    this.name,
                    this.addr,
                    this.point == null ? null : new Location(this.point.getY(), this.point().getX()),
                    Set.of(category));
        }

    }

    Restaurant toRestaurant(final API.DaeguFood.Restaurant restaurant, final String refinedAddress, final Point point) {
        return new Restaurant(
                restaurant.id,
                refinedAddress,
                point,
                restaurant.name,
                restaurant.category,
                restaurant.operatingInfo,
                restaurant.menuInfo,
                restaurant.description);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        API.Geocoding geocoding(
                @Value("${localsearch.public-api.geocoding.base-uri}") final String baseURI,
                @Value("${localsearch.public-api.geocoding.api-key}") final String apiKey
        ) {
            return new API.Geocoding(baseURI, apiKey);
        }

        @Bean
        API.DaeguFood daeguFood(
                @Value("${localsearch.public-api.daegu-food.base-uri}") final String baseURI
        ) {
            return new API.DaeguFood(baseURI);
        }

    }

    static class API {

        /**
         * <a href="https://www.vworld.kr/dev/v4dv_geocoderguide2_s001.do">README</a>
         */
        @Slf4j
        @RequiredArgsConstructor
        static class Geocoding {

            static final String SERVICE_TYPE = "address";

            static final String REQUEST_TYPE = "getCoord";

            static final String SERVICE_VERSION = "2.0";

            static final String ADDRESS_TYPE = "PARCEL";

            static final ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            final String baseURI;

            final String apiKey;

            AddressInfo fetchData(final String addr) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 주소 변환
                final String address;
                String[] tokens = addr.split(" ");
                if (tokens.length > 2) {
                    address = tokens[tokens.length - 2] + ' ' + tokens[tokens.length - 1];
                } else {
                    address = addr;
                }

                final URL url;
                try {
                    url = new URL(baseURI +
                            "?service=" + SERVICE_TYPE +
                            "&request=" + REQUEST_TYPE +
                            "&version=" + SERVICE_VERSION +
                            "&type=" + ADDRESS_TYPE +
                            "&key=" + apiKey +
                            "&address=" + URLEncoder.encode(address, StandardCharsets.UTF_8));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }

                HttpURLConnection conn = null;

                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    log.info("\nVVV REQUEST VVV\n주소={}\n{}", address, url);

                    log.info("\nVVV RESPONSE HEADERS VVV");
                    Map<String, List<String>> headers = conn.getHeaderFields();
                    for (String name : headers.keySet()) {
                        System.out.println(name + ": " + headers.get(name));
                    }

                    var responseBody = mapper.readValue(conn.getInputStream(), ResponseBody.class);
                    log.info("\nVVV RESOPONSE BODY VVV\n{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBody));

                    if ("NOT_FOUND".equals(responseBody.response.status)) {
                        var addressInfo = new AddressInfo();

                        addressInfo.value = addr;
                        addressInfo.point = null;

                        return addressInfo;
                    } else {
                        return AddressInfo.toAddressInfo(responseBody.response);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }

            @Data
            static class AddressInfo {

                private String value;

                private Point point;

                static AddressInfo toAddressInfo(final InnerResponse response) {
                    var result = new AddressInfo();

                    result.value = response.refined.text;
                    result.point = response.result.point.toDomainPoint();

                    return result;
                }

            }

            @Data
            static class ResponseBody {

                private InnerResponse response;

            }

            @Data
            static class InnerResponse {

                private String status;

                private RefinedAddress refined;

                private ConvertingResult result;

            }

            @Data
            static class RefinedAddress {

                private String text;

            }

            @Data
            @ToString
            static class ConvertingResult {

                @JsonAlias("crs")
                private String coordinatesInfo;

                private GeoPoint point;

            }

            @Data
            @ToString
            static class GeoPoint {

                private Double x;

                private Double y;

                Point toDomainPoint() {
                    return new Point(this.x, this.y);
                }

            }

        }

        @Slf4j
        @RequiredArgsConstructor
        static class DaeguFood {

            static final String MODE = "json";

            static final ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            final String baseURI;

            Collection<Restaurant> fetchData(final String region) {
                final URL url;
                try {
                    url = new URL(baseURI +
                            "?mode=" + MODE +
                            "&addr=" + URLEncoder.encode(region, StandardCharsets.UTF_8));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }

                HttpURLConnection conn = null;

                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    log.info("\nVVV REQUEST VVV\n지역={}\n{}", region, url);

                    log.info("\nVVV RESPONSE HEADERS VVV");
                    Map<String, List<String>> headers = conn.getHeaderFields();
                    for (String name : headers.keySet()) {
                        System.out.println(name + ": " + headers.get(name));
                    }

                    var responseBody = mapper.readValue(conn.getInputStream(), ResponseBody.class);
                    log.info("\nVVV RESOPONSE BODY VVV\n{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBody));

                    return responseBody.data;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }

            @Data
            static class ResponseBody {

                private String status;

                private Integer total;

                private Collection<Restaurant> data;

            }

            @Data
            @ToString
            static class Restaurant {

                @JsonAlias("OPENDATA_ID")
                private Integer id;

                @JsonAlias("GNG_CS")
                private String addr;

                @JsonAlias("BZ_NM")
                private String name;

                @JsonAlias("FD_CS")
                private String category;

                @JsonAlias("MBZ_HR")
                private String operatingInfo;

                @JsonAlias("MNU")
                private String menuInfo;

                @JsonAlias("SMPL_DESC")
                private String description;

            }

        }

    }

}
