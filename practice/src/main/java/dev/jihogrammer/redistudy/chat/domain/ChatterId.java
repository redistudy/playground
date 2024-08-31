package dev.jihogrammer.redistudy.chat.domain;

public record ChatterId(Long value) {

    public ChatterId {
        if (value == null || value < 0) {
            throw new RuntimeException("chatter id is not valid.");
        }
    }

}
