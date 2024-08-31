package dev.jihogrammer.redistudy.localsearch.adaptor.redis;

import dev.jihogrammer.redistudy.poi.application.port.out.PointOfInterests;
import dev.jihogrammer.redistudy.poi.domain.PointOfInterest;
import dev.jihogrammer.redistudy.poi.domain.SearchCondition;
import dev.jihogrammer.redistudy.poi.domain.vo.Location;
import dev.jihogrammer.redistudy.poi.domain.vo.PointOfInterestId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
class RedisPoiRepository implements PointOfInterests {

    private final RedisHashPointOfInterestRepository metaRepository;

    private final RedisGeospatialPointOfInterestRepository poiLocationRepository;

    private final RedisSetPointOfInterestRepository poiTagRepository;

    @Override
    public Optional<PointOfInterest> findById(final PointOfInterestId id) {
        return this.metaRepository.findById(id.value())
                .map(meta -> {
                    this.poiLocationRepository.findById(id.value())
                            .ifPresent(meta::setLocation);
                    meta.setTags(this.poiTagRepository.findById(id.value()));

                    return meta;
                })
                .map(RedisPointOfInterest::toPointOfInterest);
    }

    @Override
    public Collection<PointOfInterest> findAllBySearchCondition(final SearchCondition condition) {
        final double x = condition.location().longitude();
        final double y = condition.location().latitude();
        final double radius = condition.radius().meterValue();

        return this.poiLocationRepository.findByLocationAndRadius(x, y, radius).stream()
                .map(geoResult -> {
                    var content = geoResult.getContent();

                    var id = content.getName();
                    var optionalMeta = this.metaRepository.findById(id);
                    if (optionalMeta.isEmpty()) {
                        return null;
                    }

                    var poi = optionalMeta.get();

                    var point = content.getPoint();
                    if (point != null) {
                        poi.setLocation(new Location(point.getY(), point.getX()));
                    }

                    var tags = this.poiTagRepository.findById(id);
                    poi.setTags(tags);

                    return poi;
                })
                .filter(Objects::nonNull)
                .map(RedisPointOfInterest::toPointOfInterest)
                .toList();
    }

}
