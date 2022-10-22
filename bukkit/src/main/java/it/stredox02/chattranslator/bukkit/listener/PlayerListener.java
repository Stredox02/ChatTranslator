package it.stredox02.chattranslator.bukkit.listener;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import it.stredox02.chattranslator.bukkit.ChatTranslator;
import it.stredox02.chattranslator.common.consts.Languages;
import it.stredox02.chattranslator.common.data.PlayerData;
import it.stredox02.chattranslator.common.managers.PlayerDataManager;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.logging.Level;

@AllArgsConstructor
public class PlayerListener implements Listener {

    private final ChatTranslator chatTranslator;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerData playerData = PlayerDataManager.getInstance().addData(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerData databaseUser = chatTranslator.getDatabaseDriver().getUser(player.getUniqueId());
                if (databaseUser == null) {
                    chatTranslator.getDatabaseDriver().savePlayer(player.getUniqueId(), playerData);
                } else {
                    playerData.setSourceLanguage(databaseUser.getSourceLanguage());
                    playerData.setTargetLanguage(databaseUser.getTargetLanguage());
                }

                boolean shouldUseDefaultLanguage = true;
                if (chatTranslator.getMaxMindAPICountry() != null) {
                    //Set source translation for better translations with maxmind
                    if(!player.getAddress().getAddress().isSiteLocalAddress() && !player.getAddress().getAddress().isAnyLocalAddress() && !player.getAddress().getAddress().isLoopbackAddress()) {
                        CountryResponse country;
                        try {
                            country = chatTranslator.getMaxMindAPICountry().country(player.getAddress().getAddress());
                        } catch (IOException | GeoIp2Exception e) {
                            throw new RuntimeException(e);
                        }
                        if (country != null) {
                            Languages fromShortCode = Languages.getFromShortCode(country.getCountry().getIsoCode());
                            if (fromShortCode != null) {
                                playerData.setSourceLanguage(fromShortCode);
                                shouldUseDefaultLanguage = false;
                            }
                        }
                    }
                }

                if(shouldUseDefaultLanguage) {
                    chatTranslator.log(Level.INFO, "Using default language for player " + player.getName());
                    Languages fromShortCode = Languages.getFromShortCode(chatTranslator.getConfigManager().getString("config", "default_language"));
                    if (fromShortCode != null) {
                        playerData.setSourceLanguage(fromShortCode);
                    }
                }

                if (chatTranslator.isDebug()) {
                    chatTranslator.log(Level.INFO, player.getName() + "'s source language is " + playerData.getSourceLanguage().name());
                    chatTranslator.log(Level.INFO, player.getName() + "'s target language is " + playerData.getTargetLanguage().name());
                }
            }
        }.runTaskAsynchronously(chatTranslator);

        chatTranslator.getNmsManager().addPlayer(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        chatTranslator.getNmsManager().removePlayer(player);

        PlayerData playerData = PlayerDataManager.getInstance().getData(player);

        chatTranslator.getScheduler().getAsyncScheduler().run(() -> chatTranslator.getDatabaseDriver().savePlayer(player.getUniqueId(),playerData));

        PlayerDataManager.getInstance().removeData(player);
    }

}
