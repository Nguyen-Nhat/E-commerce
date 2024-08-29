package org.ckxnhat.resource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-18 21:06:30.611
 */

@Configuration
public class S3Config {
    @Value("${aws.bucket.access_key}")
    private String awsAccessKey;
    @Value("${aws.bucket.secret_key}")
    private String awsSecretKey;
    @Bean
    public S3Client s3Client(){
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(awsAccessKey,awsSecretKey)
        );
        return S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.AP_SOUTHEAST_2)
                .build();
    }

    @Bean
    public S3AsyncClient s3AsyncClient(){
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(awsAccessKey,awsSecretKey)
        );
        return S3AsyncClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.AP_SOUTHEAST_2)
                .build();
    }
}
