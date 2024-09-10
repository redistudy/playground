package dev.jihogrammer.redistudy.filebeat.web;

import static org.springframework.util.StringUtils.hasText;

record RandomValueModel(
        Object value,
        String message
) {

    public static RandomValueModel of(final Number value) {
        if (value == null) {
            throw new IllegalArgumentException("RandomValueModel value is null.");
        }
        return new RandomValueModel(value, null);
    }

    public static RandomValueModel of(final String message) {
        if (!hasText(message)) {
            throw new IllegalArgumentException("RandomValueModel message is empty.");
        }
        return new RandomValueModel(null, message);
    }

}
