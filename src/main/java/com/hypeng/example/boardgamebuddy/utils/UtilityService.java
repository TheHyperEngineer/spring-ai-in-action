package com.hypeng.example.boardgamebuddy.utils;

import java.text.Normalizer;

public class UtilityService {

    /**
     * Remove the file extension from a filename.
     * Examples:
     *  - "rules.txt" -> "rules"
     *  - "my.game.rules.pdf" -> "my.game.rules"
     *  - ".hiddenfile" -> ".hiddenfile"
     *  - null -> ""
     */
    public static  String removeFileExtension(String filename) {
        if (filename == null || filename.isBlank()) {
            return "";
        }
        // Keep only the file name portion if a path was provided
        String name = filename.replace('\\', '/');
        int lastSlash = name.lastIndexOf('/');
        if (lastSlash >= 0) {
            name = name.substring(lastSlash + 1);
        }

        int lastDot = name.lastIndexOf('.');
        if (lastDot <= 0) { // dot at index 0 is a hidden file on Unix, keep it
            return name;
        }
        return name.substring(0, lastDot);
    }

    /**
     * Normalize a game title into a consistent key suitable for metadata or lookup.
     * Steps:
     *  - null/blank -> empty string
     *  - strip surrounding whitespace
     *  - remove file extension if present
     *  - Unicode normalize and remove diacritics
     *  - convert to lower case
     *  - replace any sequence of non-alphanumeric characters with a single hyphen
     *  - trim leading/trailing hyphens
     *
     * Examples:
     *  - "  Catan: Seafarers  " -> "catan-seafarers"
     *  - "Monopoly (2021).pdf" -> "monopoly-2021"
     */
    public static String normalizeGameTitle(String rawTitle) {
        if (rawTitle == null) {
            return "";
        }

        String title = rawTitle.trim();
        if (title.isEmpty()) {
            return "";
        }

        // If the input looks like a filename, remove its extension
        title = removeFileExtension(title);

        // Normalize unicode and remove diacritics
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFKD)
                .replaceAll("\\p{M}", ""); // remove combining marks

        // Lowercase
        normalized = normalized.toLowerCase();

        // Replace any sequence of non-alphanumeric characters with a single hyphen
        normalized = normalized.replaceAll("[^a-z0-9]+", "-");

        // Trim leading/trailing hyphens
        normalized = normalized.replaceAll("^-+|-+$", "");

        return normalized;
    }
}
