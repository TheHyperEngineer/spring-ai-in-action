package com.hypeng.cyberjar.controller;

import com.embabel.agent.api.common.autonomy.Autonomy;
import com.embabel.agent.api.common.autonomy.ProcessExecutionException;
import com.embabel.agent.core.ProcessOptions;
import com.hypeng.cyberjar.story.StoryRequest;
import com.hypeng.cyberjar.story.StoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StoryController {

    @Autowired
    private Autonomy autonomy;

    @PostMapping("/story")
    public ResponseEntity<StoryResponse> generateStory(
            @RequestBody StoryRequest request) throws ProcessExecutionException {
        // Optional: customize verbosity, budget, planner, etc.
        ProcessOptions options = ProcessOptions.DEFAULT;
        /*builder()
                .withVerbosity(ProcessOptions.Verbosity.builder()
                        .withShowPrompts(false)     // keep clean output
                        .withShowLlmResponses(false)
                        .withDebug(false)
                        .withShowPlanning(false)
                        .build())
                .withBudget(ProcessOptions.Budget.builder()
                        .withCost(2.0)
                        .withActions(50)
                        .withTokens(100_000)
                        .build())
                .withPlannerType(ProcessOptions.PlannerType.GOAP)
                .build();

                ProcessOptions options = ProcessOptions.builder()
        .withVerbosity(new Verbosity(false, false, false, false))
        .withBudget(new Budget(2.0, 50, 100_000))
        .withPlannerType(GOAP)
        .build();
        */

        // Execute agent with raw query string + options
        Object result = autonomy.chooseAndRunAgent(request.getQuery(), options);

        if (result instanceof StoryResponse storyResponse) {
            return ResponseEntity.ok(storyResponse);
        } else {
            // Fallback in case agent returns unexpected type or fails
            return ResponseEntity.badRequest().body(new StoryResponse("Failed to generate story"));
        }
    }
}