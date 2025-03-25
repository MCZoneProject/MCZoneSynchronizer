package kr.cosine.mczone.synchronizer.util;

import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.Inventory;

public class InventorySerializer {
    private static final String ITEMS_KEY = "items";

    public static byte[] toCompressed(Inventory inventory) {
        var compoundTag = new CompoundTag();
        var listTag = new ListTag();
        inventory.save(listTag);
        compoundTag.put(ITEMS_KEY, listTag);
        return CompoundTagSerializer.toCompressed(compoundTag);
    }

    public static ListTag toDecompressed(byte[] compressedByteArray) {
        var compoundTag = CompoundTagSerializer.toDecompressed(compressedByteArray);
        if (compoundTag == null) return null;
        return compoundTag.getList(ITEMS_KEY, Tag.TAG_COMPOUND);
    }
}
