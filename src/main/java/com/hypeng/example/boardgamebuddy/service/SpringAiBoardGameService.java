package com.hypeng.example.boardgamebuddy.service;

import com.hypeng.example.boardgamebuddy.dto.Answer;
import com.hypeng.example.boardgamebuddy.dto.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import static com.hypeng.example.boardgamebuddy.utils.UtilityService.normalizeGameTitle;
import static org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever.FILTER_EXPRESSION;

@Service
public class SpringAiBoardGameService implements BoardGameService {

    private final ChatClient chatClient;

    public SpringAiBoardGameService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    Resource promptTemplate;

    @Override
    public Answer askQuestion(Question question) {
        // Prepare the filter for the specific game
        var gameFilter = String.format(
                "gameTitle == '%s'",
                normalizeGameTitle(question.gameTitle()));

        // The chatClient already has the RetrievalAugmentationAdvisor (with translation)
        // as a default advisor from AiConfig.
        var aiResponse = chatClient.prompt()
                .system(systemSpec -> systemSpec
                        .text(promptTemplate)
                        .param("gameTitle", question.gameTitle()))
                .user(question.question())
                .advisors(advisorSpec ->
                        advisorSpec.param(FILTER_EXPRESSION, gameFilter))
                .call()
                .content();

        return new Answer(question.gameTitle(), aiResponse);
    }
}