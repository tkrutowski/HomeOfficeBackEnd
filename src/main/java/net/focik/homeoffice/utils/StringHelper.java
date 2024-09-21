package net.focik.homeoffice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {

    /**
     * Extracts the text between the first occurrence of a colon ":" and an opening parenthesis "(" in the given string.
     *
     * @param input the input string from which to extract the text
     * @return the extracted text between the colon and the opening parenthesis, or an empty string if not found
     */
    public static String extractTextBetweenColonAndParenthesis(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String regex = ":\\s*(.*?)\\s*\\(";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "";
    }


    /**
     * Extracts the first number found inside parentheses in the given string.
     *
     * @param input the input string containing parentheses
     * @return the number found inside parentheses, or an empty string if no number is found
     */
    public static String extractNumberFromParentheses(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

//        String regex = "\\(([^)]*\\d+[^)]*)\\)";
        String regex = "\\(([^)]*)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String insideParentheses = matcher.group(1);
//            Pattern numberPattern = Pattern.compile("\\d+");
            Pattern numberPattern = Pattern.compile("\\d+(\\.\\d+)?");
            Matcher numberMatcher = numberPattern.matcher(insideParentheses);
            if (numberMatcher.find()) {
                return numberMatcher.group();
            }
        }

        return "";
    }

    /**
     * Extracts the file extension from a given URL.
     *
     * This method returns the substring after the last dot (.) in the URL, provided
     * that the dot appears after the last slash (/). If there is no dot, or if the last
     * dot appears before the last slash, the method returns an empty string.
     *
     *
     * @param url the URL string from which to extract the file extension
     * @return the file extension, or an empty string if none found or if the URL is invalid
     */
    public static String extractFileExtension(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }

        int lastDotIndex = url.lastIndexOf('.');
        if (lastDotIndex == -1) {
            // No dot found, return empty string
            return "";
        }

        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex > lastDotIndex) {
            // Last dot is before the last slash, meaning it's not part of the file name
            return "";
        }

        return url.substring(lastDotIndex + 1);
    }
}