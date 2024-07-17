package dev.jihogrammer.redistudy.localsearch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@Configuration
class RedisConfig {

    private final String host;

    private final Integer port;

    public RedisConfig(
            @Value("${spring.redis.host:localhost}") final String host,
            @Value("${spring.redis.port:6379}") final Integer port
    ) {
        this.host = host;
        this.port = port;
    }

    @Bean
    RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(this.host, this.port);
    }

    @Bean
    RedisTemplate<?, ?> redisTemplate() {
        var redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(this.redisConnectionFactory());
        return redisTemplate;
    }

}
