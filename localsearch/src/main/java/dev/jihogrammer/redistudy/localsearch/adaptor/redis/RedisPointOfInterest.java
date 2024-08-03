package dev.jihogrammer.redistudy.localsearch.adaptor.redis;

import dev.jihogrammer.redistudy.poi.domain.PointOfInterest;
import dev.jihogrammer.redistudy.poi.domain.vo.Location;
import dev.jihogrammer.redistudy.poi.domain.vo.PointOfInterestId;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Collection;

@Data
@RedisHash("poi")
class RedisPointOfInterest {

    @Id
    private Integer id;

    private String name;

    private String address;

    private Location location;

    private Collection<String> tags;

    PointOfInterest toPointOfInterest() {
        return new PointOfInterest(
                new PointOfInterestId(this.id),
                this.name,
                this.address,
                this.location,
                this.tags);
    }

}
