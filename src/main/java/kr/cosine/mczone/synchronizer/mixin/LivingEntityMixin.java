package kr.cosine.mczone.synchronizer.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    private static final ListTag EMPTY_LIST_TAG = new ListTag();

    @Shadow
    public abstract void setHealth(float p_21154_);

    @Redirect(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"))
    public void setHealth(LivingEntity livingEntity, float health) {
        if (!(livingEntity instanceof Player)) {
            setHealth(health);
        }
    }

    @Redirect(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getList(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;", ordinal = 1))
    public ListTag getList(CompoundTag compoundTag, String key, int type) {
        if ((LivingEntity) (Object) this instanceof Player) {
            return EMPTY_LIST_TAG;
        } else {
            return compoundTag.getList("ActiveEffects", Tag.TAG_COMPOUND);
        }
    }
}
