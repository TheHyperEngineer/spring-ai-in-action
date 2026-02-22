package com.hypeng.cyberjar.agent;

import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;
import com.hypeng.cyberjar.document.MarkdownResult;

/*@Agent(
    name = "DocConverterAgent",
    description = "Converts web documentation into LLM-friendly Markdown using Fetch MCP."
)*/
public class DocConverterAgent {

    //    @Action(description = "Identify all sub-pages and convert them to Markdown")
    public MarkdownResult convertDocs(UserInput input, OperationContext context) {
        return context.ai()
                .withLlm("gpt5Nano")
                // Section 3.9.2: Attach the 'fetch-tools' group we defined in the Bean above
                .withToolGroup("fetch-tools")
                .createObject(
                        """
                                1. Use the 'fetch' tool to get content from: %s
                                2. Identify sub-pages mentioned in the 'Overview' or 'Sidebar'.
                                3. Recursively fetch those sub-pages.
                                4. Combine everything into a structured Markdown document.
                                """.formatted(input.getContent()),
                        MarkdownResult.class
                );
    }

    /*    @AchievesGoal(description = "Markdown conversion complete")
        @Action(description = "Finalize the output")*/
    public String buildFinalOutput(MarkdownResult result) {
        return "Conversion Complete. Total pages processed: " + result.pagesProcessed();
    }
}