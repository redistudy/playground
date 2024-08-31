package dev.jihogrammer.redistudy.localsearch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoLocation;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;

import static org.springframework.data.redis.connection.RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs;

@Slf4j
@Repository
public class RedisPoiRepository {

    private final String LOCATION_GEOSPATIAL_KEY = "location";

    private final HashOperations<String, String, String> poiMetas;

    private final GeoOperations<String, String> poiLocations;

    RedisPoiRepository(final RedisTemplate<String, String> redisTemplate) {
        this.poiMetas = redisTemplate.opsForHash();
        this.poiLocations = redisTemplate.opsForGeo();

        log.info(">>> poiMetas={}", this.poiMetas.getClass());
        log.info(">>> poiLocations={}", this.poiLocations.getClass());
    }

    public void save(final PointOfInterest poi) {
        var metaKey = "poi:" + poi.id();

        String ID_HASH_FIELD = "id";
        this.poiMetas.put(metaKey, ID_HASH_FIELD, poi.id());
        String NAME_HASH_FIELD = "name";
        this.poiMetas.put(metaKey, NAME_HASH_FIELD, poi.name());
        String ADDR_HASH_FIELD = "address";
        this.poiMetas.put(metaKey, ADDR_HASH_FIELD, poi.address());

        if (poi.location() != null) {
            this.poiLocations.add(LOCATION_GEOSPATIAL_KEY, poi.location(), poi.id());
        }
    }

    PointOfInterest findByPoiId(final String poiId) {
        var entries = this.poiMetas.entries("poi:" + poiId);
        var locations = this.poiLocations.position(LOCATION_GEOSPATIAL_KEY, poiId);
        log.info("meta={}, locations={}", entries, locations);

        return new PointOfInterest(
                entries.get("id"),
                entries.get("name"),
                entries.get("address"),
                this.parseLocation(locations));
    }

    Collection<PointOfInterest> findByLocationAndRadius(final Point location, final Double radius) {
        var results = this.poiLocations.search(
                LOCATION_GEOSPATIAL_KEY,
                GeoReference.fromCoordinate(location),
                new Distance(radius),
                newGeoSearchArgs()
                        .sortAscending()
                        .limit(20)
                        .includeDistance()
                        .includeCoordinates());

        if (results == null) {
            log.info("search results=<EMPTY>");
            return Collections.emptyList();
        }

        log.info("search results={}, averageDist={}", results, results.getAverageDistance());

        return results.getContent().stream()
                .map(GeoResult::getContent)
                .map(GeoLocation::getName)
                .map(this::findByPoiId)
                .toList();
    }

    private Point parseLocation(final Collection<Point> locations) {
        if (locations == null || locations.isEmpty()) {
            throw new RuntimeException(new IllegalStateException("location is empty"));
        }
        return locations.iterator().next();
    }

}
