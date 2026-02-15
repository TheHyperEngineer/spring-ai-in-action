package com.hypeng.example.boardgamebuddy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.InputStream;

@Configuration
public class VectorStoreConfig {
    private static final Logger log =
            LoggerFactory.getLogger(VectorStoreConfig.class);

    @Value("${spring.ai.vectorstore.simple.store.path:vectorstore.json}")
    private String vectorStorePath;

//    private SimpleVectorStore simpleVectorStore;

    @Bean
    public VectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        log.info("Creating SimpleVectorStore bean...");
        File vectorStoreFile = new File(vectorStorePath);
        if (!vectorStoreFile.exists()) {
            try (InputStream in = getClass().getResourceAsStream("/vectorstore.json")) {
                if (in == null) {
                    log.warn("Default vector store file not found in classpath: /vectorstore.json");
                } else {
                    java.nio.file.Files.copy(in, vectorStoreFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    log.info("Default vector store file copied to: {}", vectorStoreFile.getAbsolutePath());
                }
            } catch (Exception e) {
                log.error("Failed to copy default vector store file: {}", e.getMessage(), e);
            }
        }
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
//        simpleVectorStore = vectorStore;

        if (vectorStoreFile.exists()) {
            log.info("Loading existing vector store from file: {}", vectorStorePath);
            try {
                vectorStore.load(vectorStoreFile);
                log.info("Vector store loaded successfully.");
            } catch (Exception e) {
                log.error("Failed to load vector store from file: {}", e.getMessage(), e);
            }
        } else {
            log.info("No existing vector store found at path: {}. A new one will be created.", vectorStorePath);
        }
        return vectorStore;
    }
}
