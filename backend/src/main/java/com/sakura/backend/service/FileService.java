package com.sakura.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
public class FileService {

    private static final String UPLOAD_FOLDER = "backend/src/main/resources/files/";
    private static final String[] ALLOWED_FILE_EXTENSIONS = {".epub"};

    public String uploadFile(MultipartFile file) {
        try {
            validateFileType(file);
            Path uploadPath = Paths.get(UPLOAD_FOLDER).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            String uniqueFileName = generateUniqueFileName(Objects.requireNonNull(file.getOriginalFilename()));
            Path filePath = uploadPath.resolve(uniqueFileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    public byte[] downloadFile(String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_FOLDER).resolve(fileName).normalize();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }

    private void validateFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        boolean validExtension = false;
        for (String extension : ALLOWED_FILE_EXTENSIONS) {
            if (fileName.toLowerCase().endsWith(extension)) {
                validExtension = true;
                break;
            }
        }

        if (!validExtension) {
            throw new IllegalArgumentException("无效的文件类型. 只允许上传: " + String.join(", ", ALLOWED_FILE_EXTENSIONS));
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        return baseName + "_" + timestamp + extension;
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_FOLDER).resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
