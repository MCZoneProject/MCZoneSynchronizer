package kr.cosine.mczone.synchronizer.util;

import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.Inventory;

import java.io.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

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
            return toCompressedByteArray(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ListTag toDecompressed(byte[] compressedByteArray) {
        var byteArray = toDecompressedByteArray(compressedByteArray);
        try (
            var byteArrayInputStream = new ByteArrayInputStream(byteArray);
            var dataInputStream = new DataInputStream(byteArrayInputStream)
        ) {
            var compoundTag = NbtIo.readCompressed(dataInputStream);
            return compoundTag.getList(ITEMS_KEY, Tag.TAG_COMPOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] toCompressedByteArray(byte[] byteArray) {
        var deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(byteArray);
        deflater.finish();
        var out = new ByteArrayOutputStream(byteArray.length);
        var buffer = new byte[1024];
        while (!deflater.finished()) {
            var count = deflater.deflate(buffer);
            out.write(buffer, 0, count);
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    private static byte[] toDecompressedByteArray(byte[] byteArray) {
        var inflater = new Inflater();
        inflater.setInput(byteArray);
        var out = new ByteArrayOutputStream(byteArray.length);
        var buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                var count = inflater.inflate(buffer);
                out.write(buffer, 0, count);
            }
            out.close();
        } catch (IOException | DataFormatException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
