package dev.jihogrammer.redistudy.localsearch;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisPoiRepositoryTest {

    @Autowired
    RedisTemplate<String, ?> redisTemplate;

    @Autowired
    RedisPoiRepository repository;

    @AfterEach
    @SuppressWarnings("all")
    void tearDown() {
        this.redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void save() {
        // given
        var poi = new PointOfInterest("1", "hello", "addr1", new Point(12, 34));

        // when
        repository.save(poi);

        // then
        var foundPoi = repository.findByPoiId(poi.id());
        assertThat(foundPoi.id()).isEqualTo(poi.id());
        assertThat(foundPoi.name()).isEqualTo(poi.name());
        assertThat(foundPoi.location().getX()).isEqualTo(poi.location().getX(), Offset.offset(0.1));
        assertThat(foundPoi.location().getY()).isEqualTo(poi.location().getY(), Offset.offset(0.1));
    }

    @Test
    void findByPoiId() {
        // given
        var poi1 = new PointOfInterest("1", "AAA", "addrA", new Point(12, 34));
        var poi2 = new PointOfInterest("2", "BBB", "addrB", new Point(34, 56));
        var poi3 = new PointOfInterest("3", "CCC", "addrC", new Point(56, 67));

        repository.save(poi1);
        repository.save(poi2);
        repository.save(poi3);

        // when
        var foundPoi1 = repository.findByPoiId(poi1.id());
        var foundPoi2 = repository.findByPoiId(poi2.id());
        var foundPoi3 = repository.findByPoiId(poi3.id());

        // then
        assertThat(foundPoi1.id()).isEqualTo(poi1.id());
        assertThat(foundPoi1.name()).isEqualTo(poi1.name());
        assertThat(foundPoi1.location().getX()).isEqualTo(poi1.location().getX(), Offset.offset(0.1));
        assertThat(foundPoi1.location().getY()).isEqualTo(poi1.location().getY(), Offset.offset(0.1));

        assertThat(foundPoi2.id()).isEqualTo(poi2.id());
        assertThat(foundPoi2.name()).isEqualTo(poi2.name());
        assertThat(foundPoi2.location().getX()).isEqualTo(poi2.location().getX(), Offset.offset(0.1));
        assertThat(foundPoi2.location().getY()).isEqualTo(poi2.location().getY(), Offset.offset(0.1));

        assertThat(foundPoi3.id()).isEqualTo(poi3.id());
        assertThat(foundPoi3.name()).isEqualTo(poi3.name());
        assertThat(foundPoi3.location().getX()).isEqualTo(poi3.location().getX(), Offset.offset(0.1));
        assertThat(foundPoi3.location().getY()).isEqualTo(poi3.location().getY(), Offset.offset(0.1));
    }

    @Test
    void findByLocationAndRadius() {
        // given
        var poi1 = new PointOfInterest("1", "AAA", "addrA", new Point(12, 34));
        var poi2 = new PointOfInterest("2", "BBB", "addrB", new Point(34, 56));
        var poi3 = new PointOfInterest("3", "CCC", "addrC", new Point(56, 67));

        repository.save(poi1);
        repository.save(poi2);
        repository.save(poi3);

        // when
        var poiList = repository.findByLocationAndRadius(poi1.location(), 1000D);

        // then
        assertThat(poiList).isNotEmpty();
        var foundPoi = poiList.iterator().next();
        assertThat(foundPoi.id()).isEqualTo(poi1.id());
        assertThat(foundPoi.name()).isEqualTo(poi1.name());
        assertThat(foundPoi.location().getX()).isEqualTo(poi1.location().getX(), Offset.offset(0.1));
        assertThat(foundPoi.location().getY()).isEqualTo(poi1.location().getY(), Offset.offset(0.1));
    }

}
