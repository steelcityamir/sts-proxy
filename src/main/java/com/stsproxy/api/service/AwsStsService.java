package com.stsproxy.api.service;

import com.stsproxy.api.data.StsCredentials;
import com.stsproxy.api.exception.AwsClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.Credentials;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@Slf4j
public class AwsStsService {

    @Value("${aws.role.arn}")
    private String roleArn;

    @Value("${aws.role.session.duration.seconds}")
    private int sessionDuration;

    private final StsClient stsClient = StsClient.builder().build();

    public StsCredentials getTemporaryCredentials() {
        try {
            Credentials awsCredentials = assumeAwsRole();
            return convertToStsCredentials(awsCredentials);
        } catch (Exception e) {
            String errorMessage = "Unable to process request because the AWS client encountered an error.";
            log.error(errorMessage, e);
            throw new AwsClientException(errorMessage);
        }
    }

    private Credentials assumeAwsRole() {
        AssumeRoleRequest roleRequest = AssumeRoleRequest.builder()
                .roleArn(roleArn)
                .roleSessionName(getSessionName())
                .durationSeconds(sessionDuration)
                .build();
        return stsClient.assumeRole(roleRequest).credentials();
    }

    private StsCredentials convertToStsCredentials(Credentials credentials) {
        ZonedDateTime expiration = credentials.expiration().atZone(ZoneId.systemDefault());

        return StsCredentials.builder()
                .accessKeyId(credentials.accessKeyId())
                .secretAccessKey(credentials.secretAccessKey())
                .sessionToken(credentials.sessionToken())
                .expiresOn(expiration)
                .build();
    }

    private String getSessionName() {
        return "MySession";
    }
}

