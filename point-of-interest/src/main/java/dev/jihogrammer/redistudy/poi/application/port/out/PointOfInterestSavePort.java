package dev.jihogrammer.redistudy.poi.application.port.out;

import dev.jihogrammer.redistudy.poi.domain.PointOfInterest;

public interface PointOfInterestSavePort {

    void save(PointOfInterest poi);

}
