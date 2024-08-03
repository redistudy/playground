package dev.jihogrammer.redistudy.poi.domain;

import dev.jihogrammer.redistudy.poi.domain.vo.Location;
import dev.jihogrammer.redistudy.poi.domain.vo.PointOfInterestId;
import dev.jihogrammer.redistudy.poi.exception.PointOfInterestException;

import java.util.Collection;

public record PointOfInterest(
        PointOfInterestId id,
        String name,
        String address,
        Location location,
        Collection<String> tags
) {

    public PointOfInterest {
        if (id == null) {
            throw new PointOfInterestException(new IllegalArgumentException("poi id is null."));
        }
        if (name == null || name.isBlank()) {
            throw new PointOfInterestException(new IllegalArgumentException("poi name is blank."));
        }
    }

    public static PointOfInterest of(final PointOfInterest poi, final Location location) {
        return new PointOfInterest(
                poi.id,
                poi.name,
                poi.address,
                location,
                poi.tags);
    }

}
