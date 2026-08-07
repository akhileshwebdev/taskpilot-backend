package com.taskpilot.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.taskpilot.ai.session.ConversationState;
import com.taskpilot.ai.session.PendingAction;

@Service
public class AIService {

    private final ChatClient chatClient;
    private final ConversationState conversationState;

    
    public AIService(ChatClient chatClient,
            ConversationState conversationState) {

		this.chatClient = chatClient;
		this.conversationState = conversationState;
		}

    public String chat(String message) {
    	message = message.trim();
    	String lower = message.toLowerCase();
    	if (conversationState.getLastTaskTitle() != null) {

    		if (conversationState.getLastTaskTitle() != null) {

    		    message = message.replaceAll("\\bit\\b",
    		            conversationState.getLastTaskTitle());
    		}
    	}

    	if (lower.equals("yes") || lower.equals("yes, create it")) {

    	    if (conversationState.getAction() == PendingAction.CREATE_TASK) {

    	        String title = conversationState.getPendingTitle();

    	        conversationState.clear();

    	        return chatClient.prompt()
    	                .user("Create task " + title)
    	                .call()
    	                .content();
    	    }
    	}
    	if (lower.equals("no") || lower.equals("cancel")) {

    	    conversationState.clear();

    	    return "👍 Okay, I won't perform that action.";
    	}


        var prompt = chatClient.prompt()
        		.system("""
        				You are TaskPilot AI.

        				You help users manage their tasks naturally.

        				Rules:

        				- Always understand the user's intent.
        				- Always use available tools for task operations.
        				- Never invent task information.
        				- Infer missing information whenever possible.
        				- If a task title is unclear, ask a follow-up question.
        				- Keep responses friendly, concise and conversational.
        				- Never explain which tool you used.
        				- If a tool returns a result, use it to generate a natural response.
        				""");


        var response = prompt.user(message).call();

        String content = response.content();


        conversationState.setLastResponse(content);

        if (content == null || content.isBlank()) {
            return "I couldn't understand that. Could you rephrase it?";
        }

        return content;
    }
}