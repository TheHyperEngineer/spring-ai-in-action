package com.hypeng.example.chaining.configs;

import com.hypeng.example.chaining.tasks.Chain;
import com.hypeng.example.chaining.tasks.MechanicsDeterminerAction;
import com.hypeng.example.chaining.tasks.RuleFetcherAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AgentWorkflowConfig {

    @Bean
    public Chain chain(
            RuleFetcherAction ruleFetcher,
            MechanicsDeterminerAction mechanicsDeterminer) {
        return new Chain(List.of(ruleFetcher, mechanicsDeterminer));
    }

}