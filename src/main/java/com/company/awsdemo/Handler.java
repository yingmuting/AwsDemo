package com.company.awsdemo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

public class Handler implements RequestHandler<Map<String, String>, String> {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("CONTEXT: " + gson.toJson(context));
        logger.log("EVENT: " + gson.toJson(event));
        boolean result = LambdaS3Manager.listObjects(event.get("region"), event.get("bucket"), event.get("output"));
//        boolean result = LambdaS3Manager.listObjects("us-west-2", "mmt-bucket-02");
        String response = gson.toJson(result);
        return response;
    }

}
