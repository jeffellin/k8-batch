package com.ellin.batch.batchk8.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class S3Service {

    @Autowired
    AmazonS3 s3Client;

    public String downloadFile(String bucketName, String key) throws IOException {

        String localFile  = null;
        S3Object fullObject = null, objectPortion = null, headerOverrideObject = null;
        try {

            // Get an object and print its contents.

            String tmp = (System.getProperty("java.io.tmpdir"));
            localFile= tmp+"/"+key;
            fullObject = s3Client.getObject(new GetObjectRequest(bucketName, key));
            log.info("downloading file {} from bucket {} to {}",key,bucketName,localFile);
            S3ObjectInputStream inputStream = fullObject.getObjectContent();
            FileUtils.copyInputStreamToFile(inputStream, new File(localFile));

        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        finally {
            // To ensure that the network connection doesn't remain open, close any open input streams.
            if(fullObject != null) {
                fullObject.close();
            }
            if(objectPortion != null) {
                objectPortion.close();
            }
            if(headerOverrideObject != null) {
                headerOverrideObject.close();
            }
        }

        return localFile;

    }

    public String[] getFilesInBucket(String bucketName) throws Exception {


        List<String> resourceList = new ArrayList<>();
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(2);
        ListObjectsV2Result result;
        do {
            result = s3Client.listObjectsV2(req);

            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
               log.debug("found s3 object {}",objectSummary.getKey());
               resourceList.add(objectSummary.getKey());
            }
            // If there are more than maxKeys keys in the bucket, get a continuation token
            // and list the next objects.
            String token = result.getNextContinuationToken();
            log.debug("Next Continuation Token: {} ",token);
            req.setContinuationToken(token);
        } while (result.isTruncated());

        return resourceList.toArray(new String[resourceList.size()]);
    }
}
