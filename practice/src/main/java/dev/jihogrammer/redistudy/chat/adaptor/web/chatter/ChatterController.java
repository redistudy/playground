package dev.jihogrammer.redistudy.chat.adaptor.web.chatter;

import dev.jihogrammer.redistudy.chat.application.port.out.Chatters;
import dev.jihogrammer.redistudy.chat.domain.Chatter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatters")
@RequiredArgsConstructor
class ChatterController {

    private final Chatters chatters;

    @PostMapping
    Chatter register(final ChatterRegisterPayload payload) {
        return this.chatters.register(payload.getName());
    }

}
