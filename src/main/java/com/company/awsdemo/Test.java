package com.company.awsdemo;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;

public class Test {
    public static void main(String[] args) {
        String key = "/";
        int index = key.lastIndexOf("/");
        System.out.println("\n The first is " + key.substring(++index));
        Region region = Region.US_WEST_2;
        CloudWatchLogsClient cloudWatchLogsClient = CloudWatchLogsClient.builder()
                .region(region)
                .build();

    }


}
