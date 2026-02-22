package com.hypeng.cyberjar.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;
import com.hypeng.cyberjar.tool.WeatherTool;
import com.hypeng.cyberjar.trip.TripDetails;
import com.hypeng.cyberjar.trip.TripItinerary;
import com.hypeng.cyberjar.trip.TripPlanResponse;
import org.springframework.beans.factory.annotation.Autowired;

@Agent(
        name = "TripPlannerAgent",
        description = "Analyzes a user's trip request and builds an itinerary."
)
public class TripPlannerAgent {

    @Autowired
    private WeatherTool weatherTool;

    @Action(description = "Extract destination and duration")
    public TripDetails extractTripDetails(UserInput input, OperationContext context) {
        return context.ai().withLlm("gpt5Nano").createObject(
                "Extract destination and duration (days) from: " + input.getContent(),
                TripDetails.class);
    }

    @Action(description = "Plan the itinerary using tools")
    public TripItinerary planItinerary(TripDetails details, OperationContext context) {
        return context.ai()
                .withLlm("gpt5Nano")
                // Registring the tool object allows the LLM to see @LlmTool methods
                .withToolObject(weatherTool)
                .createObject(
                        """
                        Plan a %d day trip to %s. 
                        
                        CRITICAL INSTRUCTIONS:
                        1. You MUST call the 'weatherFunction' tool to check the weather.
                        2. Adjust the daily activities based on the weather response received.
                        3. Provide a day-by-day detailed breakdown.
                        """.formatted(details.getDurationDays(), details.getDestination()),
                        TripItinerary.class);
    }

    @AchievesGoal(description = "Finalize the response")
    @Action(description = "Build final response")
    public TripPlanResponse buildResponse(TripItinerary itinerary) {
        return new TripPlanResponse(itinerary.getDetailedPlan());
    }
}