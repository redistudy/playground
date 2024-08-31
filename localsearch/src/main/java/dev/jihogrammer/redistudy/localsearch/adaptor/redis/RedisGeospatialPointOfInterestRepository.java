package dev.jihogrammer.redistudy.localsearch.adaptor.redis;

import dev.jihogrammer.redistudy.poi.domain.vo.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Repository
class RedisGeospatialPointOfInterestRepository {

    private final String key;

    private final GeoOperations<String, Integer> ops;

    RedisGeospatialPointOfInterestRepository(
            @Value("${localsearch.location.key:location}") final String key,
            final RedisTemplate<String, Integer> redisTemplate
    ) {
        this.key = key;
        this.ops = redisTemplate.opsForGeo();
    }

    Optional<Location> findById(final Integer id) {
        var locations = this.ops.position(this.key, id);

        if (locations == null || locations.isEmpty()) {
            return Optional.empty();
        }

        var point = locations.iterator().next();

        return Optional.of(new Location(point.getY(), point.getX()));
    }

    Collection<GeoResult<RedisGeoCommands.GeoLocation<Integer>>> findByLocationAndRadius(
            final Double x,
            final Double y,
            final Double radiusInMeter
    ) {
        var results = this.ops.search(
                this.key,
                GeoReference.fromCoordinate(x, y),
                new Distance(radiusInMeter));

        if (results == null) {
            return Collections.emptyList();
        }

        return results.getContent();
    }

}
