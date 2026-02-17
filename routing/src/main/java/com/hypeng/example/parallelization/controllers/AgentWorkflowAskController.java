package com.hypeng.example.parallelization.controllers;


import com.hypeng.example.parallelization.dto.Answer;
import com.hypeng.example.parallelization.dto.Question;
import com.hypeng.example.parallelization.services.Router;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentWorkflowAskController {

    private final Router router;

    public AgentWorkflowAskController(Router router) {
        this.router = router;
    }

    @PostMapping("/ask")
    public Answer ask(@RequestBody Question question) {
        var response = router.act(question.question());
        return new Answer(response);
    }

}