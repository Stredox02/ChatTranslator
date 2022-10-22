package it.stredox02.chattranslator.bukkit;

import com.maxmind.geoip2.DatabaseReader;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import it.stredox02.chattranslator.bukkit.commands.ChatTranslatorCommand;
import it.stredox02.chattranslator.bukkit.commands.LanguageCommand;
import it.stredox02.chattranslator.bukkit.config.ConfigManagerImpl;
import it.stredox02.chattranslator.bukkit.gui.SourceLanguageGui;
import it.stredox02.chattranslator.bukkit.listener.PlayerListener;
import it.stredox02.chattranslator.bukkit.metrics.Metrics;
import it.stredox02.chattranslator.bukkit.scheduler.BukkitSchedulerImpl;
import it.stredox02.chattranslator.common.consts.ServerVersion;
import it.stredox02.chattranslator.common.consts.StorageType;
import it.stredox02.chattranslator.common.databases.AbstractDatabaseDriver;
import it.stredox02.chattranslator.common.databases.MySQLDatabaseDriverImpl;
import it.stredox02.chattranslator.common.managers.SchedulerManager;
import it.stredox02.chattranslator.common.plugin.ChatTranslatorPlugin;
import it.stredox02.chattranslator.common.scheduler.AbstractScheduler;
import it.stredox02.chattranslator.common.utils.ArchiveUtils;
import it.stredox02.chattranslator.common.utils.Utils;
import it.stredox02.chattranslator.nms.interfaces.NMSManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;

public class ChatTranslator extends JavaPlugin implements ChatTranslatorPlugin {

    //NMS
    @Getter
    private NMSManager nmsManager;

    //Api Key
    @Getter
    @Setter
    private String deepLApiKey;
    @Getter
    @Setter
    private String maxMindApiKey;

    //Inventories
    @Getter
    private static InventoryManager inventoryManager;

    //Guis
    @Getter
    private static SmartInventory languageSelector;

    //Database
    @Getter
    private AbstractDatabaseDriver databaseDriver;

    //Config
    @Getter
    private ConfigManagerImpl configManager;

    //MaxMind
    @Getter
    @Setter
    private DatabaseReader maxMindAPICountry;

    //Debug
    @Getter
    @Setter
    private boolean debug;

    //Scheduler
    @Getter
    private AbstractScheduler scheduler;

    @Getter
    private ServerVersion serverVersion;

    @Override
    public void onEnable() {
        try {
            if (!setupPlugin()) {
                return;
            }
            configManager = new ConfigManagerImpl(this);
            configManager.fullReload();

            inventoryManager = new InventoryManager(this);
            inventoryManager.init();

            registerListener();
            registerCommands();
            initDatabase();

            languageSelector = SmartInventory.builder()
                    .id("SourceSelectorGui")
                    .provider(new SourceLanguageGui(this))
                    .size(6, 9)
                    .title(Utils.colorize(configManager.getString("messages", "gui.translator.title")))
                    .manager(ChatTranslator.getInventoryManager())
                    .build();

            scheduler = new BukkitSchedulerImpl(this);

            Metrics metrics = new Metrics(this, 16646);

            log(Level.INFO, "&a&lChatTranslator successfully enabled");
        } catch (Exception e) {
            log(Level.SEVERE, "Unable to load the plugin: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        //Database
        if(databaseDriver != null) {
            databaseDriver.shutdown();
        }

        //Scheduler
        SchedulerManager.getInstance().shutdown();

        log(Level.INFO, "&c&lChatTranslator successfully disabled");
    }

    private void registerCommands() {
        getCommand("chattranslator").setExecutor(new ChatTranslatorCommand(this));
        getCommand("chattranslator").setAliases(Arrays.asList("translator"));
        getCommand("language").setExecutor(new LanguageCommand(this));
    }

    private void registerListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);
    }

    private boolean setupPlugin() {
        String serverVersion;
        try {
            serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

        log(Level.INFO, "Server version: " + serverVersion);

        this.serverVersion = ServerVersion.parse(serverVersion);

        try {
            nmsManager = (NMSManager) Class.forName("it.stredox02.chattranslator.nms." + serverVersion + ".NmsManagerImpl")
                    .getConstructor(ChatTranslatorPlugin.class).newInstance(this);
            return true;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initDatabase() {
        StorageType storageMethod = StorageType.parse(configManager.getString("config", "database.storage-method"));
        if (storageMethod == null) {
            return;
        }
        String host;
        int port;
        String username;
        String password;
        String database;
        String tablePrefix;
        if (storageMethod == StorageType.MYSQL) {
            host = configManager.getString("config", "database.host");
            port = configManager.getInt("config", "database.port");
            username = configManager.getString("config", "database.username");
            password = configManager.getString("config", "database.password");
            database = configManager.getString("config", "database.database");
            tablePrefix = configManager.getString("config", "database.table_prefix");

            databaseDriver = new MySQLDatabaseDriverImpl(this, host, port, username, password, database, tablePrefix);
        }
    }

    public void loadMaxMind(File dbFileZip, URL url, File dbFile) throws IOException {
        if (dbFileZip == null || url == null || dbFile == null) {
            return;
        }
        try (BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
             FileOutputStream fileOS = new FileOutputStream(dbFileZip)) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArchiveUtils.unTar(dbFileZip.toPath(), new File(getDataFolder().getAbsolutePath()).toPath());

        File path = new File(getDataFolder().getAbsolutePath());
        for (File file : path.listFiles()) {
            if (!file.isDirectory()) {
                continue;
            }
            for (File listFile : file.listFiles()) {
                if (!listFile.getName().endsWith("mmdb")) {
                    continue;
                }
                Files.copy(listFile.toPath(), dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        Files.walk(path.toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .filter(file -> !file.equals(dbFile) && !file.getName().endsWith(".yml"))
                .forEach(File::delete);
    }

    @Override
    public void log(@NotNull Level level, @NotNull String text) {
        getLogger().log(level, Utils.colorize(text));
    }

    @Override
    public JavaPlugin getPlugin() {
        return this;
    }

}