package com.hypeng.example.parallelization.configs;


import com.hypeng.example.parallelization.tasks.Chain;
import com.hypeng.example.parallelization.tasks.MechanicsDeterminerAction;
import com.hypeng.example.parallelization.tasks.PlayerCountAction;
import com.hypeng.example.parallelization.tasks.RuleFetcherAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AgentWorkflowConfig {

    @Bean
    public Chain mechanics(
            RuleFetcherAction ruleFetcher,
            MechanicsDeterminerAction mechanicsDeterminer) {
        return new Chain(List.of(ruleFetcher, mechanicsDeterminer));
    }

    @Bean
    public Chain playerCount(
            RuleFetcherAction ruleFetcher,
            PlayerCountAction playerCountTask) {
        return new Chain(List.of(ruleFetcher, playerCountTask));
    }

}