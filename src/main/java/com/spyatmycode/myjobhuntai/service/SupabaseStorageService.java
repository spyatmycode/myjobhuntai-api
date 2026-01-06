package com.spyatmycode.myjobhuntai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.spyatmycode.myjobhuntai.exception.FileTransferException;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import java.io.InputStream;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class SupabaseStorageService {

    private final S3Client s3Client;
    private final String bucketName;
    private final String endpoint; // <--- 1. Store this as a field

    public SupabaseStorageService(
            @Value("${supabase.storage.endpoint}") String endpoint,
            @Value("${supabase.storage.region}") String region,
            @Value("${supabase.storage.bucket-name}") String bucketName,
            @Value("${supabase.storage.access-key}") String accessKey,
            @Value("${supabase.storage.secret-key}") String secretKey) {

        this.bucketName = bucketName;
        this.endpoint = endpoint;

        // Debug logging (remove after fixing)
        log.info("=== Supabase S3 Configuration ===");
        log.info("Endpoint: {}", endpoint);
        log.info("Region: {}", region);
        log.info("Bucket: {}", bucketName);
        log.info("Access Key: {}...", accessKey.substring(0, Math.min(8, accessKey.length())));
        log.info("Secret Key: [REDACTED]");
        log.info("================================");

        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

    public String uploadResume(MultipartFile file, Long candidateId) {

        try {
            log.info("Uploading resume file for Candidate ID {}", candidateId);
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".pdf";
            String uniqueFileName = "resume_" + UUID.randomUUID().toString() + extension;

            PutObjectRequest putObj = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObj, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            log.info("Done uploading resume file for Candidate ID {}", candidateId);
            // 3. Use the stored field instead of asking the client
            return endpointToPublicUrl(uniqueFileName);
        } catch (IOException e) {
            log.info("Error uploading resume file for Candidate ID {}", candidateId);
            throw new FileTransferException("An error occured trying to upload your resume");
        }
    }

    private String endpointToPublicUrl(String key) {
        // Base: https://<project>.supabase.co/storage/v1/s3
        // Target: https://<project>.supabase.co/storage/v1/object/public/<bucket>/<key>

        return this.endpoint.replace("/s3", "/object/public/" + bucketName) + "/" + key;
    }

    public InputStream downloadResume(String fileName) {
        // If the input is a full URL, extract just the filename
        // e.g. "https://.../resume_123.pdf" -> "resume_123.pdf"
        String key = fileName;
        if (fileName.contains("/")) {
            key = fileName.substring(fileName.lastIndexOf("/") + 1);
        }

        return s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
    }
}