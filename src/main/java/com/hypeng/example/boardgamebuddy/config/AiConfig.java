package com.hypeng.example.boardgamebuddy.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory
                .builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(50)
                .build();
    }

    @Bean
    ChatClient chatClient(
            ChatClient.Builder chatClientBuilder,
            VectorStore vectorStore,
            ChatMemory chatMemory) {
        return chatClientBuilder
                .defaultAdvisors(
                        PromptChatMemoryAdvisor
                                .builder(chatMemory)
                                .build(),
                        QuestionAnswerAdvisor
                                .builder(vectorStore)
                                .searchRequest(SearchRequest.builder().build())
                                .build())
                .build();
    }
}