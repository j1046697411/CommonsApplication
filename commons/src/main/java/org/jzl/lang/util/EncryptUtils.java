package org.jzl.lang.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class EncryptUtils {

    private EncryptUtils() {
    }

    public static byte[] encrypt(Algorithm algorithm, byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm.name);
            return digest.digest(bytes);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptString(Algorithm algorithm, String text, Charset charset, String delimiter, boolean isUpper) {
        return StringUtils.toHexString(delimiter, encrypt(algorithm, text.getBytes(charset)), isUpper);
    }

    public static String md5(String delimiter, String text, boolean isUpper) {
        return encryptString(Algorithm.MD5, text, StandardCharsets.UTF_8, delimiter, isUpper);
    }

    public static String md5(String text) {
        return md5(StringUtils.EMPTY, text, true);
    }

    public static String sha1(String delimiter, String text, boolean isUpper) {
        return encryptString(Algorithm.SHA1, text, StandardCharsets.UTF_8, delimiter, isUpper);
    }

    public static String sha1(String text) {
        return sha1(StringUtils.EMPTY, text, true);
    }

    public static String sha256(String delimiter, String text, boolean isUpper) {
        return encryptString(Algorithm.SHA256, text, StandardCharsets.UTF_8, delimiter, isUpper);
    }

    public static String sha256(String text) {
        return sha256(StringUtils.EMPTY, text, true);
    }

    public static String sha512(String delimiter, String text, boolean isUpper){
        return encryptString(Algorithm.SHA512, text, StandardCharsets.UTF_8, delimiter, isUpper);
    }

    public static String sha512(String text){
        return sha512(StringUtils.EMPTY, text, true);
    }

    public enum Algorithm {
        MD5("md5"), SHA1("sha-1"), SHA256("sha-256"), SHA512("sha-512");

        private String name;

        Algorithm(String name) {
            this.name = name;
        }
    }
}
