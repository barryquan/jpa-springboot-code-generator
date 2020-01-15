package com.github.barry.akali.generator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import lombok.experimental.UtilityClass;

/**
 * 文件读取工具类
 *
 */
@UtilityClass
public class ResourceReader {

    public static InputStream getResourceAsStream(String path) throws IOException {
        InputStream classPathResource = ResourceReader.class.getClassLoader().getResourceAsStream(path);
        if (classPathResource != null) {
            return classPathResource;
        }
        return new FileInputStream(new File(path));
    }

}
