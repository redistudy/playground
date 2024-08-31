package dev.jihogrammer.redistudy.chat.domain;

import java.util.Collection;

public record ChattingRoom(
        ChattingRoomId id,
        String name,
        Collection<Chatter> chatters
) {

    public ChattingRoom {
        if (id == null) {
            throw new RuntimeException("chattingRoom id is null.");
        }
        if (name == null || name.isBlank()) {
            name = Long.toString(id.value());
        }
        if (chatters == null || chatters.isEmpty()) {
            throw new RuntimeException("chattingRoom has nobody.");
        }
    }

}
