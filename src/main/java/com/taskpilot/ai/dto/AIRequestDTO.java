package com.taskpilot.ai.dto;

public class AIRequestDTO {

    private String message;

    public AIRequestDTO() {
    }

    public AIRequestDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}