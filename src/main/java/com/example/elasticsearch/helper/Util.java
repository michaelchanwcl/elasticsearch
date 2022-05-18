package com.example.elasticsearch.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.nio.file.Files;

@Slf4j
public class Util {
    public static String loadAsString(final String path) {
        try {
            final File resource = new ClassPathResource(path).getFile();
            return new String(Files.readAllBytes(resource.toPath()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
