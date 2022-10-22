package it.stredox02.chattranslator.bukkit.config;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import it.stredox02.chattranslator.bukkit.ChatTranslator;
import it.stredox02.chattranslator.bukkit.exceptions.ConfigReloadErrorException;
import it.stredox02.chattranslator.common.config.ConfigManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ConfigManagerImpl implements ConfigManager {

    private final ChatTranslator chatTranslator;

    private final Map<String, YamlFile> files;

    public ConfigManagerImpl(ChatTranslator chatTranslator) {
        this.chatTranslator = chatTranslator;

        files = new HashMap<>();

        init();
    }

    private void init() {
        YamlFile messageFile = new YamlFile(chatTranslator.getDataFolder() + "/messages.yml");
        try {
            if (!messageFile.exists()) {
                messageFile.createNewFile(true);
                messageFile.set("usage", "&8» &7/chattranslator reload &8- &eReload the configuraton" +
                        "\n&8» &7/chattranslator version &8- &eShow the plugin info" +
                        "\n&8» &7/language &8- &eTo chose your language");
                messageFile.set("nopermission", "&8» &cSorry but you don't have permission");
                messageFile.set("config.reload.error", "&8» &cConfig reload failed");
                messageFile.set("config.reload.success", "&8» &aConfiguration reloaded! Keep in mind not all features can be reloaded using the command, stop the server instead for a full reload!");
                messageFile.set("gui.translator.title", "&8Chose your language");
                messageFile.set("gui.translator.open", "&8» &aChose the language you speak");
                messageFile.set("gui.translator.success", "&8» &aLanguage changed to: ");
                messageFile.save();
            }
            messageFile.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        YamlFile configFile = new YamlFile(chatTranslator.getDataFolder() + "/config.yml");
        try {
            if (!configFile.exists()) {
                configFile.createNewFile(true);

                configFile.setComment("api.deepl", "Put your deepl.com key here");
                configFile.set("api.deepl", "INSERT-KEY-HERE");

                configFile.setComment("api.maxmind", "Put your maxmind.com key here if you want");
                configFile.set("api.maxmind.enabled", false);
                configFile.set("api.maxmind.key", "INSERT-KEY-HERE");
                configFile.setBlankLine("api.maxmind");

                configFile.setComment("debug", "If you need what is going on, set this true");
                configFile.set("debug", false);
                configFile.setBlankLine("debug");

                configFile.setComment("default_language", "When MaxMind is not configured, it is assumed that the language the player will write will be EN-US");
                configFile.set("default_language", "EN-US");
                configFile.setBlankLine("default_language");

                configFile.setComment("database", "Put your database information here" +
                        "\nValid database type: MySQL");
                configFile.set("database.storage-method", "MySQL");
                configFile.set("database.host", "127.0.0.1");
                configFile.set("database.port", 3306);
                configFile.set("database.username", "root");
                configFile.set("database.password", "");
                configFile.set("database.database", "chattranslator");
                configFile.set("database.table_prefix", "ct_");
                configFile.setBlankLine("database");

                configFile.setComment("version", "DON'T TOUCH THAT");
                configFile.setBlankLine("version");
                configFile.set("version", "0.1");

                configFile.save();
            }
            configFile.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        files.put("config", configFile);
        files.put("messages", messageFile);
    }

    public void fullReload() throws ConfigReloadErrorException {
        chatTranslator.setDeepLApiKey(getString("config", "api.deepl"));
        if (chatTranslator.getDeepLApiKey() == null || chatTranslator.getDeepLApiKey().equals("INSERT-KEY-HERE")) {
            Bukkit.getPluginManager().disablePlugin(chatTranslator);
            throw new ConfigReloadErrorException("DeepL.com api key is not set, disabling plugin...");
        }

        boolean maxMindEnabled = getBoolean("config", "api.maxmind.enabled");
        chatTranslator.setMaxMindApiKey(getString("config", "api.maxmind.key"));
        if (maxMindEnabled && (chatTranslator.getMaxMindApiKey() == null || chatTranslator.getMaxMindApiKey().equals("INSERT-KEY-HERE"))) {
            Bukkit.getPluginManager().disablePlugin(chatTranslator);
            throw new ConfigReloadErrorException("MaxMind.com api key is not set, disabling plugin...");
        }

        chatTranslator.setDebug(getBoolean("config", "debug"));

        if (maxMindEnabled) {
            File dbFileZip = new File(chatTranslator.getDataFolder().getAbsolutePath() + "/dbcountry.tar.gz");
            URL contryUrl = null;
            try {
                contryUrl = new URL("https://download.maxmind.com/app/geoip_download?edition_id=GeoLite2-Country&license_key=" + chatTranslator.getMaxMindApiKey() + "&suffix=tar.gz");
            } catch (MalformedURLException e) {
                throw new ConfigReloadErrorException(e);
            }
            File dbFile = new File(chatTranslator.getDataFolder().getAbsolutePath() + "/databasecountry.mmdb");
            try {
                chatTranslator.loadMaxMind(dbFileZip, contryUrl, dbFile);
            } catch (IOException e) {
                throw new ConfigReloadErrorException(e);
            }
            try {
                DatabaseReader database = new DatabaseReader.Builder(dbFile).withCache(new CHMCache()).build();
                chatTranslator.setMaxMindAPICountry(database);
            } catch (IOException e) {
                throw new ConfigReloadErrorException(e);
            }
        }
    }

    @Override
    public int getInt(@NotNull String fileName, @NotNull String path) {
        YamlFile file = files.get(fileName);
        if(file == null){
            return 0;
        }
        return file.getInt(path);
    }

    @Override
    public String getString(@NotNull String fileName, @NotNull String path){
        YamlFile file = files.get(fileName);
        if(file == null){
            return null;
        }
        return file.getString(path);
    }

    @Override
    public boolean getBoolean(@NotNull String fileName, @NotNull String path) {
        YamlFile file = files.get(fileName);
        if (file == null) {
            return false;
        }
        return file.getBoolean(path);
    }

}
