package org.example.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MealUtils {

    public static boolean isValidMealCategory(String nameOrCategory) {
        return nameOrCategory.equalsIgnoreCase("BREAKFAST") ||
                nameOrCategory.equalsIgnoreCase("LUNCH") ||
                nameOrCategory.equalsIgnoreCase("DINNER");
    }

    public static boolean isValidNameOrIngredient(String inputToValidate) {
        String pattern = "[A-Za-z -]+";
        String[] nameOrIngredients = inputToValidate.split(",");

        for (String string : nameOrIngredients) {
            if (string.isEmpty() || !string.trim().matches(pattern)) {
                return false;
            }
        }
        return true;
    }

    public static Map<Integer, String> getMealNamesAndIds(List<List<String>> mealsData) {
        Map<Integer, String> mealNamesAndIds = new LinkedHashMap<>();

        for (List<String> individualMealData : mealsData) {
            int idValue = Integer.parseInt(individualMealData.get(3));
            String mealName = individualMealData.get(1);
            mealNamesAndIds.put(idValue, mealName);
        }
        return mealNamesAndIds;
    }
}
