package com.fagile.Klasha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class State {
    private String name;
    @JsonProperty("state_code")
    private String stateCode;

    // Getters and setters for the fields

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}