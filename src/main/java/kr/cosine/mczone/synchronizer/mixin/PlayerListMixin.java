package kr.cosine.mczone.synchronizer.mixin;

import kr.cosine.mczone.synchronizer.database.dao.SynchronizerDao;
import kr.cosine.mczone.synchronizer.service.SynchronizerService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    public void placeNewPlayer(Connection connection, ServerPlayer player, CallbackInfo callbackInfo) {
        SynchronizerDao.findByUniqueId(player.getUUID()).thenAccept((synchronizerVo) -> {
            if (synchronizerVo == null) return;
            SynchronizerService.sync(player, synchronizerVo);
        });
    }

    @Redirect(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;loadGameTypes(Lnet/minecraft/nbt/CompoundTag;)V"))
    public void loadGameTypes(ServerPlayer player, CompoundTag compoundTag) {}
}
