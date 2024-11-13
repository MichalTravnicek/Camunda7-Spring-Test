package com.example.workflow;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.api.ApiError;
import com.example.api.iplocation.IpLocationApiClient;

import static com.example.workflow.GetWeatherByIp.IP_ADDRESS_IN_VARIABLE_NAME;
import static org.mockito.ArgumentMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

class GetWeatherByIpTest {

    @Test
    public void testOk(){
        GetWeatherByIp delegate = new GetWeatherByIp(Mockito.mock(IpLocationApiClient.class));
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Mockito.when(execution.hasVariable(IP_ADDRESS_IN_VARIABLE_NAME)).thenReturn(true);
        Mockito.when(execution.getVariable(IP_ADDRESS_IN_VARIABLE_NAME)).thenReturn("102.10.1.0");
        delegate.execute(execution);
        Mockito.verify(execution).setVariable(eq("city"), any());
    }

    @Test
    public void testMissingIp(){
        GetWeatherByIp delegate = new GetWeatherByIp(Mockito.mock(IpLocationApiClient.class));
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Mockito.when(execution.hasVariable(IP_ADDRESS_IN_VARIABLE_NAME)).thenReturn(false);
        assertThrows(BpmnError.class,()-> delegate.execute(execution));
    }

    @Test
    public void testIpNotValid(){
        GetWeatherByIp delegate = new GetWeatherByIp(Mockito.mock(IpLocationApiClient.class));
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Mockito.when(execution.hasVariable(IP_ADDRESS_IN_VARIABLE_NAME)).thenReturn(true);
        Mockito.when(execution.getVariable(IP_ADDRESS_IN_VARIABLE_NAME)).thenReturn("102.10");
        assertThrows(BpmnError.class,()-> delegate.execute(execution));
    }

    @Test
    public void testApiException(){
        final IpLocationApiClient apiClient = Mockito.mock(IpLocationApiClient.class);
        GetWeatherByIp delegate = new GetWeatherByIp(apiClient);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Mockito.when(execution.hasVariable(IP_ADDRESS_IN_VARIABLE_NAME)).thenReturn(true);
        Mockito.when(execution.getVariable(IP_ADDRESS_IN_VARIABLE_NAME)).thenReturn("102.10.1.0");
        Mockito.when(apiClient.getLocation(any())).thenThrow(new ApiError("Api fail"));
        assertThrows(BpmnError.class,()-> delegate.execute(execution));
    }

}