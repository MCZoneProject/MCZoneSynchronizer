package kr.cosine.mczone.synchronizer.database.dao;

import kr.cosine.mczone.synchronizer.database.DataSource;
import kr.cosine.mczone.synchronizer.database.vo.SynchronizerVo;
import kr.cosine.mczone.synchronizer.util.InventorySerializer;
import kr.cosine.mczone.synchronizer.util.MobEffectSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

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
            "health FLOAT NOT NULL DEFAULT 100, " +
            "max_health FLOAT NOT NULL DEFAULT 100, " +
            "inventory BLOB NOT NULL, " +
            "held_item_slot TINYINT NOT NULL DEFAULT 0, " +
            "effect BLOB NOT NULL, " +
            "game_mode TINYINT NOT NULL DEFAULT 0" +
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

    public static CompletableFuture<SynchronizerVo> findByUniqueId(UUID owner) {
        return CompletableFuture.supplyAsync(() -> {
            var query = "SELECT * FROM mczone_synchronizer WHERE owner = ?";
            try (
                var connection = DataSource.getConnection();
                var preparedStatement = connection.prepareStatement(query)
            ) {
                preparedStatement.setString(1, owner.toString());
                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    var health = resultSet.getFloat("health");
                    var maxHealth = resultSet.getFloat("max_health");
                    var compressedInventory = resultSet.getBytes("inventory");
                    var inventory = InventorySerializer.toDecompressed(compressedInventory);
                    var heldItemSlot = resultSet.getInt("held_item_slot");
                    var compressedMobEffects = resultSet.getBytes("effect");
                    var mobEffects = MobEffectSerializer.toDecompressed(compressedMobEffects);
                    var gameModeId = resultSet.getInt("game_mode");
                    return new SynchronizerVo(health, maxHealth, inventory, heldItemSlot, mobEffects, gameModeId);
                } else {
                    return SynchronizerVo.getDefaultInstance();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }, EXECUTOR_SERVICE);
    }

    public static void save(ServerPlayer player) {
        var playerUniqueId = player.getStringUUID();
        var playerHealth = player.getHealth();
        var playerMaxHealth = player.getMaxHealth();
        var inventory = player.getInventory();
        player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        var compressedInventory = InventorySerializer.toCompressed(inventory);
        var mobEffects = player.getActiveEffects();
        var compressedMobEffects = MobEffectSerializer.toCompressed(mobEffects);
        var gameMode = player.gameMode.getGameModeForPlayer().getId();
        var query = "INSERT INTO mczone_synchronizer (owner, health, max_health, inventory, held_item_slot, effect, game_mode) VALUES (?, ?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "health = VALUES(health), " +
            "max_health = VALUES(max_health), " +
            "inventory = VALUES(inventory), " +
            "held_item_slot = VALUES(held_item_slot), " +
            "effect = VALUES(effect), " +
            "game_mode = VALUES(game_mode)";
        try (
            var connection = DataSource.getConnection();
            var preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, playerUniqueId);
            preparedStatement.setFloat(2, playerHealth);
            preparedStatement.setFloat(3, playerMaxHealth);
            preparedStatement.setBytes(4, compressedInventory);
            preparedStatement.setInt(5, inventory.selected);
            preparedStatement.setBytes(6, compressedMobEffects);
            preparedStatement.setInt(7, gameMode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveAsync(ServerPlayer player) {
        CompletableFuture.runAsync(() -> save(player), EXECUTOR_SERVICE);
    }
}
