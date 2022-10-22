package it.stredox02.chattranslator.common.plugin;

import it.stredox02.chattranslator.common.config.ConfigManager;
import it.stredox02.chattranslator.common.consts.ServerVersion;
import it.stredox02.chattranslator.common.databases.AbstractDatabaseDriver;
import it.stredox02.chattranslator.common.scheduler.AbstractScheduler;
import it.stredox02.chattranslator.nms.interfaces.NMSManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public interface ChatTranslatorPlugin {

    JavaPlugin getPlugin();

    NMSManager getNmsManager();

    String getDeepLApiKey();

    String getMaxMindApiKey();

    AbstractDatabaseDriver getDatabaseDriver();

    ConfigManager getConfigManager();

    boolean isDebug();

    void log(@NotNull Level level, @NotNull String text);

    AbstractScheduler getScheduler();

    ServerVersion getServerVersion();

}
