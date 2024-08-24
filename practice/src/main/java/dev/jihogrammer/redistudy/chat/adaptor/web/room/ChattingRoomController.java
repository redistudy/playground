package dev.jihogrammer.redistudy.chat.adaptor.web.room;

import dev.jihogrammer.redistudy.chat.application.port.in.ChattingRoomManager;
import dev.jihogrammer.redistudy.chat.domain.ChattingRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatting-room")
@RequiredArgsConstructor
class ChattingRoomController {

    private final ChattingRoomManager chattingRoomManager;

    @PostMapping("/open")
    ChattingRoom open(final OpenPayload payload) {
        return this.chattingRoomManager.open(payload.chatterIds(), payload.getChattingRoomName());
    }

    @PostMapping("/join")
    ChattingRoom join(final JoinPayload payload) {
        return this.chattingRoomManager.join(payload.chatterId(), payload.getChattingRoomId());
    }

}
