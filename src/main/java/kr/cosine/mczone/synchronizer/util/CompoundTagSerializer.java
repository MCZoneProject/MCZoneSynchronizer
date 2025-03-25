package kr.cosine.mczone.synchronizer.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import java.io.*;

public class CompoundTagSerializer {
    public static byte[] toCompressed(CompoundTag compoundTag) {
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

    public static CompoundTag toDecompressed(byte[] compressedByteArray) {
        var byteArray = ByteArraySerializer.toDecompressedByteArray(compressedByteArray);
        try (
            var byteArrayInputStream = new ByteArrayInputStream(byteArray);
            var dataInputStream = new DataInputStream(byteArrayInputStream)
        ) {
            return NbtIo.readCompressed(dataInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
