package com.hypeng.cyberjar.config;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {

    @Bean
    public McpSyncClient fetchMcpClient() {
        // Build transport with the remote SSE endpoint
        var transport = HttpClientSseClientTransport.builder("https://remote.mcpservers.org/fetch/mcp/sse")
                .build();

        // Build the synchronous client
        return McpClient.sync(transport).build();
    }

    @Bean(name = "fetch-tools")
    public ToolGroup fetchToolGroup(McpSyncClient fetchMcpClient) {
        // Bridges the Spring AI MCP client into the Embabel Agent system
        return ToolGroups.fromMcpClient(fetchMcpClient);
    }
}