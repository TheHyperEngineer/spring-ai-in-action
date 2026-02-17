package com.hypeng.example.parallelization.configs;


import com.hypeng.example.parallelization.tasks.*;
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
    ParallelizerAction parallelAction(
            PlayerCountAction playerCount,
            MechanicsDeterminerAction mechanicsDeterminer) {
        return new ParallelizerAction(
                List.of(playerCount, mechanicsDeterminer));
    }

    @Bean
    public Chain summarizerChain(
            RuleFetcherAction ruleFetcher,
            ParallelizerAction parallelizerAction,
            SummarizerAction summarizer) {
        return new Chain(
                List.of(ruleFetcher, parallelizerAction, summarizer));
    }

}