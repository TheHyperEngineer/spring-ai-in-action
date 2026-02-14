package com.hypeng.example.boardgamebuddy.service.impl;

import com.hypeng.example.boardgamebuddy.dto.Question;
import com.hypeng.example.boardgamebuddy.service.BoardGameService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SpringAiBoardGameService implements BoardGameService {

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
    public Flux<String> askQuestion(Question question) {
        var gameRules = gameRulesService.getRulesFor(question.gameTitle());

        return chatClient.prompt()
                .system(userSpec -> userSpec
                        .text(promptTemplate)
                        .param("gameTitle", question.gameTitle())
                        .param("rules", gameRules))
                .user(question.question())
                .stream()
                .content();
    }
}
