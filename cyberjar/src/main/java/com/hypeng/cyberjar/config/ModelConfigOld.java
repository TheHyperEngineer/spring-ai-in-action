package com.hypeng.cyberjar.config;

import com.embabel.agent.spi.LlmService;
import com.embabel.agent.spi.support.springai.SpringAiLlmService;
import com.embabel.common.ai.model.DefaultOptionsConverter;
import com.embabel.common.ai.model.PerTokenPricingModel;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.model.SimpleApiKey;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.util.Collections;

public class ModelConfigOld {

    private final JwtTokenProvider tokenProvider;   // your existing class (whatever you named it)
    private final ObservationRegistry observationRegistry;

    public ModelConfigOld(JwtTokenProvider tokenProvider, ObservationRegistry observationRegistry) {
        this.tokenProvider = tokenProvider;
        this.observationRegistry = observationRegistry;
    }

    /**
     * Your existing working OpenAiChatModel – kept 100% intact
     * (runtime JWT + custom proxy + app_id header + custom path)
     */
    @Bean
    public OpenAiChatModel openAiChatModel() {
        String token = tokenProvider.getCurrentJwtToken();   // runs at bean creation (startup)
        HttpHeaders headers = new HttpHeaders();
        headers.add("app_id", "my-app");

        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .baseUrl("https://uat.gpt.site.firm.com/models-gateway/api/openai")
//                        .baseUrl("https://api.openai.com/v1")
                        .completionsPath("/chat/completions")
                        .apiKey(new SimpleApiKey(token))
                        .headers(headers)
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-5-nano")
                        .temperature(0.6)
                        .build())
                .build();
    }


    /**
     * Embabel wrapper – exact match to the source you shared
     */
    @Bean
    public LlmService<?> gpt5Nano(OpenAiChatModel chatModel) {
        return new SpringAiLlmService(
                "gpt-5-nano",                                      // name
                "OpenAI",                                 // provider
                chatModel,                                         // your Spring AI ChatModel
                DefaultOptionsConverter.INSTANCE,                  // optionsConverter
                LocalDate.of(2024, 5, 30),                         // knowledgeCutoffDate
                Collections.emptyList(),                           // promptContributors
                new PerTokenPricingModel(0.05, 0.40)               // pricingModel
        );
    }
}