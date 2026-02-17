package com.hypeng.exampl.embabel.config;

import com.embabel.agent.api.models.OpenAiCompatibleModelFactory;
import com.embabel.common.ai.model.Llm;
import com.embabel.common.ai.model.PerTokenPricingModel;
import io.micrometer.observation.ObservationRegistry;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class openAiModelConfig extends OpenAiCompatibleModelFactory {

    public openAiModelConfig(
            @NonNull ObservationRegistry observationRegistry,
            @NonNull @Value("${OPENAI_API_KEY}") String apiKey) {
        super(
                "https://api.openai.com/v1",
                apiKey,
                "/chat/completions",
                "/embeddings",
                observationRegistry);
    }

    Llm openAiLlm() {
        return openAiCompatibleLlm(
                "gpt-5-nano",
                new PerTokenPricingModel(0, 0),
                "OpenAI GPT-3.5 Turbo",
                LocalDate.of(2026, 2, 11)
        );
    }
}
