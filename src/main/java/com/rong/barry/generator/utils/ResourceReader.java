package com.rong.barry.generator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import lombok.experimental.UtilityClass;

/**
 * TODO
 *
 * @author gaochen
 * Created on 2019/6/21.
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
