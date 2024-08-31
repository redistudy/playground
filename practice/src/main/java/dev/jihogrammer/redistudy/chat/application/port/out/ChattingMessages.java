package dev.jihogrammer.redistudy.chat.application.port.out;

import dev.jihogrammer.redistudy.chat.domain.ChatterId;
import dev.jihogrammer.redistudy.chat.domain.ChattingMessage;
import dev.jihogrammer.redistudy.chat.domain.ChattingRoomId;

import java.util.Collection;

public interface ChattingMessages {

    ChattingMessage save(ChattingRoomId roomId, ChatterId chatterId, String message);

    Collection<ChattingMessage> findByChattingRoomId(ChattingRoomId chattingRoomId);

}
