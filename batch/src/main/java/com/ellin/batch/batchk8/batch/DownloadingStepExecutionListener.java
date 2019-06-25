package com.ellin.batch.batchk8.batch;

import com.ellin.batch.batchk8.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Slf4j
public class DownloadingStepExecutionListener extends StepExecutionListenerSupport {

    @Autowired
    S3Service s3Service;

    @Value("${bucketName:k8-batch}")
    String bucketName;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        String fileName = (String) stepExecution.getExecutionContext().get("keyName");
        String localFileName = null;

        try {
            localFileName = s3Service.downloadFile(bucketName,fileName);
            stepExecution.getExecutionContext().put("localFile", localFileName);
        } catch (IOException e) {
            log.error("Unable to download file {}/{}",bucketName,fileName);
            throw new RuntimeException(e);
        }

    }
}