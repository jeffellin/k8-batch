package com.ellin.batch.batchk8.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AwsConfig {

    @Value("${s3endpoint}")
    String s3Endpoint;

    @Value("${s3Secret}")
    String s3Secret;

    @Value("${s3AccessKey}")
    String s2AccessKey;

    @Bean
    public AmazonS3 getClient() throws IOException {
        AWSCredentials credentials = new BasicAWSCredentials(s2AccessKey, s3Secret);
        ClientConfiguration clientConfiguration = new ClientConfiguration();

        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3Endpoint, Regions.US_EAST_1.name()))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        return s3Client;

    }
}
