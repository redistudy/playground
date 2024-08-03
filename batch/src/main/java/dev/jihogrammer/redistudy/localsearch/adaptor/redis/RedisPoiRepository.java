package dev.jihogrammer.redistudy.localsearch.adaptor.redis;

import dev.jihogrammer.redistudy.localsearch.adaptor.redis.entity.PointOfInterestRedisEntity;
import dev.jihogrammer.redistudy.localsearch.adaptor.redis.repository.PoiLocationRedisGeoRepository;
import dev.jihogrammer.redistudy.localsearch.adaptor.redis.repository.PoiMetaRedisHashRepository;
import dev.jihogrammer.redistudy.localsearch.adaptor.redis.repository.PoiTagRedisSetRepository;
import dev.jihogrammer.redistudy.poi.application.port.out.PointOfInterestSavePort;
import dev.jihogrammer.redistudy.poi.domain.PointOfInterest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
class RedisPoiRepository implements PointOfInterestSavePort {

    private final PoiMetaRedisHashRepository poiMetaRepository;

    private final PoiLocationRedisGeoRepository poiLocationRepository;

    private final PoiTagRedisSetRepository poiTagRepository;

    @Override
    public void save(final PointOfInterest poi) {
        var hash = PointOfInterestRedisEntity.of(poi);

        this.poiMetaRepository.save(hash);

        if (hash.getLocation() != null) {
            this.poiLocationRepository.save(hash);
        }

        if (hash.getTags() != null && !hash.getTags().isEmpty()) {
            this.poiTagRepository.save(hash);
        }
    }

}
