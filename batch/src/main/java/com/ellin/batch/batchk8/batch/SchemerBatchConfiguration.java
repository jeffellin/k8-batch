package com.ellin.batch.batchk8.batch;

import com.ellin.batch.batchk8.service.S3Service;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.deployer.resource.docker.DockerResource;
import org.springframework.cloud.deployer.spi.task.TaskLauncher;
import org.springframework.cloud.task.batch.partition.CommandLineArgsProvider;
import org.springframework.cloud.task.batch.partition.DeployerPartitionHandler;
import org.springframework.cloud.task.batch.partition.NoOpEnvironmentVariablesProvider;
import org.springframework.cloud.task.batch.partition.PassThroughCommandLineArgsProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;

import java.util.ArrayList;
import java.util.List;


@Configuration
@Profile("!patsy")
public class SchemerBatchConfiguration {  //extends PatsyJobConfiguration {

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


/*    @Bean
    public TaskExecutorPartitionHandler partitionHandler(TaskExecutor taskExecutor){

        TaskExecutorPartitionHandler partitionerHandler = new TaskExecutorPartitionHandler();
        partitionerHandler.setTaskExecutor(taskExecutor);
        partitionerHandler.setGridSize(10);
        partitionerHandler.setStep(load(null));

        return partitionerHandler;
    }*/



    @Bean
    public PassThroughCommandLineArgsProvider commandLineArgsProvider() {

        List<String> commandLineArgs = new ArrayList<>(4);
        commandLineArgs.add("--spring.profiles.active=patsy");
        commandLineArgs.add("--spring.cloud.task.initialize.enable=false");
        commandLineArgs.add("--spring.batch.initializer.enabled=false");
        commandLineArgs.add("--spring.datasource.initialize=false");

        PassThroughCommandLineArgsProvider provider = new PassThroughCommandLineArgsProvider(commandLineArgs);

        return provider;
    }

    @Bean
    public DeployerPartitionHandler partitionHandler(
            @Value("${spring.application.name}") String applicationName,
            ApplicationContext context,
            TaskLauncher taskLauncher,
            JobExplorer jobExplorer,
            CommandLineArgsProvider commandLineArgsProvider) {

        Resource resource =  new DockerResource(
                "harbor.ellin.net/library/batch-k8-task");

        DeployerPartitionHandler partitionHandler =
                new DeployerPartitionHandler(taskLauncher,
                        jobExplorer,
                        resource,
                        "load");

        partitionHandler.setCommandLineArgsProvider(commandLineArgsProvider);
        partitionHandler.setEnvironmentVariablesProvider(new NoOpEnvironmentVariablesProvider());
        partitionHandler.setMaxWorkers(2);
        partitionHandler.setApplicationName(applicationName);

        return partitionHandler;
    }
}



