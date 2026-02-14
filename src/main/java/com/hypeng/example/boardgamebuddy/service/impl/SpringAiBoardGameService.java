package com.hypeng.example.boardgamebuddy.service.impl;

import com.hypeng.example.boardgamebuddy.dto.Answer;
import com.hypeng.example.boardgamebuddy.dto.Question;
import com.hypeng.example.boardgamebuddy.service.BoardGameService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class SpringAiBoardGameService implements BoardGameService {

    private final ChatClient chatClient;
    private final GameRulesService gameRulesService;

    @Value("classpath:/promptTemplates/questionPromptTemplate.st")
    private Resource questionPromptTemplate;

    public SpringAiBoardGameService(ChatClient.Builder chatClientBuilder,
                                    GameRulesService gameRulesService) {
        this.chatClient = chatClientBuilder.build();
        this.gameRulesService = gameRulesService;
    }

    @Override
    public Answer askQuestion(Question question) {
        var gameRules = gameRulesService.getRulesFor(question.gameTitle());

        var answerText = chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(questionPromptTemplate)
                        .param("gameTitle", question.gameTitle())
                        .param("question", question.question())
                        .param("rules", gameRules))
                .advisors(new SimpleLoggerAdvisor())
                .call()
                .content();

        return new Answer(question.gameTitle(), answerText);
    }
}
