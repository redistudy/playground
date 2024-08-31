package dev.jihogrammer.redistudy.poi.application.service;

import dev.jihogrammer.redistudy.poi.application.port.in.PointOfInterestQuery;
import dev.jihogrammer.redistudy.poi.application.port.out.PointOfInterestStatistics;
import dev.jihogrammer.redistudy.poi.application.port.out.PointOfInterests;
import dev.jihogrammer.redistudy.poi.domain.SearchCondition;
import dev.jihogrammer.redistudy.poi.domain.PointOfInterest;
import dev.jihogrammer.redistudy.poi.domain.vo.Location;
import dev.jihogrammer.redistudy.poi.domain.vo.PointOfInterestId;
import dev.jihogrammer.redistudy.poi.domain.vo.Radius;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
public class PointOfInterestQueryService implements PointOfInterestQuery {

    private final PointOfInterests pointOfInterests;

    private final PointOfInterestStatistics poiStatistics;

    @Override
    public Optional<PointOfInterest> searchById(final PointOfInterestId id) {
        return this.pointOfInterests.findById(id);
    }

    @Override
    public Collection<PointOfInterest> searchByLocationAndRadius(final Location location, final Radius radius) {
        var poiList = this.pointOfInterests.findAllBySearchCondition(SearchCondition.of(location, radius));

        for (final var poi : poiList) {
            this.poiStatistics.addSearchCount(poi.id());
        }

        return poiList;
    }

}
