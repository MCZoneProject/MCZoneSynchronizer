package kr.cosine.mczone.synchronizer.mixin;

import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

@Mixin(ServerPlayerGameMode.class)
public interface ServerPlayerGameModeAccessor {
    @Invoker
    void invokeSetGameModeForPlayer(GameType gameType, @Nullable GameType prevGameType);
}
