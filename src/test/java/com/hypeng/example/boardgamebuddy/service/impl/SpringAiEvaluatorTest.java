package com.hypeng.example.boardgamebuddy.service.impl;

import com.hypeng.example.boardgamebuddy.dto.Answer;
import com.hypeng.example.boardgamebuddy.dto.Question;
import com.hypeng.example.boardgamebuddy.service.BoardGameService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class SpringAiEvaluatorTest {

    @Autowired
    private BoardGameService boardGameService;

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    private RelevancyEvaluator relevancyEvaluator;
    private FactCheckingEvaluator factCheckingEvaluator;

    @BeforeEach
    public void setup() throws IOException {
        this.relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
        this.factCheckingEvaluator = FactCheckingEvaluator.builder(chatClientBuilder).build();
    }

    @Test
    public void evaluateRelevancy() {
        String userText = "What is the capital of France?";
        Question question = new Question(userText);

        Answer answer = boardGameService.askQuestion(question);
        EvaluationRequest evaluationRequest = new EvaluationRequest(userText, answer.answer());

        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

        Assertions.assertThat(evaluationResponse.isPass())
                .withFailMessage("""
                                  ========================================
                                  The answer "%s"
                                  is not considered relevant to the question
                                  "%s".
                                  ========================================
                        """, answer.answer(), userText)
                .isTrue();
    }

    @Test
    public void evaluateFactualAccuracy() {
        var userText = "Why is the sky blue?";
        var question = new Question(userText);
        var answer = boardGameService.askQuestion(question);

        var evaluationRequest =
                new EvaluationRequest(userText, answer.answer());

        var response =
                factCheckingEvaluator.evaluate(evaluationRequest);

        Assertions.assertThat(response.isPass())
                .withFailMessage("""

          ========================================
          The answer "%s"
          is not considered correct for the question
          "%s".
          ========================================
          """, answer.answer(), userText)
                .isTrue();
    }

}