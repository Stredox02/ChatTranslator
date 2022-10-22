package it.stredox02.chattranslator.common.databases;

import it.stredox02.chattranslator.common.data.PlayerData;
import it.stredox02.chattranslator.common.plugin.ChatTranslatorPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class AbstractDatabaseDriver {

    protected final ChatTranslatorPlugin plugin;

    protected final String host;
    protected final int port;
    protected final String username;
    protected final String password;
    protected final String database;
    protected final String tablePrefix;

    protected AbstractDatabaseDriver(@NotNull ChatTranslatorPlugin plugin, @NotNull String host, int port, @NotNull String username, @NotNull String password, @NotNull String database, @NotNull String tablePrefix) {
        this.plugin = plugin;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.tablePrefix = tablePrefix;

        init();
    }

    protected abstract void init();

    public abstract void shutdown();

    public abstract PlayerData getUser(@NotNull UUID playerUUID);

    public abstract void savePlayer(@NotNull UUID playerUUID, @NotNull PlayerData playerData);

}
