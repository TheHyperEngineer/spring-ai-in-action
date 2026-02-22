package com.hypeng.cyberjar.controller;

import com.embabel.agent.api.common.autonomy.Autonomy;
import com.embabel.agent.api.common.autonomy.ProcessExecutionException;
import com.embabel.agent.core.ProcessOptions;
import com.hypeng.cyberjar.story.StoryRequest;
import com.hypeng.cyberjar.story.StoryResponse;
import com.hypeng.cyberjar.trip.TripPlanResponse;
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

        // Execute agent with raw query string + options
        Object result = autonomy.chooseAndRunAgent(request.getQuery(), options);

        // Check if it's the execution wrapper
        if (result instanceof com.embabel.agent.api.common.autonomy.AgentProcessExecution execution) {
            Object output = execution.getOutput();
            if (output instanceof StoryResponse storyResponse) {
                return ResponseEntity.ok(storyResponse);
            }
        }

        // Fallback
        return ResponseEntity.badRequest().body(new StoryResponse("Execution failed or returned wrong type"));
    }
}