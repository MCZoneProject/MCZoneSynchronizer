package kr.cosine.mczone.synchronizer.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ByteArraySerializer {
    public static byte[] toCompressedByteArray(byte[] byteArray) {
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

    public static byte[] toDecompressedByteArray(byte[] byteArray) {
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
