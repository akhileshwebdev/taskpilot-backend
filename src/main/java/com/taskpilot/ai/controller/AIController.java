package com.taskpilot.ai.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskpilot.ai.dto.AIRequestDTO;
import com.taskpilot.ai.dto.AIResponseDTO;
import com.taskpilot.ai.service.AIService;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<AIResponseDTO> chat(@RequestBody AIRequestDTO request) {

        System.out.println("Inside AIController");

        try {
            String response = aiService.chat(request.getMessage());
            return ResponseEntity.ok(new AIResponseDTO(response));

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new AIResponseDTO(
                            "The AI service is temporarily unavailable. Please try again in a few seconds."
                    ));
        }
    }

    @GetMapping("/test")
    public String test() {
        return "AI Controller Working";
    }
}