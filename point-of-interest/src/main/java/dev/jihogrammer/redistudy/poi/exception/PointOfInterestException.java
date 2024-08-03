package dev.jihogrammer.redistudy.poi.exception;

public class PointOfInterestException extends RuntimeException {

    public PointOfInterestException(final String message) {
        super(message);
    }

    public PointOfInterestException(final Throwable cause) {
        super(cause);
    }

    public PointOfInterestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static PointOfInterestException illegalArgument(final String message) {
        return new PointOfInterestException(new IllegalArgumentException(message));
    }

}
