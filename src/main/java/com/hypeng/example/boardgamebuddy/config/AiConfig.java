package com.hypeng.example.boardgamebuddy.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

    @Bean
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
               /* .queryTransformers(List.of(
                        RewriteQueryTransformer.builder()
                                // Use the clean builder here!
                                .chatClientBuilder(cleanBuilder)
                                .build(),
                        TranslationQueryTransformer.builder()
                                // Use the clean builder here too!
                                .chatClientBuilder(cleanBuilder)
                                .targetLanguage("English")
                                .build()
                ))*/
                .build();

        // 2. This is the "Primary" client used by your Service
        return chatClientBuilder
                .defaultAdvisors(advisor)
                .build();
    }
}