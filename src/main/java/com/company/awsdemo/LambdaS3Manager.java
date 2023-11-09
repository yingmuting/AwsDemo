package com.company.awsdemo;


import com.company.awsdemo.util.TimeCostUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;
import software.amazon.awssdk.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * arg[0]:region
 * arg[1]:bucketName
 */
public class LambdaS3Manager {

    public static void main(String[] args) {
//        listObjects("us-west-2", "mmt-bucket-02","");
//        listObjects("ap-northeast-1", "mmt-bucket-001", "");
    }

    private static final Logger logger = LoggerFactory.getLogger(LambdaS3Manager.class);

    public static boolean listObjects(String regionName, String bucketName, String output) {
        long start = TimeCostUtil.start();
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "4");
        if (StringUtils.isBlank(output)) {
            output = "lambda_object_list.txt";
        }
        boolean result = false;
        Region region = Region.of(regionName);
        S3Client s3 = S3Client.builder().region(region).build();
        String bucket = bucketName;
        logger.info("list objects...");
        List<String> objects = listBucketObjects(s3, bucket);
        logger.info("list objects complete");
        result = upload(s3, bucketName, output, objects);
        logger.info("upload objectList complete");
        s3.close();
        logger.info("Connection closed");
        TimeCostUtil.printCost(start);
        return result;
    }

    private static List<String> listBucketObjects(S3Client s3, String bucketName) {
        try {
            ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName).build();
            ListObjectsV2Iterable listObjectsV2Iterable = s3.listObjectsV2Paginator(listObjectsV2Request);
            List<String> keyNameList = listObjectsV2Iterable.stream().parallel()
                    .flatMap(r -> r.contents().stream())
                    .map(content -> getKeyName(content.key()))
                    .filter(content -> content.length() > 0)
                    .distinct()
                    .collect(Collectors.toList());
            logger.info("\n The first is " + keyNameList.get(0));
            return keyNameList;
        } catch (S3Exception e) {
            logger.error("S3Exception", e);
        }
        return null;
    }


    private static String getKeyName(String key) {
        int index = key.lastIndexOf("/");
        return key.substring(++index);
    }


    public static boolean upload(S3Client s3, String bucketName, String outputName, List<String> result) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName).key(outputName).build();
        RequestBody requestBody = RequestBody.empty();
        if (null != result && !result.isEmpty()) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (String line : result) {
                    baos.write((line + "\r\n").getBytes());
                }
                byte[] bytes = baos.toByteArray();
                requestBody = RequestBody.fromBytes(bytes);
            } catch (Exception ex) {
                logger.error("list to byte error", ex);
                return false;
            }
        }
        s3.putObject(putObjectRequest, requestBody);
        return true;
    }
}
