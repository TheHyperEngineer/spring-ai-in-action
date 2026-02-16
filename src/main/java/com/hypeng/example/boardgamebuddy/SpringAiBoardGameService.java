package com.hypeng.example.boardgamebuddy;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.text.Normalizer;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;
import static org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor.FILTER_EXPRESSION;

@Service
public class SpringAiBoardGameService implements BoardGameService {

  private final ChatClient chatClient;

  public SpringAiBoardGameService(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @Value("classpath:/promptTemplates/systemPromptTemplate.st")
  private Resource promptTemplate;

  @Override
  public Answer askQuestion(Question question, String conversationId) {
    var gameNameMatch = String.format(
            "gameTitle == '%s'",
            normalizeGameTitle(question.gameTitle()));

    return chatClient.prompt()
        .user(question.question())
        .system(systemSpec -> systemSpec
            .text(promptTemplate)
            .param("gameTitle", question.gameTitle()))
        .advisors(advisorSpec -> advisorSpec
            .param(FILTER_EXPRESSION, gameNameMatch)
            .param(CONVERSATION_ID, conversationId))
        .call()
        .entity(Answer.class);
  }

  private String normalizeGameTitle(String rawTitle) {
    if (rawTitle == null) {
      return "";
    }

    String title = rawTitle.trim();
    if (title.isEmpty()) {
      return "";
    }

    // If the input looks like a filename, remove its extension
    title = removeFileExtension(title);

    // Normalize unicode and remove diacritics
    String normalized = Normalizer.normalize(title, Normalizer.Form.NFKD)
            .replaceAll("\\p{M}", ""); // remove combining marks

    // Lowercase
    normalized = normalized.toLowerCase();

    // Replace any sequence of non-alphanumeric characters with a single hyphen
    normalized = normalized.replaceAll("[^a-z0-9]+", "-");

    // Trim leading/trailing hyphens
    normalized = normalized.replaceAll("^-+|-+$", "");

    return normalized;
  }

  private  String removeFileExtension(String filename) {
    if (filename == null || filename.isBlank()) {
      return "";
    }
    // Keep only the file name portion if a path was provided
    String name = filename.replace('\\', '/');
    int lastSlash = name.lastIndexOf('/');
    if (lastSlash >= 0) {
      name = name.substring(lastSlash + 1);
    }

    int lastDot = name.lastIndexOf('.');
    if (lastDot <= 0) { // dot at index 0 is a hidden file on Unix, keep it
      return name;
    }
    return name.substring(0, lastDot);
  }

}