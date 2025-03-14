package kr.cosine.mczone.synchronizer.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @Shadow
    private int foodLevel;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(Player player, CallbackInfo callbackInfo) {
        foodLevel = 20;
        callbackInfo.cancel();
    }
}
