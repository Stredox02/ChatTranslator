package it.stredox02.chattranslator.common.managers;

import it.stredox02.chattranslator.common.data.PlayerMessage;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class PlayerMessagesManager {

    private static PlayerMessagesManager instance;

    private final List<PlayerMessage> playerMessages;

    private PlayerMessagesManager(){
        playerMessages = new CopyOnWriteArrayList<>();

        SchedulerManager.getInstance().scheduleRepeating(() -> {
            for (PlayerMessage playerMessage : playerMessages) {
                if(playerMessage.getTimestamp() + 3_000 > System.currentTimeMillis()){
                    continue;
                }
                //When the message is too old and has not been consumed I remove it now
                playerMessages.remove(playerMessage);
            }
        },1,1, TimeUnit.SECONDS);
    }

    public static PlayerMessagesManager getInstance() {
        if(instance == null){
            instance = new PlayerMessagesManager();
        }
        return instance;
    }

    public void addPlayerMessage(@NotNull PlayerMessage playerMessage){
        playerMessages.add(playerMessage);
    }

    public PlayerMessage getMessage(@Nullable String messageToTranslate){
        for (PlayerMessage test : playerMessages) {
            if (StringUtils.contains(messageToTranslate, test.getMessage())) {
                return test;
            }
        }
        return null;
    }

    public void removeMessage(@NotNull PlayerMessage message) {
        playerMessages.remove(message);
    }

}
