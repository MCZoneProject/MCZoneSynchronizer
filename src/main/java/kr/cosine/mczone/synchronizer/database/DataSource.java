package kr.cosine.mczone.synchronizer.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kr.cosine.mczone.synchronizer.MCZoneSynchronizer;
import kr.cosine.mczone.synchronizer.config.SynchronizerDatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private static HikariDataSource dataSource;

    public static void connect() {
        var hikariConfig = new HikariConfig();
        var host = SynchronizerDatabaseConfig.host.get();
        var port = SynchronizerDatabaseConfig.port.get();
        var database = SynchronizerDatabaseConfig.database.get();
        var user = SynchronizerDatabaseConfig.user.get();
        var password = SynchronizerDatabaseConfig.password.get();
        var maximumPoolSize = SynchronizerDatabaseConfig.maximumPoolSize.get();
        hikariConfig.setPoolName(MCZoneSynchronizer.MOD_ID);
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        dataSource = new HikariDataSource(hikariConfig);
    }

    public static void disconnect() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
