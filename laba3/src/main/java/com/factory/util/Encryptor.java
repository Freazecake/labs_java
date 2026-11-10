package com.factory.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class Encryptor {
    private static final String AES = "AES";

    /** MD5 — для пароля */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 error", e);
        }
    }

    /** AES — для данных с ключом (ключ сводится к 16 байтам через MD5) */
    public static String encryptAes(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, buildKey(key));
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("UTF-8")));
    }

    public static String decryptAes(String encrypted, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, buildKey(key));
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)), "UTF-8");
    }

    private static SecretKeySpec buildKey(String key) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return new SecretKeySpec(md.digest(key.getBytes("UTF-8")), AES);
    }
}