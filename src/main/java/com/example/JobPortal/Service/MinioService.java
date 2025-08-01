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
    private String primaryBucketName; // should be set as "resume" in properties

    private final String fallbackBucketName = "resumes"; // legacy bucket

    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            // Try primary bucket first
            ensureBucketExists(primaryBucketName);

            // Upload to primary bucket
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(primaryBucketName)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return generatePresignedUrl(primaryBucketName, fileName);

        } catch (Exception primaryEx) {
            System.out.println("⚠️ Primary bucket failed. Trying fallback bucket...");

            try (InputStream inputStream = file.getInputStream()) {
                // Try fallback bucket
                ensureBucketExists(fallbackBucketName);

                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(fallbackBucketName)
                        .object(fileName)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());

                return generatePresignedUrl(fallbackBucketName, fileName);

            } catch (Exception fallbackEx) {
                fallbackEx.printStackTrace();
                throw new RuntimeException("❌ Upload failed to both buckets: " + fallbackEx.getMessage());
            }
        }
    }

    private void ensureBucketExists(String bucketName) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    private String generatePresignedUrl(String bucket, String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(bucket)
                .object(objectName)
                .method(Method.GET)
                .expiry(7, TimeUnit.DAYS)
                .build());
    }
}
