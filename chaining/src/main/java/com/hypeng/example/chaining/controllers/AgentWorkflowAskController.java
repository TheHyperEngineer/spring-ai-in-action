package com.hypeng.example.chaining.controllers;

import com.hypeng.example.chaining.dto.Answer;
import com.hypeng.example.chaining.dto.Question;
import com.hypeng.example.chaining.tasks.Chain;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentWorkflowAskController {

    private final Chain chain;

    public AgentWorkflowAskController(Chain chain) {
        this.chain = chain;
    }

    @PostMapping("/ask")
    public Answer ask(@RequestBody Question question) {
        var response = chain.act(question.question());
        return new Answer(response);
    }

}