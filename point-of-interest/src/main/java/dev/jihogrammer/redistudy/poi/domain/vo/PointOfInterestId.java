package dev.jihogrammer.redistudy.poi.domain.vo;

import dev.jihogrammer.redistudy.poi.exception.PointOfInterestException;

public record PointOfInterestId(Integer value) {

    public PointOfInterestId {
        if (value == null || value < 0) {
            throw new PointOfInterestException(new IllegalArgumentException("poi id value is not valid."));
        }
    }

}
