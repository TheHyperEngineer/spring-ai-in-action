package com.hypeng.cyberjar.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;
import com.hypeng.cyberjar.story.Story;
import com.hypeng.cyberjar.story.StoryOutline;
import com.hypeng.cyberjar.story.StoryResponse;
import com.hypeng.cyberjar.story.StoryTopic;

@Agent(
        name = "StoryAgent",
        description = "Analyses user query for story topic and generates a story outline with characters, " +
                "plot and setting. Then writes a story based on the outline.")
public class StoryAgent {

    @Action(description = "Parse user input to extract story topic, characters, plot points, and setting details")
    public StoryTopic parseUserInputForStoryTopic(UserInput input, OperationContext context) {
        StoryTopic storyTopic = context.ai().withLlm("gpt5Nano").createObject(
                """
                        Extract an Story from the user's message.
                        
                        User message:
                        %s
                        """.formatted(input.getContent()), StoryTopic.class);
        System.out.println("Extracted StoryTopic: " + storyTopic.getTopic());
        return storyTopic;

    }

    @Action(description = "Generate a story outline based on the extracted topic, " +
            "characters, plot points, and setting")
    public StoryOutline generateStoryOutline(StoryTopic storyTopic, OperationContext context) {
        StoryOutline storyOutline = context.ai().withLlm("gpt5Nano").createObject(
                """
                        Create a StoryOutline for a story based on the given topic.
                        Include main characters, key plot points, and setting details in the outline.
                        
                        Topic:
                        %s
                        """.formatted(storyTopic.getTopic()), StoryOutline.class);
        System.out.println("Generated StoryOutline: " + storyOutline.getStoryOutline());
        return storyOutline;
    }

    @Action(description = "Write a complete story based on the generated outline, " +
            "fleshing out characters, plot, and setting with rich details and engaging narrative")
    public Story generateStory(StoryOutline storyOutline, OperationContext context) {

        Story story = context.ai().withLlm("gpt5Nano").createObject(
                """
                        Write a story based on the following outline.
                        Flesh out the characters, plot, and setting with rich details and engaging narrative.
                        
                        Story Outline:
                        %s
                        """.formatted(storyOutline.getStoryOutline()), Story.class);

        System.out.println("Generated Story: " + story.getStory());
        return story;
    }

    @AchievesGoal(description = "Build a StoryResponse that encapsulates the generated story, " +
            "making it ready for presentation to the user. " +
            "This may involve formatting the story or adding additional context as needed.")
    @Action(description = "")
    public StoryResponse buildStoryResponse(Story story) {
        System.out.println("Building StoryResponse from Story: " + story.getStory());
        return new StoryResponse(story.getStory());
    }
}