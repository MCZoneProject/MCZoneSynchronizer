package kr.cosine.mczone.synchronizer.service;

import kr.cosine.mczone.synchronizer.database.vo.SynchronizerVo;
import kr.cosine.mczone.synchronizer.mixin.ServerPlayerGameModeAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SynchronizerService {
    public static void sync(ServerPlayer player, SynchronizerVo synchronizerVo) {
        var maxHealth = synchronizerVo.getMaxHealth();
        var maxHealthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttribute != null) {
            maxHealthAttribute.setBaseValue(maxHealth);
        }

        var health = synchronizerVo.getHealth();
        player.setHealth(health);

        var playerInventory = player.getInventory();
        var inventory = synchronizerVo.getInventory();
        playerInventory.load(inventory);

        playerInventory.selected = synchronizerVo.getHeldItemSlot();

        player.server.execute(() -> {
            player.removeAllEffects();
            var mobEffects = synchronizerVo.getMobEffects();
            mobEffects.forEach(player::addEffect);
        });

        var gameMode = synchronizerVo.getGameMode();
        ((ServerPlayerGameModeAccessor) player.gameMode).invokeSetGameModeForPlayer(gameMode, null);
    }
}
