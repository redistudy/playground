package dev.jihogrammer.redistudy.poi.application.port.out;

import dev.jihogrammer.redistudy.poi.domain.*;
import dev.jihogrammer.redistudy.poi.domain.vo.PointOfInterestId;

import java.util.Collection;
import java.util.Optional;

public interface PointOfInterests {

    Optional<PointOfInterest> findById(PointOfInterestId id);

    Collection<PointOfInterest> findAllBySearchCondition(SearchCondition condition);

}
