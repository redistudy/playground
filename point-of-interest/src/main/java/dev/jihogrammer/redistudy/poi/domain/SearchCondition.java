package dev.jihogrammer.redistudy.poi.domain;

import dev.jihogrammer.redistudy.poi.domain.vo.Location;
import dev.jihogrammer.redistudy.poi.domain.vo.Radius;

public record SearchCondition(
        Location location,
        Radius radius
) {

    public SearchCondition {
        if (location == null) {
            location = Location.DEFAULT;
        }

        if (radius == null) {
            radius = Radius.DEFAULT;
        }
    }

    public static SearchCondition of(final Location location, final Radius radius) {
        return new SearchCondition(location, radius);
    }

    public static SearchCondition of(final Double lat, final Double lon, final Double radiusInMeter) {
        return new SearchCondition(Location.of(lat, lon), Radius.meter(radiusInMeter));
    }

}
