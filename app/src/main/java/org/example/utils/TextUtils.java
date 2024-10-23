package org.example.utils;

public class TextUtils {

    public static String toTitleCase(String input) {
        // Split the input into words
        String[] words = input.split("\\s+");  // Splitting by one or more spaces
        StringBuilder capitalizedString = new StringBuilder();

        // Loop through each word
        for (String word : words) {
            if (word.length() > 0) {
                // Capitalize the first letter and make the rest lowercase
                String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                capitalizedString.append(capitalizedWord).append(" ");
            }
        }

        // Trim to remove the trailing space and return the result
        return capitalizedString.toString().trim();
    }

}
