package com.hypeng.exampl.embabel;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;
import com.hypeng.exampl.embabel.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.stringtemplate.v4.ST;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

@Agent(
        name = "GameInfoAgent",
        description = "An agent that helps users answer questions " +
                "about board games, including mechanics and player counts.",
        version = "1.0.0",
        beanName = "gameInfoAgent")
public class GameInfoAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameInfoAgent.class);
    //
    // Prompt template resources
    //
    @Value("classpath:/promptTemplates/determineTitle.st")
    private Resource determineTitlePromptTemplate;

    @Value("classpath:/promptTemplates/rulesFetcher.st")
    private Resource rulesFetcherPromptTemplate;

    @Value("classpath:/promptTemplates/playerCount.st")
    private Resource playerCountPromptTemplate;

    @Value("classpath:/promptTemplates/mechanicsDeterminer.st")
    private Resource mechanicsDeterminerPromptTemplate;

    private final String rulesFilePath;

    public GameInfoAgent(@Value("${boardgame.rules.path}") String rulesFilePath) {
        this.rulesFilePath = rulesFilePath;
    }

    // ...action methods go here...

    @Action
    public GameTitle extractGameTitle(UserInput userInput, OperationContext context) {
        LOGGER.info("Extracting game title from user input");

        var prompt = promptResourceToString(determineTitlePromptTemplate,
                Map.of("userInput", userInput.getContent()));

        return context.ai()
                .withDefaultLlm()
                .createObjectIfPossible(prompt, GameTitle.class);
    }

    @Action
    public RulesFile getGameRulesFilename(GameTitle gameTitle, OperationContext context) {
        LOGGER.info("Getting game rules filename for: " + gameTitle.gameTitle());

        var prompt = promptResourceToString(rulesFetcherPromptTemplate,
                Map.of("gameTitle", gameTitle.gameTitle()));

        return context.ai().withDefaultLlm()
                .createObjectIfPossible(prompt, RulesFile.class);
    }

    @Action
    public GameRules getGameRules(GameTitle gameTitle, RulesFile rulesFile) {
        LOGGER.info("Getting game rules for: " + gameTitle.gameTitle()
                + " from file: " + rulesFile.filename());

        if (rulesFile.successful()) {
            String rulesContent =
                    new TikaDocumentReader(
                            rulesFilePath + "/" + rulesFile.filename())
                            .get()
                            .getFirst()
                            .getText();
            if (rulesContent != null) {
                return new GameRules(gameTitle.gameTitle(), rulesContent);
            }
        }

        throw new ActionFailedException(
                "Unable to fetch rules for the specified game.");
    }

    @Action
    @AchievesGoal(description = "Player count has been determined.")
    public PlayerCount determinePlayerCount(GameRules gameRules, OperationContext context) {
        LOGGER.info("Determining player count from rules for: {}",
                gameRules.gameTitle());

        var prompt = promptResourceToString(playerCountPromptTemplate,
                Map.of("gameRules", gameRules.rulesText()));

        return context.ai().withDefaultLlm().createObjectIfPossible(prompt, PlayerCount.class);
    }

    @Action
    @AchievesGoal(description = "Game mechanics have been determined.")
    public GameMechanics determineGameMechanics(GameRules gameRules, OperationContext context) {
        LOGGER.info("Determining mechanics from rules for: {}",
                gameRules.gameTitle());

        var prompt = promptResourceToString(mechanicsDeterminerPromptTemplate,
                Map.of("gameRules", gameRules.rulesText()));

        return context.ai().withDefaultLlm().createObjectIfPossible(prompt, GameMechanics.class);
    }

    //
    // Helper Methods
    //

    private String promptResourceToString(Resource resource, Map<String, String> params) {
        try {
            var promptString = resource.getContentAsString(Charset.defaultCharset());
            var stringTemplate = new ST(promptString, '{', '}');
            params.forEach(stringTemplate::add);
            return stringTemplate.render();
        } catch (IOException e) {
            LOGGER.error("Error reading prompt resource: " + resource.getFilename(), e);
            return "";
        }
    }

}
