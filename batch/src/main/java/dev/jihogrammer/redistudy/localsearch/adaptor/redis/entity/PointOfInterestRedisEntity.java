package dev.jihogrammer.redistudy.localsearch.adaptor.redis.entity;

import dev.jihogrammer.redistudy.poi.domain.vo.Location;
import dev.jihogrammer.redistudy.poi.domain.PointOfInterest;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Collection;

@Data
@RedisHash("poi")
public class PointOfInterestRedisEntity {

    @Id
    private Integer id;

    private String name;

    private String address;

    private Location location;

    private Collection<String> tags;

    public static PointOfInterestRedisEntity of(PointOfInterest poi) {
        var result = new PointOfInterestRedisEntity();

        result.id = poi.id().value();
        result.name = poi.name();
        result.address = poi.address();
        result.location = poi.location();
        result.tags = poi.tags();

        return result;
    }

}
