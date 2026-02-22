package com.hypeng.cyberjar.config;

import com.embabel.agent.spi.LlmService;
import com.embabel.agent.spi.support.springai.SpringAiLlmService;
import com.embabel.common.ai.model.PerTokenPricingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Collections;

@Configuration
public class ModelConfig {

    private final JwtTokenProvider tokenProvider;

    public ModelConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    @Bean
    public OpenAiChatModel openAiChatModel() {

        String token = tokenProvider.getCurrentJwtToken();

        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .baseUrl("https://api.openai.com/v1")
                        .completionsPath("/chat/completions")
                        .apiKey(token)
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-5-nano")
                        .build())
                .build();
    }

    @Bean
    public LlmService<?> gpt5Nano(OpenAiChatModel chatModel) {
        return new SpringAiLlmService(
                "gpt5Nano",
                "OpenAI",
                chatModel,
                com.embabel.common.ai.model.DefaultOptionsConverter.INSTANCE,
                LocalDate.of(2024, 5, 30),
                Collections.emptyList(),
                new PerTokenPricingModel(0.05, 0.40)
        );
    }
}