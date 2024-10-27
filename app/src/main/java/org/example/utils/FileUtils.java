package org.example.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class FileUtils {

    public static void writeShoppingListToFile(String fileName, Map<String, Integer> shoppingList) {

        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }

        Path filePath = Paths.get(fileName);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.toFile()))) {

            for (Map.Entry<String, Integer> entry : shoppingList.entrySet()) {
                String ingredient = entry.getKey();
                int ingredientCount = entry.getValue();

                if (ingredientCount > 1) {
                    writer.printf("%s x%d%n", ingredient, ingredientCount);
                } else {
                    writer.println(ingredient);
                }
            }

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

}
