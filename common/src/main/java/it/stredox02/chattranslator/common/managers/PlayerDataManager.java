package it.stredox02.chattranslator.common.managers;

import it.stredox02.chattranslator.common.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private static PlayerDataManager instance;

    private final Map<UUID, PlayerData> playerDatas;

    private PlayerDataManager(){
        playerDatas = new HashMap<>();
    }

    public static PlayerDataManager getInstance() {
        if(instance == null){
            instance = new PlayerDataManager();
        }
        return instance;
    }

    public PlayerData addData(Player player){
        if(playerDatas.containsKey(player.getUniqueId())){
            return playerDatas.get(player.getUniqueId());
        }
        PlayerData playerData = new PlayerData(player.getUniqueId());
        playerDatas.put(player.getUniqueId(), playerData);
        return playerData;
    }

    public PlayerData getData(Player player){
        return playerDatas.get(player.getUniqueId());
    }

    public void removeData(Player player){
        playerDatas.remove(player.getUniqueId());
    }

}
