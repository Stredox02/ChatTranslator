package it.stredox02.chattranslator.common.databases;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.stredox02.chattranslator.common.consts.Languages;
import it.stredox02.chattranslator.common.data.PlayerData;
import it.stredox02.chattranslator.common.plugin.ChatTranslatorPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class MySQLDatabaseDriverImpl extends AbstractDatabaseDriver {

    private HikariDataSource dataSource;

    public MySQLDatabaseDriverImpl(@NotNull ChatTranslatorPlugin plugin,
                                   @NotNull String host,
                                   int port,
                                   @NotNull String username,
                                   @NotNull String password,
                                   @NotNull String database,
                                   @NotNull String tablePrefix) {
        super(plugin,host, port, username, password, database, tablePrefix);
    }

    @Override
    protected void init() {
        if (dataSource != null) {
            return;
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setPoolName("ChatTranslator Pool");

        dataSource = new HikariDataSource(config);

        try (Connection con = getConnection();
             PreparedStatement statement = con.prepareStatement("create table if not exists players (" +
                     "id int primary key auto_increment not null, " +
                     "uuid char(36) unique not null, " +
                     "source_language varchar(5) not null," +
                     "target_language varchar(5) not null)");) {
            statement.executeUpdate();

            plugin.log(Level.INFO,"&aSuccessfully connected to the database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public PlayerData getUser(@NotNull UUID playerUUID) {
        try (Connection con = getConnection();
             PreparedStatement statement = con.prepareStatement("select source_language, target_language from players where uuid = ?");) {
            statement.setString(1, playerUUID.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Languages wrappedSourceLanguage = null;
                    Languages wrappedTargetLanguage = null;

                    String sourceLanguage = resultSet.getString("source_language");
                    if (sourceLanguage != null) {
                        wrappedSourceLanguage = Languages.getFromShortCode(sourceLanguage);
                    }

                    String targetLanguage = resultSet.getString("target_language");
                    if (targetLanguage != null) {
                        wrappedTargetLanguage = Languages.getFromShortCode(targetLanguage);
                    }

                    return new PlayerData(playerUUID, wrappedSourceLanguage, wrappedTargetLanguage);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void savePlayer(@NotNull UUID playerUUID, @NotNull PlayerData playerData) {
        try (Connection con = getConnection();
             PreparedStatement statement = con.prepareStatement("insert into players (uuid, source_language, target_language) values (?, ?, ?) on duplicate key update source_language = ?, target_language = ?");) {
            statement.setString(1, playerUUID.toString());
            statement.setString(2, playerData.getSourceLanguage().getSourceLang());
            statement.setString(3, playerData.getTargetLanguage().getTargetLang());

            //Update
            statement.setString(4, playerData.getSourceLanguage().getSourceLang());
            statement.setString(5, playerData.getTargetLanguage().getTargetLang());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
