package com.example.workflow;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

import com.example.api.iplocation.IpLocationApiClient;

@Named
public class GetWeatherByIp implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(GetWeatherByIp.class);
    public static String IP_ADDRESS_IN_VARIABLE_NAME = "ip_address";

    private final IpLocationApiClient apiClient;

    public GetWeatherByIp(final IpLocationApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {

        String activityId = delegateExecution.getActivityInstanceId();
        log.info("Invoked by:" + activityId);

        if (delegateExecution.hasVariable(IP_ADDRESS_IN_VARIABLE_NAME)){
            String ipAddress = (String) delegateExecution.getVariable(IP_ADDRESS_IN_VARIABLE_NAME);
            log.debug("IP address:" + ipAddress);
            if (ipAddress == null || !InetAddressValidator.getInstance().isValid(ipAddress)){
                throw new BpmnError("IP address is not valid!");
            }
            try {
                String city = apiClient.getLocation(ipAddress);
                delegateExecution.setVariable("city", city);
            }catch (Exception ex){
                throw new BpmnError("API error:" + ex);
            }
        }
        else {
            log.error("IP address variable is not present in input! Aborting.");
            throw new BpmnError("IP address missing","IP address variable is not present");
        }
    }

}

