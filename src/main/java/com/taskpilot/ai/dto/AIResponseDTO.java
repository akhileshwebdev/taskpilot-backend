package com.taskpilot.ai.dto;

public class AIResponseDTO {

    private String response;

    public AIResponseDTO() {
    }

    public AIResponseDTO(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}