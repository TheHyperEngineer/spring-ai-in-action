package com.hypeng.example.boardgamebuddy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
public class DocumentLoadController {

    private static final Logger log = LoggerFactory.getLogger(DocumentLoadController.class);

    private final VectorStore vectorStore;

    // Inject the same path used in your VectorStoreConfig
    @Value("${spring.ai.vectorstore.simple.store.path:vectorstore.json}")
    private String vectorStorePath;

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

    /**
     * Uploads a text document, splits it, creates embeddings, and saves to the local VectorStore.
     *
     * @param file      The text file to upload (e.g., .txt).
     * @param gameTitle Optional. The specific game title. If blank, derives from filename.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   @RequestParam(value = "gameTitle", required = false) String gameTitle) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // 1. Convert MultipartFile to a Resource compatible with TextReader
            Resource resource = file.getResource();
            DocumentReader documentReader = new TextReader(resource);

            // 2. Load the document text
            List<Document> documents = documentReader.get();

            // 3. Split into chunks (TokenTextSplitter is good for LLM context windows)
            TextSplitter textSplitter = TokenTextSplitter.builder().build();
            List<Document> splitDocuments = textSplitter.apply(documents);

            // 4. Determine and Normalize Game Title
            // We must use the same normalization logic as the Search Service to ensure matches.
            String finalGameTitle = (gameTitle != null && !gameTitle.isBlank())
                    ? gameTitle
                    : removeFileExtension(file.getOriginalFilename());

            String normalizedKey = normalizeGameTitle(finalGameTitle);

            // 5. Inject Metadata
            log.info("Processing file: {} with key: {}", file.getOriginalFilename(), normalizedKey);
            for (Document doc : splitDocuments) {
                // "gameTitle" is the metadata key your GameRulesService filters by
                doc.getMetadata().put("gameTitle", normalizedKey);
            }

            // 6. Add to Vector Store (Generates Embeddings)
            vectorStore.accept(splitDocuments);

            // 7. Persist to Disk
            // This ensures the uploaded rules are saved to the JSON file immediately
            if (vectorStore instanceof SimpleVectorStore simpleVectorStore) {
                File storeFile = new File(vectorStorePath);
                simpleVectorStore.save(storeFile);
                log.info("Vector store persisted to: {}", storeFile.getAbsolutePath());
            }

            return ResponseEntity.ok("Successfully loaded rules for: " + finalGameTitle);

        } catch (Exception e) {
            log.error("Error processing file upload", e);
            return ResponseEntity.internalServerError().body("Error processing file: " + e.getMessage());
        }
    }

    /**
     * Helper to match the normalization logic in your GameRulesService.
     * Logic: lowercase and replace spaces with underscores.
     */
    private String normalizeGameTitle(String title) {
        if (title == null) return "unknown";
        return title.toLowerCase().trim().replace(" ", "_");
    }

    private String removeFileExtension(String filename) {
        if (filename == null) return "unknown";
        int lastDotIndex = filename.lastIndexOf('.');
        return (lastDotIndex == -1) ? filename : filename.substring(0, lastDotIndex);
    }
}