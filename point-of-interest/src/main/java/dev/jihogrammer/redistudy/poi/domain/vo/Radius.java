package dev.jihogrammer.redistudy.poi.domain.vo;

import dev.jihogrammer.redistudy.poi.exception.PointOfInterestException;

public record Radius(
        Double value,
        Unit unit
) {

    public static final double MIN_VALUE = 50;

    public static final double MAX_VALUE = 5_000;

    public static final double DEFAULT_VALUE = 500;

    public static final Radius DEFAULT = new Radius(DEFAULT_VALUE, Unit.METER);

    public Radius {
        if (value == null) {
            value = DEFAULT_VALUE;
        }

        if (unit == null) {
            unit = Unit.METER;
        }

        if (MIN_VALUE > value || value > MAX_VALUE) {
            throw PointOfInterestException.illegalArgument("radius value is not valid.");
        }
    }

    public static Radius meter(final Double value) {
        return new Radius(value, Unit.METER);
    }

    public static Radius of(final Double value, final Unit unit) {
        return new Radius(value, unit);
    }

    public double meterValue() {
        return switch (this.unit) {
            case METER -> this.value;
            case KILOMETER -> this.value * 1_000;
        };
    }

    public enum Unit {
        METER,
        KILOMETER;

        public static Unit of(final String input) {
            if (input == null || input.isBlank()) {
                return METER;
            }

            final String unitString = input.toLowerCase();
            if ("m".equals(unitString)) {
                return METER;
            }
            if ("meter".equals(unitString)) {
                return METER;
            }
            if ("km".equals(unitString)) {
                return KILOMETER;
            }
            if ("kilometer".equals(unitString)) {
                return KILOMETER;
            }

            return METER;
        }
    }

}
