package com.hypeng.example.boardgamebuddy.service.impl;

import com.hypeng.example.boardgamebuddy.dto.Answer;
import com.hypeng.example.boardgamebuddy.dto.Question;
import com.hypeng.example.boardgamebuddy.service.BoardGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SpringAiBoardGameService implements BoardGameService {
    private static final Logger log =
            LoggerFactory.getLogger(SpringAiBoardGameService.class);

    private final ChatClient chatClient;
    private final GameRulesService gameRulesService;

    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    private Resource promptTemplate;

    public SpringAiBoardGameService(ChatClient.Builder chatClientBuilder,
                                    GameRulesService gameRulesService) {
        this.chatClient = chatClientBuilder.build();
        this.gameRulesService = gameRulesService;
    }

    @Override
    public Answer askQuestion(Question question) {
        var gameRules = gameRulesService.getRulesFor(question.gameTitle());

        var responseEntity = chatClient.prompt()
                .system(sysSpec -> sysSpec
                        .text(promptTemplate)
                        .param("gameTitle", question.gameTitle())
                        .param("rules", gameRules))
                .user(question.question())
                .call()
                .responseEntity(Answer.class);

        var response = responseEntity.response();
        var metadata = response.getMetadata();
        logUsage(metadata.getUsage());

        return responseEntity.entity();
    }

    private void logUsage(Usage usage) {
        log.info("Token usage: prompt={}, generation={}, total={}",
                usage.getPromptTokens(),
                usage.getCompletionTokens(),
                usage.getTotalTokens());
    }
}
