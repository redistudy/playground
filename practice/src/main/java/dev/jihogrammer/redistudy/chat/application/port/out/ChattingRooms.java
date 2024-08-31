package dev.jihogrammer.redistudy.chat.application.port.out;

import dev.jihogrammer.redistudy.chat.domain.ChatterId;
import dev.jihogrammer.redistudy.chat.domain.ChattingRoom;
import dev.jihogrammer.redistudy.chat.domain.ChattingRoomId;

import java.util.Collection;

public interface ChattingRooms {

    ChattingRoom save(Collection<Chatters> chatters, String name);

    Collection<ChattingRoom> findAll();

    ChattingRoom findById(ChattingRoomId id);

    Collection<ChattingRoom> findByChatterId(ChatterId chatterId);

}
