package it.stredox02.chattranslator.common.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ArchiveUtils {

    public static void unTar(final Path inputFile, final Path outputDir) throws IOException {
        try (InputStream fi = Files.newInputStream(inputFile);
             BufferedInputStream bi = new BufferedInputStream(fi);
             GzipCompressorInputStream gzi = new GzipCompressorInputStream(bi);
             TarArchiveInputStream ti = new TarArchiveInputStream(gzi)) {

            ArchiveEntry entry;
            while ((entry = ti.getNextEntry()) != null) {
                Path targetDirResolved = outputDir.resolve(entry.getName());
                Path normalizePath = targetDirResolved.normalize();

                if (entry.isDirectory()) {
                    Files.createDirectories(normalizePath);
                } else {
                    Path parent = normalizePath.getParent();
                    if (parent != null) {
                        if (Files.notExists(parent)) {
                            Files.createDirectories(parent);
                        }
                    }

                    Files.copy(ti, normalizePath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

}
