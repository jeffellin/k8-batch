package com.ellin.batch.batchk8;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class BatchK8Application {


    public static void main(String[] args)  {
        SpringApplication.run(BatchK8Application.class, args);
    }


}
