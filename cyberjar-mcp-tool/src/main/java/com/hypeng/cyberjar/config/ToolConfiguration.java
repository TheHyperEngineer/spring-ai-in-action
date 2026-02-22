package com.hypeng.cyberjar.config;

import com.embabel.agent.spi.support.springai.SpringAiMcpToolFactory;
import com.embabel.agent.tools.mcp.McpToolFactory;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ToolConfiguration {

    @Bean
    public McpToolFactory mcpToolFactory(List<McpSyncClient> clients) {

        return new SpringAiMcpToolFactory(clients);
    }
}