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
    	String lower = message.trim().toLowerCase();
    	if (conversationState.getLastTaskTitle() != null) {

    	    message = message.replace(" it ", " " + conversationState.getLastTaskTitle() + " ");
    	    message = message.replace(" it.", " " + conversationState.getLastTaskTitle() + ".");
    	    message = message.replace(" it?", " " + conversationState.getLastTaskTitle() + "?");
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

        // Save last AI response
        conversationState.setLastResponse(content);

        // Remember task names for follow-up conversations
        String lowerResponse = content.toLowerCase();

        if (lowerResponse.contains("task")) {

            if (lower.contains("create")) {
                conversationState.setLastTaskTitle(message);
            }

            if (lower.contains("finish")
                    || lower.contains("completed")
                    || lower.contains("delete")
                    || lower.contains("rename")
                    || lower.contains("priority")) {

                conversationState.setLastTaskTitle(message);
            }
        }

        return content;
    }
}