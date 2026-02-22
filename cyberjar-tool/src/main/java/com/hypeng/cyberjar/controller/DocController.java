package com.hypeng.cyberjar.controller;

import com.embabel.agent.api.common.autonomy.Autonomy;
import com.embabel.agent.api.common.autonomy.ProcessExecutionException;
import com.embabel.agent.core.ProcessOptions;
import com.hypeng.cyberjar.document.MarkdownResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

//@RestController
public class DocController {

    private final Autonomy autonomy;

    public DocController(Autonomy autonomy) {
        this.autonomy = autonomy;
    }

    //    @PostMapping("/convert")
    public ResponseEntity<MarkdownResult> convert(@RequestBody String url) throws ProcessExecutionException {

        // Optional: customize verbosity, budget, planner, etc.
        ProcessOptions options = ProcessOptions.DEFAULT;

        // Execute agent with raw query string + options
        Object result = autonomy.chooseAndRunAgent(url, options);
        // This triggers the GOAP planner to execute DocConverterAgent
        // Check if it's the execution wrapper
        if (result instanceof com.embabel.agent.api.common.autonomy.AgentProcessExecution execution) {
            Object output = execution.getOutput();
            if (output instanceof MarkdownResult markdownResult) {
                return ResponseEntity.ok(markdownResult);
            }
        }

        // Fallback
        return ResponseEntity.badRequest().body(new MarkdownResult("Execution failed or returned wrong type", 0, ""));
    }
}