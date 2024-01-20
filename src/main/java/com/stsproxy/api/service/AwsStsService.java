package com.stsproxy.api.service;

import com.stsproxy.api.exception.AwsClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;

@Service
@Slf4j
public class AwsStsService {

    @Value("${aws.role.arn}")
    private String roleArn;

    @Value("${aws.role.session.duration.seconds}")
    private int sessionDuration;

    @Value("${aws.role.session.name}")
    private String sessionName;

    public Credentials getTemporaryCredentials() {
        try {
            StsClient stsClient = StsClient.builder().build();

            AssumeRoleRequest roleRequest = AssumeRoleRequest.builder()
                    .roleArn(roleArn)
                    .roleSessionName(sessionName)
                    .durationSeconds(sessionDuration)
                    .build();

            AssumeRoleResponse roleResponse = stsClient.assumeRole(roleRequest);
            return roleResponse.credentials();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AwsClientException("Unable to process request because the AWS client encountered an error.");
        }
    }
}

