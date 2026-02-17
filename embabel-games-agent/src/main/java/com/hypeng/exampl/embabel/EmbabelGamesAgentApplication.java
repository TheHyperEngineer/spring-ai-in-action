package com.hypeng.exampl.embabel;

//import com.embabel.agent.config.annotation.EnableAgentShell;

import com.embabel.agent.config.annotation.EnableAgents;
import com.embabel.agent.config.annotation.LoggingThemes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAgents(loggingTheme = LoggingThemes.STAR_WARS)
public class EmbabelGamesAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmbabelGamesAgentApplication.class, args);
    }

}
