package com.hypeng.cyberjar.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.annotation.ToolGroup;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Agent(
        name = "CloudFlareMcpToolAgent",
        description = "Intelligent agent that understands natural language commands and performs calls CloudFlare's Demo Day MCP Tool.",
        version = "1.0.0"
)
public class CloudFlareMcpToolAgent {

    private static final Logger log = LoggerFactory.getLogger(CloudFlareMcpToolAgent.class);

    @Action(description = "Process natural language file operation requests and call CloudFlare's Demo Day MCP Tool")
    @ToolGroup(role = "cloudFlare-mcp-tool-group")
    @AchievesGoal(description = "Understand user's natural language command and call CloudFlare's Demo Day MCP Tool")
    public String processFileOperation(UserInput input, OperationContext context) {
        log.info("[ACTION] processFileOperation START - user command: {}", input.getContent());

        String prompt = String.format("""
                Get information about Cloudflare's MCP Demo Day.
                """);

        String response = context.ai()
                .withDefaultLlm()
                .withToolGroup("cloudFlare-mcp-tool-group")
                .createObjectIfPossible(prompt, String.class);

        log.info("[ACTION] processFileOperation END - operation completed");
        return response;
    }
}