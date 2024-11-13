package com.example.api.iplocation;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.api.ApiError;

@Component
public class IpLocationApiClient {

    private static final Logger log = LoggerFactory.getLogger(IpLocationApiClient.class);

    private final RestTemplate restTemplate;

    private final String ipApiKey;

    private final String ipApiUrl;

    public IpLocationApiClient(final RestTemplate restTemplate, @Value("${api.ip.key}") final String ipApiKey,
            @Value("${api.ip.url}") final String ipApiUrl) {
        this.restTemplate = restTemplate;
        this.ipApiKey = ipApiKey;
        this.ipApiUrl = ipApiUrl;
    }

    public String getLocation (String ipAddress) {
        IpLocation ipLocation;
        try {
            final ResponseEntity<IpLocation> response = restTemplate.getForEntity(ipApiUrl + "/" +ipAddress +
                    "?access_key=" + ipApiKey, IpLocation.class);
            ipLocation = response.getBody();
            Objects.requireNonNull(ipLocation);
            log.debug(ipLocation.toString());
        }catch (Exception ex){
            log.error("Error in API call:" + ex);
            throw new ApiError("API error:" + ex);
        }
        if (Objects.equals(ipLocation.getSuccess(), "false")){
            log.error("Ip location API returned error:{}", ipLocation.getError().toString());
            throw new ApiError("API error:" + ipLocation.getError().toString());
        }
        if (StringUtils.isEmpty(ipLocation.getCity())){
            log.error("City is empty");
            throw new ApiError("City is empty");
        }
        return ipLocation.getCity();
    }
}
