package dev.jihogrammer.redistudy.chat.adaptor.redis;

import dev.jihogrammer.redistudy.chat.application.port.out.Chatters;
import dev.jihogrammer.redistudy.chat.domain.Chatter;
import dev.jihogrammer.redistudy.chat.domain.ChatterId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatterRepository implements Chatters {

    private final RedisKeyValueRepository<String, String> repository;

    @Override
    public Chatter register(final String name) {
        var id = new ChatterId((long) (Math.random() * 100_000_000));
        var chatter = new Chatter(id, name);

        this.repository.save(chatter.id().value().toString(), chatter.name());
        return chatter;
    }

    @Override
    public Chatter findById(final ChatterId id) {
        var name = this.repository.findByKey(id.value().toString());
        return new Chatter(id, name);
    }

}
