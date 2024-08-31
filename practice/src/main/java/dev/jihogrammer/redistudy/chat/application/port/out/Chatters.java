package dev.jihogrammer.redistudy.chat.application.port.out;

import dev.jihogrammer.redistudy.chat.domain.Chatter;
import dev.jihogrammer.redistudy.chat.domain.ChatterId;

public interface Chatters {

    Chatter register(String name);

    Chatter findById(ChatterId id);

}
