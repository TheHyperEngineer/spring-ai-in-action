package com.hypeng.exampl.embabel;


import com.embabel.agent.api.common.autonomy.AgentInvocation;
import com.embabel.agent.core.AgentPlatform;
import com.embabel.agent.core.ProcessOptions;
import com.hypeng.exampl.embabel.dto.Answer;
import com.hypeng.exampl.embabel.dto.Question;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentWorkflowAskController {

    private final AgentPlatform agentPlatform;

    public AgentWorkflowAskController(AgentPlatform agentPlatform) {

        this.agentPlatform = agentPlatform;
    }

    @PostMapping("/ask")
    public Answer ask(@RequestBody Question question) {
        var responseInvocation = AgentInvocation
                .builder(agentPlatform)
                .options(
                        ProcessOptions
                                .builder()
                                .verbosity(v -> {
                                    v.showPrompts(true);
                                    v.showLlmResponses(true);
                                    v.showPlanning(true);
                                    v.debug(true);
                                }).build()
                )
                .build(String.class);
        String response = responseInvocation.invoke(question);
        return new Answer(response);
    }

}