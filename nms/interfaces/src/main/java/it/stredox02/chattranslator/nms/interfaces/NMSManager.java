package it.stredox02.chattranslator.nms.interfaces;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface NMSManager {

    String HANDLER_NAME = "enhanced_translator";

    void addPlayer(@NotNull Player player);

    void removePlayer(@NotNull Player player);

}
