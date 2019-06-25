package com.ellin.batch.batchk8.batch;

import com.ellin.batch.batchk8.service.S3Service;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;


@Configuration
@Profile("!schemer")
public class SchemerBatchConfiguration  extends PatsyJobConfiguration {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("loadflightdata")
                .start(schemerStep(null,null,null))
                .build();
    }


    @Bean
    public Partitioner partitioner(S3Service amazonS3) throws Exception {
        String[] resources = amazonS3.getFilesInBucket("k8-batch");
        S3MultiObjectPartitioner partitioner = new S3MultiObjectPartitioner();
        partitioner.setKeys(resources);
        return partitioner;
    }

    @Bean
    public Step schemerStep(StepBuilderFactory stepBuilderFactory,
                       Partitioner partitioner,
                       PartitionHandler partitionHandler) {
        return stepBuilderFactory.get("master")
                .partitioner("load", partitioner)
                .partitionHandler(partitionHandler)
                .build();
    }


    @Bean
    public TaskExecutorPartitionHandler partitionHandler(TaskExecutor taskExecutor){

        TaskExecutorPartitionHandler partitionerHandler = new TaskExecutorPartitionHandler();
        partitionerHandler.setTaskExecutor(taskExecutor);
        partitionerHandler.setGridSize(10);
        partitionerHandler.setStep(load(null));

        return partitionerHandler;
    }




}
