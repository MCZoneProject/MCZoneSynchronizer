package kr.cosine.mczone.synchronizer.database.dao;

import kr.cosine.mczone.synchronizer.database.DataSource;
import kr.cosine.mczone.synchronizer.util.InventorySerializer;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SynchronizerDao {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(32);

    public static void createTable() {
        var query = "CREATE TABLE IF NOT EXISTS mczone_synchronizer (" +
            "owner VARCHAR(36) NOT NULL PRIMARY KEY, " +
            "inventory BLOB NOT NULL" +
            ") DEFAULT CHARSET=utf8mb4";
        try (
            var connection = DataSource.getConnection();
            var preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static CompletableFuture<ListTag> findInventory(UUID owner) {
        return CompletableFuture.supplyAsync(() -> {
            var query = "SELECT inventory FROM mczone_synchronizer WHERE owner = ?";
            try (
                var connection = DataSource.getConnection();
                var preparedStatement = connection.prepareStatement(query)
            ) {
                preparedStatement.setString(1, owner.toString());
                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    var compressedByteArray = resultSet.getBytes("inventory");
                    if (compressedByteArray != null) {
                        return InventorySerializer.toDecompressed(compressedByteArray);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }, EXECUTOR_SERVICE);
    }

    public static CompletableFuture<Void> setInventory(Player player) {
        return CompletableFuture.runAsync(() -> {
            var query = "INSERT INTO mczone_synchronizer (owner, inventory) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE inventory = VALUES(inventory)";
            try (
                var connection = DataSource.getConnection();
                var preparedStatement = connection.prepareStatement(query)
            ) {
                preparedStatement.setString(1, player.getStringUUID());
                preparedStatement.setBytes(2, InventorySerializer.toCompressed(player.getInventory()));
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, EXECUTOR_SERVICE);
    }
}
