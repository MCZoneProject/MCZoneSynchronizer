package kr.cosine.mczone.synchronizer.util;

import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.Inventory;

import java.io.*;

public class InventorySerializer {
    private static final String ITEMS_KEY = "items";

    public static byte[] toCompressed(Inventory inventory) {
        var compoundTag = new CompoundTag();
        var listTag = new ListTag();
        inventory.save(listTag);
        compoundTag.put(ITEMS_KEY, listTag);
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

    public static ListTag toDecompressed(byte[] compressedByteArray) {
        var byteArray = ByteArraySerializer.toDecompressedByteArray(compressedByteArray);
        try (
            var byteArrayInputStream = new ByteArrayInputStream(byteArray);
            var dataInputStream = new DataInputStream(byteArrayInputStream)
        ) {
            var compoundTag = NbtIo.readCompressed(dataInputStream);
            return compoundTag.getList(ITEMS_KEY, Tag.TAG_COMPOUND);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
