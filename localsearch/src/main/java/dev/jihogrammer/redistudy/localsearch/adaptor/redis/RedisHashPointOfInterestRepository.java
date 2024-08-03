package dev.jihogrammer.redistudy.localsearch.adaptor.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RedisHashPointOfInterestRepository extends CrudRepository<RedisPointOfInterest, Integer> {
}
