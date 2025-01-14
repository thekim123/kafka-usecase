package com.namusd.jwtredis.util;

import java.io.File;

public class FileUtil {

    public static String buildFilePath(String dir, String fileName) {
        return String.format("%s/%s", dir, fileName);
    }

    public static String buildMinioUrl(String endpoint, String bucket, String filePath) {
        return endpoint + "/" + bucket + "/" + filePath;
    }
}
