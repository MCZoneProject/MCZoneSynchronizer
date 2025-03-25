package kr.cosine.mczone.synchronizer.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MobEffectSerializer {
    private static final String MOB_EFFECT_KEY = "effect";

    public static byte[] toCompressed(Collection<MobEffectInstance> mobEffects) {
        var compoundTag = new CompoundTag();
        var listTag = new ListTag();
        for (MobEffectInstance mobEffectInstance : mobEffects) {
            var mobEffectCompoundTag = new CompoundTag();
            mobEffectInstance.save(mobEffectCompoundTag);
            listTag.add(mobEffectCompoundTag);
        }
        compoundTag.put(MOB_EFFECT_KEY, listTag);
        return CompoundTagSerializer.toCompressed(compoundTag);
    }

    public static List<MobEffectInstance> toDecompressed(byte[] compressedByteArray) {
        var compoundTag = CompoundTagSerializer.toDecompressed(compressedByteArray);
        if (compoundTag == null) return null;
        var listTag = compoundTag.getList(MOB_EFFECT_KEY, Tag.TAG_COMPOUND);
        var mobEffects = new ArrayList<MobEffectInstance>();
        for (int i = 0; i < listTag.size(); ++i) {
            var mobEffectCompoundTag = listTag.getCompound(i);
            var mobEffectInstance = MobEffectInstance.load(mobEffectCompoundTag);
            if (mobEffectInstance != null) {
                mobEffects.add(mobEffectInstance);
            }
        }
        return mobEffects;
    }
}
