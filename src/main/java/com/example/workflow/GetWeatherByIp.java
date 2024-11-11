package com.example.workflow;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

@Named
public class GetWeatherByIp implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(GetWeatherByIp.class);
    public static String IP_ADDRESS_IN_VARIABLE = "ip_address";

    @Override
    public void execute(DelegateExecution delegateExecution) {

        String activityId = delegateExecution.getActivityInstanceId();
        log.info("Invoked by:" + activityId);

        if (delegateExecution.hasVariable(IP_ADDRESS_IN_VARIABLE)){
            String ipAddress = (String) delegateExecution.getVariable(IP_ADDRESS_IN_VARIABLE);
            log.debug("Ip address:" + ipAddress);
            // TODO implementation
            delegateExecution.setVariable("weather", "TODO");
        }
        else {
            log.error("IP address variable is not present in input! Aborting.");
            throw new BpmnError("IP address missing","IP address variable is not present");
        }
    }

}

