package com.namusd.jwtredis.util;

import com.namusd.jwtredis.model.constant.FileNameConstant;

import java.io.File;

public class FileUtil {

    public static String buildFilePath(String dir, String fileName) {
        return String.format("%s/%s", dir, fileName);
    }

    public static String buildMinioUrl(String endpoint, String bucket, String filePath) {
        return endpoint + "/" + bucket + "/" + filePath;
    }

    /**
     * 파일의 확장자를 반환하는 메서드
     * @param filename
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return ""; // 확장자가 없는 경우
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    /**
     *  파일 확장자 추출하고 새로운 파일명 생성
     *
     * @param originalFileName  (기존 파일명)
     * @param newFileName   (FileNameConstraint)
     * @return FileNameConstraint + extension
     */
    public static String makeNewFileName(String originalFileName, String newFileName) {
        // 기존 파일명에서 확장자 추출
        String extension = getFileExtension(originalFileName);
        return newFileName + extension;
    }

    /**
     * 파일명에 .json 확장자 여부를 확인하고 추가해주는 메서드
     */
    public static String withJsonExtension(String fileName) {
        if (!fileName.endsWith(FileNameConstant.EXTENSION_JSON)) {
            return fileName + FileNameConstant.EXTENSION_JSON;
        }
        return fileName;
    }

}
