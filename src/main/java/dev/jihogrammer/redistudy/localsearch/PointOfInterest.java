package dev.jihogrammer.redistudy.localsearch;

import org.springframework.data.geo.Point;

public record PointOfInterest(
        String id,
        String name,
        String address,
        Point location
) {

    public PointOfInterest {
        if (id == null || id.isBlank()) {
            throw new RuntimeException(new NullPointerException("id is blank."));
        }
        if (name == null || name.isBlank()) {
            throw new RuntimeException(new NullPointerException("name is blank."));
        }
    }

}
