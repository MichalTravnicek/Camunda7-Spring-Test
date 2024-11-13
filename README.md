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

### Create JWT authentication part - COMPLETED
Based on https://www.javainuse.com/spring/boot-jwt
Service now requires authentication on /engine-rest endpoint

### Add the ipstack API to flow - COMPLETED
Getting "city" from IP address resolving API and inserting to flow as variable

### Add the weatherstack API to flow - TODO

### Potential cleanup refactoring - TODO

## Web interface
Camunda web management interface is exposed on http://localost:8080/<br>
Default user is "demo", password "demo" (configured in **application.yml**)


## JWT Authentication

Service requires JWT based authentication on Camunda REST endpoints "**/engine-rest/...**"<br>
Other endpoints like / endpoint for web interface are not authenticated by JWT.

### How to authenticate:
POST request to http://localhost:8080/engine-rest/authenticate
with JSON payload:<br> 
```json
{
    "username":"user",
    "password":"password"
}
```
Actual username and password are configured in **application.yml**

Successful request for authetication returns JWT token:<br>
(Default token validity expire is 5 minutes/300 seconds - **application.yml**) 

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzMxNDIwNDIyLCJleHAiOjE3MzE0MjA3MjJ9.3fXJwnS04ZWwiOfbtzulp1erzDr-JvoWerAP80zz8SkUZnIJ98iV6pEhUaN5utRBYQ8yDkhVsnXWa_bbbMHmKA"
}
```
### When calling Camunda REST endpoints, to gain access:<br>
request must contain **"Authorization"** header with string "Bearer[SPACE]" after which follows full token<br>
(in following example is token value shortened for brevity...)  

>"Bearer eyJhbGciOiJIUzUxMiJ9..."

### Examples of REST request:

Content-Type: application/json
- This request starts the Camunda process flow and must supply variable "ip_address"
> (POST) http://localhost:8080/engine-rest/process-definition/key/GetWeather-process-v1/start

```json
{
  "variables": {
    "ip_address": {
      "value": "104.16.103.112",
      "type": "String"
    }
  },
  "businessKey": "get_weather"
}
```
- This request returns history run of given process instance id with input and output variables
> (GET) http://localhost:8080/engine-rest/history/variable-instance?processInstanceId=38b3e594-9ea9-11ef-b6d9-02427698747e
(actual instanceId is returned by process start method)