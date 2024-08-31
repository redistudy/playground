package dev.jihogrammer.redistudy.poi.application.port.out;

import dev.jihogrammer.redistudy.poi.domain.vo.PointOfInterestId;

public interface PointOfInterestStatistics {

    void addSearchCount(PointOfInterestId id);

    double findScoreByPoiId(PointOfInterestId id);

}
