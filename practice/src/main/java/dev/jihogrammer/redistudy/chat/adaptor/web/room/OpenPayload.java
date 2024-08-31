package dev.jihogrammer.redistudy.chat.adaptor.web.room;

import dev.jihogrammer.redistudy.chat.domain.ChatterId;
import dev.jihogrammer.redistudy.chat.domain.ChattingRoomId;
import lombok.Data;

import java.util.Collection;
import java.util.Set;

@Data
class OpenPayload {

    private Set<Long> chatterIds;

    private ChattingRoomId chattingRoomId;

    private String chattingRoomName;

    Collection<ChatterId> chatterIds() {
        return this.chatterIds.stream()
                .map(ChatterId::new)
                .toList();
    }

}
