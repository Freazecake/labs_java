package com.factory;

import com.factory.util.Archiver;
import com.factory.util.Encryptor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    @Test
    void md5Known() {
        assertEquals("21232f297a57a5a743894a0e4a801fc3", Encryptor.md5("admin"));
    }

    @Test
    void aesRoundTrip() throws Exception {
        String key = "secret-key";
        String data = "Hello, World!";
        String enc = Encryptor.encryptAes(data, key);
        assertNotEquals(data, enc);
        assertEquals(data, Encryptor.decryptAes(enc, key));
    }

    @Test
    void zipCreatesFile() throws Exception {
        String src = "src_for_zip.txt";
        String zip = "src_for_zip.zip";
        try (PrintWriter pw = new PrintWriter(src)) {
            pw.println("content");
        }
        Archiver.zipFile(src, zip);
        assertTrue(new File(zip).exists());
        new File(src).delete();
        new File(zip).delete();
    }
}