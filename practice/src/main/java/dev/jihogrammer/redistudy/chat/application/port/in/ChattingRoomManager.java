package dev.jihogrammer.redistudy.chat.application.port.in;

import dev.jihogrammer.redistudy.chat.domain.ChatterId;
import dev.jihogrammer.redistudy.chat.domain.ChattingRoom;
import dev.jihogrammer.redistudy.chat.domain.ChattingRoomId;

import java.util.Collection;

public interface ChattingRoomManager {

    ChattingRoom open(Collection<ChatterId> chatterIds, String name);

    ChattingRoom join(ChatterId chatterId, ChattingRoomId chattingRoomId);

}
