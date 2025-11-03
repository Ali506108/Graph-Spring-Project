package org.neoj4.movieservice.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {



    private final AmazonS3 amazonS3;


    private final String bucketName;


    public S3Service(AmazonS3 amazonS3,@Value("${aws.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }


    public String uploadFile(MultipartFile file) {
        String filename = generateFileName(file.getOriginalFilename());
        try{
            File converFile = convertFileToS3(file);

            PutObjectRequest request = new PutObjectRequest(bucketName , filename , converFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(request);
            converFile.deleteOnExit();

            log.info("File uploaded successfully");

            return filename;
        }catch(AmazonServiceException e) {
            log.error(e.getMessage());
            throw new AmazonClientException("Cannot upload file");
        }catch(IOException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException("Cannot upload file");
        }
    }

    public InputStream downloadFile(String filename) {
        try {
            S3Object object = amazonS3.getObject(bucketName , filename);
            log.info("File Downloaded successfully");
            return object.getObjectContent();
        }catch(AmazonServiceException e) {
            log.error(e.getMessage());
            throw new AmazonClientException("Cannot download file");
        }
    }

    public String getFile(String filename) {
        try{
            URL url = amazonS3.getUrl(bucketName , filename);
            log.info("File Downloaded successfully");

            return url.toString();

        }catch(AmazonServiceException e) {
            log.error(e.getMessage());
            throw new AmazonClientException("Cannot download file");
        }
    }


    public void deleteFile(String filename) {
        try{
            amazonS3.deleteObject(bucketName,filename);
            log.info("File Deleted successfully");

        }catch (AmazonServiceException e) {
            log.error(e.getMessage());
            throw new AmazonClientException("Cannot delete file");
        }
    }

    public boolean exist(String filename) {
        try {
            log.info("Checking if file exists: " + filename);
            return amazonS3.doesObjectExist(bucketName, filename);
        }catch(AmazonServiceException e) {
            log.error(e.getMessage());
            throw new AmazonClientException("Cannot upload file");
        }
    }

    public ObjectMetadata getOFileMetadata(String file) {
        try {
            ObjectMetadata metadata = amazonS3.getObjectMetadata(bucketName, file);
            log.info("Getting object metadata for file {}", file);
            return metadata;

        }catch(AmazonServiceException e) {
            log.error(e.getMessage());
            throw new AmazonClientException("Cannot get object metadata");
        }
    }
    private String generateFileName(String filename) {
        return UUID.randomUUID().toString() + filename + "_" + Instant.now();
    }

    private File convertFileToS3(MultipartFile file) throws IOException {
        File converted = new File(Objects.requireNonNull(file.getOriginalFilename()));

        try (FileOutputStream fos = new FileOutputStream(converted)){
            fos.write(file.getBytes());
        }

        return converted;
    }
}
