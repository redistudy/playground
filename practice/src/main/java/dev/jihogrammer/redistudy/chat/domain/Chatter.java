package dev.jihogrammer.redistudy.chat.domain;

public record Chatter(
        ChatterId id,
        String name
) {

    public Chatter {
        if (id == null) {
            throw new RuntimeException("chatter id is null.");
        }
        if (name == null || name.isBlank()) {
            throw new RuntimeException("chatter name is blank.");
        }
    }

}
