package com.example.workflow;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.example.workflow.GetWeatherByIp.IP_ADDRESS_IN_VARIABLE;
import static org.mockito.ArgumentMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

class GetWeatherByIpTest {

    @Test
    public void testOk(){
        GetWeatherByIp delegate = new GetWeatherByIp();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Mockito.when(execution.hasVariable(IP_ADDRESS_IN_VARIABLE)).thenReturn(true);
        Mockito.when(execution.getVariable(IP_ADDRESS_IN_VARIABLE)).thenReturn("102.10.1.0");
        delegate.execute(execution);
        Mockito.verify(execution).setVariable(eq("weather"), any());
    }

    @Test
    public void testMissingIp(){
        GetWeatherByIp delegate = new GetWeatherByIp();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Mockito.when(execution.hasVariable(IP_ADDRESS_IN_VARIABLE)).thenReturn(false);
        assertThrows(BpmnError.class,()-> delegate.execute(execution));
    }

}