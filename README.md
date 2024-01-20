# AWS STS Authentication Proxy

This is an API that acts as an AWS STS Authentication Proxy.

The API accepts a username and password and returns an STS token based on a preconfigured AWS IAM role.

---
## Use Case
- A third-party vendor without an AWS account needs access to one of your AWS resources.

---
## Pre-requisites
- AWS account
- An IAM user with sts:AssumeRole permissions.
- An IAM role with the resource permissions you want to grant to the third-party (e.g. read-only access to S3 bucket)

---

## Quick Start
### 1. Set environment variables
```bash
ROLE_ARN=<IAM role arn> # arn:aws:iam::123456789012:role/S3Access
AWS_ACCESS_KEY_ID=<access key id of user who has sts:AssumeRole permission>
AWS_SECRET_ACCESS_KEY=<secret access key of user who has sts:AssumeRole permission>
```

> [!NOTE]
> The variables prefixed with `AWS_` are not required if you are running the app on an EC2 instance with an IAM role that has the requisite STS assume role permissions.


### 2. Run the application
```bash
./gradlew bootRun
```

### 3. Test the API
Use [Swagger UI](http://localhost:8080/swagger-ui/index.html) to test the API using the default credentials (username: `vendor` / password: `password`).

---
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



## Running in Production

### Use a TLS/SSL Certificate
Always use HTTPS with TLS/SSL certificates to secure data in transit. This protects sensitive data, such as authentication credentials and session tokens, from being intercepted.

### Secure Application Properties
Avoid storing sensitive information like directly in `application.properties`.

### Role and Access Management
Apply the principle of least privilege for the AWS IAM roles. Ensure that the IAM role assumed by your application has only the necessary permissions and nothing more.
