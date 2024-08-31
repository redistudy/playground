package dev.jihogrammer.redistudy.poi.domain.vo;

import dev.jihogrammer.redistudy.poi.exception.PointOfInterestException;

public record Location(
        Double latitude,
        Double longitude
) {

    /**
     * 대구광역시 중심 위도
     */
    public static final double DEFAULT_LATITUDE = 35.8714354;

    /**
     * 대구광역시 중심 경도
     */
    public static final double DEFAULT_LONGITUDE = 128.601445;

    public static final double MIN_LATITUDE = -90;

    public static final double MAX_LATITUDE = 90;

    public static final double MIN_LONGITUDE = -180;

    public static final double MAX_LONGITUDE = 180;

    public static final Location DEFAULT = new Location(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);

    public Location {
        if (latitude == null) {
            latitude = DEFAULT_LATITUDE;
        }
        if (longitude == null) {
            longitude = DEFAULT_LONGITUDE;
        }

        if (MIN_LATITUDE > latitude || latitude > MAX_LATITUDE) {
            throw new PointOfInterestException(new IllegalArgumentException("latitude value is not valid."));
        }
        if (MIN_LONGITUDE > longitude || longitude > MAX_LONGITUDE) {
            throw new PointOfInterestException(new IllegalArgumentException("longitude value is not valid."));
        }
    }

    public static Location of(final Double latitude, final Double longitude) {
        return new Location(latitude, longitude);
    }

}
