package com.hypeng.example.parallelization.configs;


import com.hypeng.example.parallelization.tasks.Chain;
import com.hypeng.example.parallelization.tasks.MechanicsDeterminerAction;
import com.hypeng.example.parallelization.tasks.PlayerCountAction;
import com.hypeng.example.parallelization.tasks.RuleFetcherAction;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AgentWorkflowConfig {

    @Bean
    ChatMemory chatMemoryAdvisor() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
    }

    @Bean
    public Chain mechanics(
            RuleFetcherAction ruleFetcher,
            MechanicsDeterminerAction mechanicsDeterminer) {
        return new Chain(List.of(ruleFetcher, mechanicsDeterminer));
    }

    @Bean
    public Chain playerCount(
            RuleFetcherAction ruleFetcher,
            PlayerCountAction playerCountTask) {
        return new Chain(List.of(ruleFetcher, playerCountTask));
    }

}