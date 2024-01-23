<div align="center">
<img height="60" src="https://github.com/codebyamir/sts-proxy/assets/54147931/938fd461-02bc-487e-bb03-e1b4c2ed4bb1">
<h1 align="center">STSproxy</h1>
</div>

STSproxy is an AWS STS (Security Token Service) Authentication Proxy, providing a simple and secure way for you to provision temporary, limited-privilege AWS credentials for third-parties who need access to your AWS resource(s).


## 🌟 Use Case
Ideal for scenarios where a third-party vendor without an AWS account needs access to your AWS resources, ensuring secure and controlled access.
![Flow Diagram](https://github.com/codebyamir/sts-proxy/assets/54147931/a37cf74d-a9bd-4907-8e63-e5a9fbd86b20)



## 📋 What You Need to Get Started
- AWS account
- An IAM user with `sts:AssumeRole` permissions.
- An IAM role with the necessary permissions for the resources you want to grant access to (e.g. read-only access to an S3 bucket).

## ⚡ Quick Start

###  🐳 Running with Docker 
Coming soon

### 🛠️ Building the App from Source

#### Pre-requisites
- Java JDK 21
  
#### 1. Clone the repository
```bash
git clone https://github.com/codebyamir/sts-proxy.git
cd sts-proxy
```

#### 2. Set environment variables
```bash
export ROLE_ARN=<IAM role arn> # arn:aws:iam::123456789012:role/S3Access
export AWS_ACCESS_KEY_ID=<access key id of user who has sts:AssumeRole permission>
export AWS_SECRET_ACCESS_KEY=<secret access key of user who has sts:AssumeRole permission>
```

> [!NOTE]
> The `AWS_` prefixed variables are not needed if running on an EC2 instance with the required IAM role.


#### 3. Run the application
```bash
./gradlew bootRun
```

#### 4. Test the API
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

## ⚙️ Configuration

The `application.properties` file contains several configuration options to tailor the behavior of the application. 

These can be overridden using environment variables.

### AWS Role ARN

`aws.role.arn=${ROLE_ARN}`

- Description: This is the Amazon Resource Name (ARN) of the role that the application will assume when interacting with AWS Security Token Service (STS).
- Example: `arn:aws:iam::123456789012:role/YourRoleName`

### Session Lifetime

`aws.role.session.duration.seconds=${ROLE_SESSION_DURATION_SECONDS:900}`
- Description: Specifies the duration, in seconds, for which the credentials should remain valid.
- Default: 900 seconds (15 minutes)
- Minimum: 900 seconds (15 minutes)
- Maximum: 43200 seconds (12 hours)

> [!TIP]
> The session duration should be long enough to perform the necessary tasks but short enough to maintain security.

### Vendor Username

`vendor.username=${VENDOR_USERNAME:vendor}`
- Description: This is the username for proxy authentication.
- Default: `vendor`

### Vendor Password

`vendor.password=${VENDOR_PASSWORD:{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG}`
- Description: The bcrypt hash of the password used for proxy authentication.
- Default: The hash corresponds to the password `password`.

> [!TIP]
> It is highly recommended to change the default password. Use a bcrypt generator to create a new hash using 10 rounds.


## 🔒 Security Considerations for Production

### Use a TLS/SSL Certificate
Always use HTTPS with TLS/SSL certificates to secure data in transit. This protects sensitive data, such as authentication credentials and session tokens, from being intercepted.

### Secure Application Properties
Avoid storing sensitive information directly in `application.properties`.

### Role and Access Management
Apply the principle of least privilege for the AWS IAM roles. Ensure that the IAM role assumed by your application has only the necessary permissions and nothing more.

### Use Strong Complex Password for API Access
Enforce the following guidelines for the password:
- Minimum Length: Password should be at least 12 characters long.
- Complexity Requirements: Include a mix of uppercase and lowercase letters, numbers, and symbols.
- No Predictable Patterns: Avoid sequential characters (e.g., 1234, abcd) and repeated characters (e.g., aaaa, 1111).

### IP Whitelisting
Restrict access to authorized IP addresses for an additional layer of security.

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
