package com.hypeng.example.boardgamebuddy;


import com.hypeng.example.boardgamebuddy.gamedata.Game;
import com.hypeng.example.boardgamebuddy.gamedata.GameComplexity;
import com.hypeng.example.boardgamebuddy.gamedata.GameComplexityResponse;
import com.hypeng.example.boardgamebuddy.gamedata.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Description("Fetches the complexity of a game.")
public class GameTools implements Function<GameComplexityRequest, GameComplexityResponse>  {

  private final GameRepository gameRepository;

  public GameTools(GameRepository gameRepository) {  
    this.gameRepository = gameRepository;
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(GameTools.class);

  @Tool(name = "getGameComplexity",
      description = "Returns a game's complexity/difficulty " +
          "given the game's title/name.")
  public GameComplexityResponse getGameComplexity(
            @ToolParam(description="The title of the game") 
            String gameTitle) {
    var gameSlug = gameTitle    
        .toLowerCase()
        .replace(" ", "_");

    LOGGER.info("Getting complexity for {} ({})",
        gameTitle, gameSlug);

    var gameOpt = gameRepository.findBySlug(gameSlug);  

    var game = gameOpt.orElseGet(() -> {  
      LOGGER.warn("Game not found: {}", gameSlug);
      return new Game(
          null,
          gameSlug,
          gameTitle,
          GameComplexity.UNKNOWN.getValue());
    });

    return new GameComplexityResponse(  
        game.title(), game.complexityEnum());
  }

}