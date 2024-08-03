package dev.jihogrammer.redistudy.localsearch.adaptor.redis.repository;

import dev.jihogrammer.redistudy.localsearch.adaptor.redis.entity.PointOfInterestRedisEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PoiLocationRedisGeoRepository {

    private final String key;

    private final GeoOperations<String, Integer> ops;

    PoiLocationRedisGeoRepository(
            @Value("${localsearch.location.key:location}") final String key,
            final RedisTemplate<String, Integer> redisTemplate
    ) {
        this.key = key;
        this.ops = redisTemplate.opsForGeo();
    }

    public void save(final PointOfInterestRedisEntity poi) {
        this.ops.add(
                this.key,
                new Point(poi.getLocation().longitude(), poi.getLocation().latitude()),
                poi.getId());
    }

}
