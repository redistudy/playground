package dev.jihogrammer.redistudy.chat.adaptor.web.room;

import dev.jihogrammer.redistudy.chat.domain.ChatterId;
import dev.jihogrammer.redistudy.chat.domain.ChattingRoomId;
import lombok.Data;

@Data
class JoinPayload {

    private Long chatterId;

    private ChattingRoomId chattingRoomId;

    ChatterId chatterId() {
        return new ChatterId(this.chatterId);
    }

}
