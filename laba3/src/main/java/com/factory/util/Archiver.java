package com.factory.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Archiver {
    public static void zipFile(String sourceFile, String zipFile) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
                FileInputStream fis = new FileInputStream(sourceFile)) {
            zos.putNextEntry(new ZipEntry(new File(sourceFile).getName()));
            byte[] buf = new byte[4096];
            int len;
            while ((len = fis.read(buf)) > 0)
                zos.write(buf, 0, len);
            zos.closeEntry();
        }
    }
}