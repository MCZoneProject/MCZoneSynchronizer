package kr.cosine.mczone.synchronizer.mixin;

import kr.cosine.mczone.synchronizer.database.dao.SynchronizerDao;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    public void placeNewPlayer(Connection connection, ServerPlayer serverPlayer, CallbackInfo callbackInfo) {
        SynchronizerDao.findInventory(serverPlayer.getUUID()).thenAccept((listTag) -> {
            if (listTag == null) return;
            serverPlayer.getInventory().load(listTag);
        });
    }
}
