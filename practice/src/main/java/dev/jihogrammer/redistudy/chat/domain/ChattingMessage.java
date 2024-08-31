package dev.jihogrammer.redistudy.chat.domain;

import java.time.LocalDateTime;

public record ChattingMessage(
        ChattingRoomId chattingRoomId,
        ChatterId chatterId,
        String value,
        LocalDateTime timestamp
) {

    public ChattingMessage {
        if (chattingRoomId == null) {
            throw new RuntimeException("chattingRoom id is null.");
        }
        if (chatterId == null) {
            throw new RuntimeException("chatter id is null.");
        }
        if (value == null || value.isBlank()) {
            throw new RuntimeException("chattingMessage value is empty.");
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    public static ChattingMessage of(ChattingRoomId roomId, ChatterId chatterId, String message) {
        return new ChattingMessage(roomId, chatterId, message, LocalDateTime.now());
    }

}
