# Camunda 7 test with spring Boot

## Tasks
* Create Spring Boot application with integrated Camunda 7 BPMN
* Supply *ip_address* as variable on Camunda process start
* Get location from *ip_address* using https://ipstack.com/ 
* Get weather for location using https://weatherstack.com/
* Information about weather forecast will be available in the process result
* Secure the rest endpoint with JWT Bearer token


## Implementation
### Create initial Spring Boot Camunda 7 app - COMPLETED 
Cloned from Camunda Platform 7 Spring Boot Tutorial Lafayette<br> 
https://github.com/camunda-community-hub/Camunda-7-Spring-Boot-Tutorial-Lafayette<br>
Created simple GetWeatherByIp BPMN flow with one service task and JAVA delegate.
Test basic functionality using Camunda REST endpoint engine-rest 

### Create JWT authentication part - TODO

### Add the ipstack API to flow - TODO

### Add the weatherstack API to flow - TODO

### Potential cleanup refactoring - TODO

