package com.stsproxy.api.data;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
public class StsCredentials {
    private String accessKeyId;
    private String secretAccessKey;
    private String sessionToken;
    private ZonedDateTime expiresOn;
}
