package dev.jihogrammer.redistudy.localsearch.adaptor.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;

@Repository
class RedisSetPointOfInterestRepository {

    private final String key;

    private final SetOperations<String, String> ops;

    RedisSetPointOfInterestRepository(
            @Value("${localsearch.tag.key:tag}") final String key,
            final RedisTemplate<String, String> redisTemplate
    ) {
        this.key = key;
        this.ops = redisTemplate.opsForSet();
    }

    Collection<String> findById(final Integer id) {
        var members = this.ops.members(this.key + ":" + id);

        if (members == null || members.isEmpty()) {
            return Collections.emptySet();
        }

        return members;
    }

}
