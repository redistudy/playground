package dev.jihogrammer.redistudy.chat.domain;

public record ChattingRoomId(Long value) {

    public ChattingRoomId {
        if (value == null || value < 0) {
            throw new RuntimeException("chattingRoom id is not valid.");
        }
    }

}
