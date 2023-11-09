#!/bin/bash
aws cloudformation package --template-file template.yml --s3-bucket mmt-bucket-001 --output-template-file out.yml
## aws cloudformation deploy --template-file out.yml --stack-name s3-listobjects --capabilities CAPABILITY_NAMED_IAM