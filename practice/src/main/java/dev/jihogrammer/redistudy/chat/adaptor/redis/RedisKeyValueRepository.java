package dev.jihogrammer.redistudy.chat.adaptor.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

class RedisKeyValueRepository<K, V> {

    private final ValueOperations<K, V> ops;

    RedisKeyValueRepository(final RedisTemplate<K, V> redisTemplate) {
        this.ops = redisTemplate.opsForValue();
    }

    void save(K key, V value) {
        this.ops.set(key, value);
    }

    V findByKey(K key) {
        return this.ops.get(key);
    }

}
