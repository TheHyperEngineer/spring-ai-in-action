package com.hypeng.cyberjar.config;

import com.embabel.agent.core.ToolGroup;
import com.embabel.agent.core.ToolGroupDescription;
import com.embabel.agent.core.ToolGroupPermission;
import com.embabel.agent.tools.mcp.McpToolGroup;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Configuration
public class McpToolConfig {

    private final List<McpSyncClient> mcpSyncClients;

    public McpToolConfig(List<McpSyncClient> mcpSyncClients) {
        this.mcpSyncClients = mcpSyncClients;
    }

    @Bean
    public ToolGroup cloudFlareMcpToolGroup() {
        //Get information about Cloudflare's MCP Demo Day. Use this tool if the user asks about Cloudflare's MCP demo day
        return new McpToolGroup(
                ToolGroupDescription.Companion.invoke(
                        "Get information about Cloudflare's MCP Demo Day. Use this tool if the user asks about Cloudflare's MCP demo day",
                        "cloudFlare-mcp-tool-group"
                ),
                "CloudFlare",
                "cloudFlare-mcp-tool-group",
                Set.of(ToolGroupPermission.INTERNET_ACCESS),
                mcpSyncClients,
                toolCallback -> {
                    String toolName = toolCallback.getToolDefinition().name();
                    return toolName.contains("mcp_demo_day_info") ||
                            toolName.contains("demo_day_info") ||
                            toolName.equals("demo_day");
                }
        );
    }
}
