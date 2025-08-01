package com.example.JobPortal.Service;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        try {
            // Ensure bucket exists
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // Unique file name
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Upload file
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
            }

            // Generate presigned URL valid for 7 days
            String presignedUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .method(Method.GET)
                    .expiry(7, TimeUnit.DAYS)
                    .build());

            return presignedUrl;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ MinIO file upload failed: " + e.getMessage());
        }
    }
}
