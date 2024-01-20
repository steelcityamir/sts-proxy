# aws-sts-proxy
AWS STS Authentication Proxy

## Overview
This is an API that acts as an AWS STS Authentication Proxy.

The API accepts a username and password and returns an STS token based on a preconfigured AWS IAM role.


## Use Cases
- A third-party vendor without an AWS account needs access to one of your AWS resources.

## Pre-requisites
- AWS account
- IAM role ARN

## API usage

### POST /auth

#### Request body

```json
{
  "username": "vendor", 
  "password": "password"
}
```

### Responses

``200 OK``
```json
{
  "accessKeyId": "ASIA...",
  "secretAccessKey": "+kd...",
  "sessionToken": "IQo...",
  "expiresOn": "2024-01-20T02:28:37Z"
}
```

``400 Bad Request``
- The request body was invalid or malformed.

``401 Unauthorized``
- The credentials were incorrect.

``503 Service Unavailable``
- Authentication was successful but the AWS client encountered an error.
