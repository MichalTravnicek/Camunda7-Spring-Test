package com.example.api.iplocation;

import java.util.Map;

public class IpLocation {

    //location response
    private String city;
    //is false in case of error
    private String success;
    //in case of error contains "code","type","info"
    private Map<String,String> error;

    public String getCity(){
        return city;
    }

    public String getSuccess() {
        return success;
    }

    public Map<String, String> getError() {
        return error;
    }

    @Override
    public String toString() {
        return "IpLocation{" +
                "city='" + city + '\'' +
                ", success='" + success + '\'' +
                ", error=" + error +
                '}';
    }
}
