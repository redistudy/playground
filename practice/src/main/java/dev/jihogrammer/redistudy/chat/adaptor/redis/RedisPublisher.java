package dev.jihogrammer.redistudy.chat.adaptor.redis;

import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.hash.HashMapper;

import java.util.Map;

class RedisPublisher<T> {

    private final StreamOperations<String, String, ?> ops;

    private final HashMapper<String, String, ?> mapper;

    RedisPublisher(final RedisTemplate<String, ?> redisTemplate, final HashMapper<String, String, ?> mapper) {
        this.ops = redisTemplate.opsForStream(mapper);
        this.mapper = mapper;
    }

    void add(final String key, final T record) {
        this.ops.add(Record.of(record));
    }

}
