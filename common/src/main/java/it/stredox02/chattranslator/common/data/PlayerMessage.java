package it.stredox02.chattranslator.common.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@AllArgsConstructor
@Getter
public class PlayerMessage {

    private final UUID sender;
    private final String message;
    private final Set<UUID> playerConsumed = new HashSet<>();
    private final long timestamp = System.currentTimeMillis();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerMessage message1 = (PlayerMessage) o;
        return sender.equals(message1.sender) && message.equals(message1.message);
    }

}
