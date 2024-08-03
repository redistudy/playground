package dev.jihogrammer.redistudy.localsearch.adaptor.redis.repository;

import dev.jihogrammer.redistudy.localsearch.adaptor.redis.entity.PointOfInterestRedisEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;

@Repository
public class PoiTagRedisSetRepository {

    private final String key;

    private final SetOperations<String, String> ops;

    PoiTagRedisSetRepository(
            @Value("${localsearch.tag.key:tag}") final String key,
            final RedisTemplate<String, String> redisTemplate
    ) {
        this.key = key;
        this.ops = redisTemplate.opsForSet();
    }

    public Collection<String> findById(final Integer id) {
        var members = this.ops.members(this.key + ":" + id);

        if (members == null || members.isEmpty()) {
            return Collections.emptySet();
        }

        return members;
    }

    public void save(final PointOfInterestRedisEntity poi) {
        this.ops.add(this.key + ":" + poi.getId(), poi.getTags().toArray(String[]::new));
    }
}
