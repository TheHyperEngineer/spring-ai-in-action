package com.hypeng.example.boardgamebuddy.controller;

import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class DocumentLoadController {

    private final VectorStore vectorStore;
    @Value("classpath:/gameRules/burger_battle.txt")
    private Resource documentResource;

    public DocumentLoadController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @GetMapping("/load")
    public String loadDocument() throws Exception {
        DocumentReader documentReader = new TextReader(documentResource);
        TextSplitter textSplitter = TokenTextSplitter.builder().build();

        // In DocumentLoadController, after splitting:
        var chunks = textSplitter.apply(documentReader.get());
        String normalizedTitle = "burger_battle"; // or derive from file name

        chunks.forEach(doc -> doc.getMetadata().put("gameTitle", normalizedTitle));
        vectorStore.accept(chunks);

        // Persist to disk if using SimpleVectorStore
        if (vectorStore instanceof SimpleVectorStore simpleVectorStore) {
            simpleVectorStore.save(new File("./vectorstore.json"));
        }

        return "Document loaded and vector store persisted.";
    }
}
