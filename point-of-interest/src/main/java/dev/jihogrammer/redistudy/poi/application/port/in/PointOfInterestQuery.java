package dev.jihogrammer.redistudy.poi.application.port.in;

import dev.jihogrammer.redistudy.poi.domain.PointOfInterest;
import dev.jihogrammer.redistudy.poi.domain.vo.Location;
import dev.jihogrammer.redistudy.poi.domain.vo.PointOfInterestId;
import dev.jihogrammer.redistudy.poi.domain.vo.Radius;

import java.util.Collection;
import java.util.Optional;

public interface PointOfInterestQuery {

    Optional<PointOfInterest> searchById(PointOfInterestId id);

    Collection<PointOfInterest> searchByLocationAndRadius(Location location, Radius radius);

}
