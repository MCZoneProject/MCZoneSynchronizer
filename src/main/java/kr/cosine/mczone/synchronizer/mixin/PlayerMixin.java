package kr.cosine.mczone.synchronizer.mixin;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Redirect(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;load(Lnet/minecraft/nbt/ListTag;)V"))
    public void load(Inventory inventory, ListTag listTag) {}

    @Redirect(method = "readAdditionalSaveData", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Inventory;selected:I"))
    public void selected(Inventory inventory, int selectedItemSlot) {}
}
