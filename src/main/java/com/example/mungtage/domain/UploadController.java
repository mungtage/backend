package com.example.mungtage.domain;

import com.example.mungtage.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
@RestController
public class UploadController {
    private final S3Uploader s3Uploader;

    @PostMapping("/images")
    public ResponseEntity<String> upload(@RequestParam("image") MultipartFile multipartFile) throws IOException {
       return new ResponseEntity<>(s3Uploader.upload(multipartFile, "static"), HttpStatus.OK);
    }
}
