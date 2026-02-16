package com.hypeng.example.boardgamebuddy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.Normalizer;
import java.util.List;

import static com.hypeng.example.boardgamebuddy.utils.UtilityService.normalizeGameTitle;
import static com.hypeng.example.boardgamebuddy.utils.UtilityService.removeFileExtension;

@RestController
public class DocumentLoadController {
    private final static Logger log = LoggerFactory.getLogger(DocumentLoadController.class);

    private final VectorStore vectorStore;

    public DocumentLoadController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Operation(
            summary = "Upload game rules file",
            description = "Upload a text file containing game rules. The file will be read, split, embedded and stored."
    )
    @PostMapping(path = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<String> handleFileUpload(
            @Parameter(
                    description = "File to upload",
                    required = true,
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestPart("file") MultipartFile file,

            @Parameter(description = "Optional game title to use instead of filename")
            @RequestPart(value = "gameTitle", required = false) String gameTitle) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // 1. Convert MultipartFile to a Resource compatible with TextReader
            Resource resource = file.getResource();
            DocumentReader documentReader = new TextReader(resource);

            // 2. Load the document text
            List<Document> documents = documentReader.get();

            // 3. Split into chunks
//            TextSplitter textSplitter = TokenTextSplitter.builder().build();
            TextSplitter textSplitter = TokenTextSplitter.builder()
                    .withChunkSize(500)
                    .withKeepSeparator(false)
                    .withMinChunkLengthToEmbed(10)
                    .build();
            List<Document> splitDocuments = textSplitter.apply(documents);

            // 4. Determine and Normalize Game Title
            String finalGameTitle = (gameTitle != null && !gameTitle.isBlank())
                    ? gameTitle
                    : removeFileExtension(file.getOriginalFilename());

            String normalizedKey = normalizeGameTitle(finalGameTitle);

            // 5. Inject Metadata
            log.info("Processing file: {} with key: {}", file.getOriginalFilename(), normalizedKey);
            for (Document doc : splitDocuments) {
                doc.getMetadata().put("gameTitle", normalizedKey);
            }

            // 6. Add to Vector Store (Generates Embeddings)
            vectorStore.add(splitDocuments);

            return ResponseEntity.ok("Successfully loaded rules for: " + finalGameTitle);

        } catch (Exception e) {
            log.error("Error processing file upload", e);
            return ResponseEntity.internalServerError().body("Error processing file: " + e.getMessage());
        }
    }
}