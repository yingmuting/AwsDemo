#!/bin/bash
aws --region us-west-2 lambda invoke --function-name s3-listobjects --payload '{ "region": "us-west-2","bucket": "mmt-bucket-02","output": "lambda_object_list.txt" }' response.json