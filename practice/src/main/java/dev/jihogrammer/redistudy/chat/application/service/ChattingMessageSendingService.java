package dev.jihogrammer.redistudy.chat.application.service;

import dev.jihogrammer.redistudy.chat.application.port.in.ChattingMessageSendingPort;
import dev.jihogrammer.redistudy.chat.application.port.out.ChattingMessages;
import dev.jihogrammer.redistudy.chat.domain.ChatterId;
import dev.jihogrammer.redistudy.chat.domain.ChattingMessage;
import dev.jihogrammer.redistudy.chat.domain.ChattingRoomId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingMessageSendingService implements ChattingMessageSendingPort {

    private final ChattingMessages chattingMessages;

    @Override
    public ChattingMessage sendMessage(ChatterId chatterId, String message, ChattingRoomId roomId) {
        return this.chattingMessages.save(roomId, chatterId, message);
    }

}
