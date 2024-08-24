package dev.jihogrammer.redistudy.chat.adaptor.redis;

import dev.jihogrammer.redistudy.chat.application.port.out.ChattingMessages;
import dev.jihogrammer.redistudy.chat.domain.ChatterId;
import dev.jihogrammer.redistudy.chat.domain.ChattingMessage;
import dev.jihogrammer.redistudy.chat.domain.ChattingRoomId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class ChattingMessageRepository implements ChattingMessages {

    private final RedisPublisher<ChattingMessage> publisher;

    @Override
    public ChattingMessage save(ChattingRoomId roomId, ChatterId chatterId, String message) {
        var chattingMessage = ChattingMessage.of(roomId, chatterId, message);
        publisher.add("chatting", chattingMessage);
        return null;
    }

    @Override
    public Collection<ChattingMessage> findByChattingRoomId(ChattingRoomId chattingRoomId) {
        throw new UnsupportedOperationException("TODO NOT YET");
    }
}
