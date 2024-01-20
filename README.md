# 🔑 AWS STS Authentication Proxy

This API acts as an AWS STS (Security Token Service) Authentication Proxy, providing a simple and secure way for you to provision temporary, limited-privilege AWS credentials for third-parties who need access to your AWS resource(s).


## 🌟 Use Case
Ideal for scenarios where a third-party vendor without an AWS account needs access to your AWS resources, ensuring secure and controlled access.


## 📋 Pre-requisites
- AWS account
- An IAM user with `sts:AssumeRole` permissions.
- An IAM role with the necessary permissions for the resources you want to grant access to (e.g., read-only access to an S3 bucket).

## ⚡ Quick Start
### 1. Set environment variables
```bash
ROLE_ARN=<IAM role arn> # arn:aws:iam::123456789012:role/S3Access
AWS_ACCESS_KEY_ID=<access key id of user who has sts:AssumeRole permission>
AWS_SECRET_ACCESS_KEY=<secret access key of user who has sts:AssumeRole permission>
```

> [!NOTE]
> The `AWS_` prefixed variables are not needed if running on an EC2 instance with the required IAM role.


### 2. Run the application
```bash
./gradlew bootRun
```

### 3. Test the API
Use Swagger UI to test the API at http://localhost:8080/swagger-ui/index.html.


## 🚀 API usage

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



## 🔒 Running in Production

### Use a TLS/SSL Certificate
Always use HTTPS with TLS/SSL certificates to secure data in transit. This protects sensitive data, such as authentication credentials and session tokens, from being intercepted.

### Secure Application Properties
Avoid storing sensitive information directly in `application.properties`.

### Role and Access Management
Apply the principle of least privilege for the AWS IAM roles. Ensure that the IAM role assumed by your application has only the necessary permissions and nothing more.

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.