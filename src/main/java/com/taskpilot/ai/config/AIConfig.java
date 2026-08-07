package com.taskpilot.ai.config;

import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.taskpilot.ai.tool.DuplicateTaskTool;
import com.taskpilot.ai.tool.DashboardSummaryTool;
import com.taskpilot.ai.tool.InsightsTool;
import com.taskpilot.ai.tool.PlanningTool;
import com.taskpilot.ai.tool.PriorityOptimizerTool;
import com.taskpilot.ai.tool.ProductivityTool;
import com.taskpilot.ai.tool.TaskTool;
import com.taskpilot.ai.tool.WeeklyReportTool;
import com.taskpilot.ai.tool.TaskEditTool;

@Configuration
public class AIConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(5)
                .build();
    }

    @Bean
    public ChatClient chatClient(
            ChatModel chatModel,
            TaskTool taskTool,
            PlanningTool planningTool,
            ProductivityTool productivityTool,
            ChatMemory chatMemory,
            WeeklyReportTool weeklyReportTool,
            InsightsTool insightsTool,
            PriorityOptimizerTool priorityOptimizerTool,
            TaskEditTool taskEditTool,
            DuplicateTaskTool duplicateTaskTool
    ) {

        return ChatClient.builder(chatModel)
        		.defaultTools(
        			    taskTool,
        			    dashboardSummaryTool,
        			    planningTool,
        			    productivityTool,
        			    weeklyReportTool,
        			    insightsTool,
        			    priorityOptimizerTool,
        			    taskEditTool,
        			    duplicateTaskTool
        			)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }
}