package com.hypeng.cyberjar.controller;

import com.embabel.agent.api.common.autonomy.Autonomy;
import com.embabel.agent.api.common.autonomy.ProcessExecutionException;
import com.embabel.agent.core.ProcessOptions;
import com.embabel.agent.core.Verbosity;
import com.hypeng.cyberjar.trip.TripRequest;
import com.hypeng.cyberjar.trip.TripPlanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TripController {

    @Autowired
    private Autonomy autonomy;

    @PostMapping("/trip")
    public ResponseEntity<TripPlanResponse> planTrip(@RequestBody TripRequest request) throws ProcessExecutionException {

        ProcessOptions options = ProcessOptions.DEFAULT.withVerbosity(Verbosity.DEFAULT.showPlanning().showLlmResponses());

        Object result = autonomy.chooseAndRunAgent(request.getQuery(), options);

        // Check if it's the execution wrapper
        if (result instanceof com.embabel.agent.api.common.autonomy.AgentProcessExecution execution) {
            Object output = execution.getOutput();
            if (output instanceof TripPlanResponse tripResponse) {
                return ResponseEntity.ok(tripResponse);
            }
        }

        // Fallback
        return ResponseEntity.badRequest().body(new TripPlanResponse("Execution failed or returned wrong type"));
    }
}