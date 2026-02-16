package com.hypeng.example.boardgamebuddy.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    /*@Bean
    public ChatClient chatClient(
            ChatClient.Builder chatClientBuilder,
            VectorStore vectorStore) {

        // 1. Create a "Clean" client builder for transformers
        // by creating a fresh clone that DOES NOT have the RAG advisor.
        ChatClient.Builder cleanBuilder = chatClientBuilder.clone();

        var advisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(
                        VectorStoreDocumentRetriever.builder()
                                .vectorStore(vectorStore)
                                .build())
                .queryExpander(
                        MultiQueryExpander.builder()
                                .chatClientBuilder(cleanBuilder)
                                .numberOfQueries(5)
                                .includeOriginal(false)
                                .build())
               *//* .queryTransformers(List.of(
                        RewriteQueryTransformer.builder()
                                // Use the clean builder here!
                                .chatClientBuilder(cleanBuilder)
                                .build(),
                        TranslationQueryTransformer.builder()
                                // Use the clean builder here too!
                                .chatClientBuilder(cleanBuilder)
                                .targetLanguage("English")
                                .build()
                ))*//*
                .build();

        // 2. This is the "Primary" client used by your Service
        return chatClientBuilder
                .defaultAdvisors(advisor)
                .build();
    }*/

    @Bean
    ChatClient chatClient(
            ChatClient.Builder chatClientBuilder,
            VectorStore vectorStore,
            ChatMemory chatMemory) {
        return chatClientBuilder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder().build()).build())
                .build();
    }
}