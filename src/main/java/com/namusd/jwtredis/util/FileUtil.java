package com.namusd.jwtredis.util;

import java.io.File;

public class FileUtil {

    public static String buildFilePath(String dir, String fileName) {
        return String.format("%s/%s", dir, fileName);
    }
}
