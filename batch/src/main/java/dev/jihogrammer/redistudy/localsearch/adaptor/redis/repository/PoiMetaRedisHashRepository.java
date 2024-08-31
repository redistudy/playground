package dev.jihogrammer.redistudy.localsearch.adaptor.redis.repository;

import dev.jihogrammer.redistudy.localsearch.adaptor.redis.entity.PointOfInterestRedisEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiMetaRedisHashRepository
        extends CrudRepository<PointOfInterestRedisEntity, Integer> {
}
