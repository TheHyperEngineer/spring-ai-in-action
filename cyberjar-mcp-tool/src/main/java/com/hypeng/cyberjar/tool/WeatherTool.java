package com.hypeng.cyberjar.tool;

import com.embabel.agent.api.annotation.EmbabelComponent;
import com.embabel.agent.api.annotation.LlmTool;
import org.springframework.stereotype.Component;

@Component
@EmbabelComponent
public class WeatherTool {

    // The method parameters match the @LlmTool.Param annotations
    // This allows the LLM to pass strings directly during the tool loop
    @LlmTool(description = "Get the current weather forecast for a location")
    public String weatherFunction(
            @LlmTool.Param(description = "The city and country, e.g. Tokyo, Japan") String location,
            @LlmTool.Param(description = "ISO date for the forecast", required = false) String date) {

        System.out.println("🔧 REAL TOOL CALLED: Fetching weather for " + location);

        // You can add logic here to handle different locations if you want to test logic
        if (location.toLowerCase().contains("tokyo")) {
            return "The forecast for Tokyo is partly cloudy, 22°C. Perfect for outdoor walking.";
        }
        return "The weather in " + location + " is sunny and 25°C.";
    }
}