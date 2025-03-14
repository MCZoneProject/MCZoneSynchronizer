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
        try (
            var byteArrayOutputStream = new ByteArrayOutputStream();
            var dataOutputStream = new DataOutputStream(byteArrayOutputStream)
        ) {
            NbtIo.writeCompressed(compoundTag, dataOutputStream);
            var byteArray = byteArrayOutputStream.toByteArray();
            return ByteArraySerializer.toCompressedByteArray(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<MobEffectInstance> toDecompressed(byte[] compressedByteArray) {
        var byteArray = ByteArraySerializer.toDecompressedByteArray(compressedByteArray);
        try (
            var byteArrayInputStream = new ByteArrayInputStream(byteArray);
            var dataInputStream = new DataInputStream(byteArrayInputStream)
        ) {
            var compoundTag = NbtIo.readCompressed(dataInputStream);
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
