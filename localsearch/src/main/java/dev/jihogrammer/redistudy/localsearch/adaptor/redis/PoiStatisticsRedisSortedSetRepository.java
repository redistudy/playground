package dev.jihogrammer.redistudy.localsearch.adaptor.redis;

import dev.jihogrammer.redistudy.poi.application.port.out.PointOfInterestStatistics;
import dev.jihogrammer.redistudy.poi.domain.vo.PointOfInterestId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
class PoiStatisticsRedisSortedSetRepository implements PointOfInterestStatistics {

    private final String key;

    private final ZSetOperations<String, Integer> ops;

    PoiStatisticsRedisSortedSetRepository(
            @Value("${localsearch.statistics.key:statistics}") final String key,
            final RedisTemplate<String, Integer> redisTemplate
    ) {
        this.key = key;
        this.ops = redisTemplate.opsForZSet();
    }

    @Override
    public void addSearchCount(final PointOfInterestId id) {
        var score = this.ops.incrementScore(this.key, id.value(), 1);
        log.debug("addSearchCount={}:{}={}", this.key, id.value(), score);
    }

    @Override
    public double findScoreByPoiId(final PointOfInterestId id) {
        var score = this.ops.score(this.key, id.value());
        log.debug("addSearchCount={}:{}={}", this.key, id.value(), score);

        if (score == null) {
            return 0;
        }

        return score;
    }
}
