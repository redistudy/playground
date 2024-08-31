package dev.jihogrammer.redistudy.chat.adaptor.redis;

import dev.jihogrammer.redistudy.chat.domain.ChatterId;
import dev.jihogrammer.redistudy.chat.domain.ChattingMessage;
import dev.jihogrammer.redistudy.chat.domain.ChattingRoomId;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

class ChattingMessageHashMapper implements HashMapper<ChattingMessage, String, Object> {

    @Override
    @NonNull
    public Map<String, Object> toHash(final ChattingMessage chattingMessage) {
        return Map.of(
                "room", chattingMessage.chattingRoomId().value(),
                "chatter", chattingMessage.chatterId().value(),
                "message", chattingMessage.value(),
                "timestamp", chattingMessage.timestamp().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    @Override
    @NonNull
    public ChattingMessage fromHash(final Map<String, Object> hash) {
        return new ChattingMessage(
                new ChattingRoomId((long) hash.get("room")),
                new ChatterId((long) hash.get("chatter")),
                hash.get("message").toString(),
                LocalDateTime.parse(hash.get("timestamp").toString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

}
