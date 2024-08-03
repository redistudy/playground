package dev.jihogrammer.redistudy.localsearch;

import dev.jihogrammer.redistudy.poi.application.port.in.PointOfInterestQuery;
import dev.jihogrammer.redistudy.poi.application.port.out.PointOfInterestStatistics;
import dev.jihogrammer.redistudy.poi.application.port.out.PointOfInterests;
import dev.jihogrammer.redistudy.poi.application.service.PointOfInterestQueryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LocalsearchApplication {

    public static void main(final String[] args) {
        SpringApplication.run(LocalsearchApplication.class, args);
    }

    @Bean
    public String apiKey(@Value("${localsearch.routo.api-key}") final String apiKey) {
        return apiKey;
    }

    @Bean
    public PointOfInterestQuery poiQuery(
            final PointOfInterests pointOfInterests,
            final PointOfInterestStatistics poiStatistics
    ) {
        return new PointOfInterestQueryService(pointOfInterests, poiStatistics);
    }

}
