package org.jzl.lang.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class StreamUtils {

    private StreamUtils() {
    }

    public static void copyStream(final InputStream in, final OutputStream out, final byte[] bytes) throws IOException {
        Objects.requireNonNull(in);
        Objects.requireNonNull(out);
        BufferedInputStream bufferedInputStream = toBufferedInputStream(in);
        BufferedOutputStream bufferedOutputStream = toBufferedOutputStream(out);
        try {
            int length;
            while ((length = bufferedInputStream.read(bytes)) > 0) {
                bufferedOutputStream.write(bytes, 0, length);
            }
        } finally {
            close(bufferedOutputStream);
            close(bufferedInputStream);
        }

    }

    public static void copyStream(final InputStream in, final OutputStream out) throws IOException {
        copyStream(in, out, ThreadLocals.getBytes());
    }

    public static byte[] copyStreamToBytes(InputStream in, byte[] bytes) throws IOException {

        Objects.requireNonNull(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream(in.available());
        copyStream(in, out, bytes);
        return out.toByteArray();
    }

    public static byte[] copyStreamToBytes(InputStream in) throws IOException {
        return copyStreamToBytes(in, ThreadLocals.getBytes());
    }

    public static String copyStreamToString(InputStream in, byte[] bytes, Charset charset) throws IOException {
        Objects.requireNonNull(in);
        return new String(copyStreamToBytes(in, bytes), charset);
    }

    public static String copyStreamToString(InputStream in, Charset charset) throws IOException {
        return copyStreamToString(in, ThreadLocals.getBytes(), charset);
    }

    public static String copyStreamToString(InputStream in) throws IOException {
        return copyStreamToString(in, StandardCharsets.UTF_8);
    }

    public static void copyStreamToFile(InputStream in, File file, byte[] bytes) throws IOException {
        copyStream(in, new FileOutputStream(file), bytes);
    }

    public static void copyStreamToFile(InputStream in, File out) throws IOException {
        copyStreamToFile(in, out, ThreadLocals.getBytes());
    }

    public static void copyFile(File in, File out, byte[] bytes) throws IOException {
        copyStream(new FileInputStream(in), new FileOutputStream(out), bytes);
    }

    public static void copyFile(File in, File out) throws IOException {
        copyFile(in, out, ThreadLocals.getBytes());
    }

    public static BufferedInputStream toBufferedInputStream(InputStream in) {
        return in instanceof BufferedInputStream ? (BufferedInputStream) in : new BufferedInputStream(in);
    }

    public static BufferedOutputStream toBufferedOutputStream(OutputStream out) {
        return out instanceof BufferedOutputStream ? (BufferedOutputStream) out : new BufferedOutputStream(out);
    }

    public static void flush(Flushable flushable){
        if (ObjectUtils.nonNull(flushable)){
            try {
                flushable.flush();
            } catch (IOException ignored) {
            }
        }
    }

    public static void close(Closeable cls) {
        if (cls != null) {
            try {
                if (cls instanceof Flushable) {
                    ((Flushable) cls).flush();
                }
                cls.close();
            } catch (IOException ignored) {
            }
        }
    }
}
