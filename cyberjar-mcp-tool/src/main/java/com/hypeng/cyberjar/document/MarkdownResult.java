package com.hypeng.cyberjar.document;

/**
 * Domain object to hold the converted documentation data.
 */
public record MarkdownResult(String fullMarkdown, int pagesProcessed, String sourceUrl) {
}