package dev.jihogrammer.redistudy.chat.application.port.in;

import dev.jihogrammer.redistudy.chat.domain.*;

public interface ChattingMessageSendingPort {

    ChattingMessage sendMessage(ChatterId chatterId, String message, ChattingRoomId chattingRoomId);

}
