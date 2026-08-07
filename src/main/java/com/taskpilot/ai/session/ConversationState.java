package com.taskpilot.ai.session;

import org.springframework.stereotype.Component;

@Component
public class ConversationState {

    private PendingAction action = PendingAction.NONE;

    private String pendingTitle;

    private String lastTaskTitle;

    private String lastResponse;

    public PendingAction getAction() {
        return action;
    }

    public void setAction(PendingAction action) {
        this.action = action;
    }

    public String getPendingTitle() {
        return pendingTitle;
    }

    public void setPendingTitle(String pendingTitle) {
        this.pendingTitle = pendingTitle;
    }

    public String getLastTaskTitle() {
        return lastTaskTitle;
    }

    public void setLastTaskTitle(String lastTaskTitle) {
        this.lastTaskTitle = lastTaskTitle;
    }

    public String getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(String lastResponse) {
        this.lastResponse = lastResponse;
    }

    public void clear() {
        action = PendingAction.NONE;
        pendingTitle = null;
        lastTaskTitle = null;
        lastResponse = null;
    }
}