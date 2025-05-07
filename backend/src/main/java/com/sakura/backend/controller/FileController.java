package com.sakura.backend.controller;

import com.sakura.backend.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/file/upload")
    public ResponseEntity<?> uploadFile(@RequestBody MultipartFile file) {
        String filePath = fileService.uploadFile(file);
        if (filePath == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(filePath);
    }

    @GetMapping(value = "/file/{fileName}", produces = "application/epub+zip")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        byte[] file = fileService.downloadFile(fileName);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(file);
    }

}
