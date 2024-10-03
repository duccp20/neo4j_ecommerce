package com.neo4j_ecom.demo.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@Slf4j
public class AWSS3Config {
    @Value("${aws.secret.access.key}")
    private String awsAccessKey;
    @Value("${aws.secret.access.key}")
    private String awsSecretAccessKey;
    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public TransferManager transferManager() {
        return TransferManagerBuilder.standard()
                .withS3Client(amazonS3())
                .withMultipartUploadThreshold((long) (5*1024*1024))
                .withExecutorFactory(()-> Executors.newFixedThreadPool(10))
                .build();
    }

    @Bean
    public AmazonS3 amazonS3() {

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretAccessKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(awsRegion).build();
    }
}
