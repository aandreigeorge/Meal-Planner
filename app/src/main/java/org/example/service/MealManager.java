package org.example.service;

import org.example.database.MealDao;
import org.example.database.MealDaoPostgresImpl;
import org.example.model.Meal;
import org.example.model.MealFactory;
import org.example.model.SimpleMealFactory;
import org.example.ui.UserInterface;
import org.example.utils.DayOfWeek;
import org.example.utils.FileUtils;
import org.example.utils.MealUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MealManager {

    private final MealDao mealDaoPostgres;
    private final UserInterface userInterface;
    private final MealFactory simpleMealFactory;

    public MealManager(Connection dbConnection) {
        this.simpleMealFactory = new SimpleMealFactory();
        this.userInterface = new UserInterface();
        this.mealDaoPostgres = new MealDaoPostgresImpl(dbConnection);
        runApplication();
    }


    @SuppressWarnings("InfiniteLoopStatement") // exit() method handles the InfiniteLoopStatement
    private void runApplication() {
        String action;

        do {
            action = userInterface.getMenuChoice();

            switch (action) {
                case "ADD" -> addMeal();
                case "SHOW" -> displayMealsByCategory();
                case "PLAN" -> createMealPlan();
                case "LIST PLAN" -> displayPlanForWeek();
                case "SAVE" -> saveShoppingListToFile();
                case "EXIT" -> exit();
            }
        } while (true);
    }

    private void addMeal() {
        List<String> mealData = getMealDataFromUser();
        Meal mealToAdd = createMealObjectFromData(mealData);
        mealDaoPostgres.addMeal(mealToAdd);
        System.out.println("The meal has been added!");
    }

    private List<String> getMealDataFromUser() {
        return userInterface.getMealDataFromUser();
    }

    private void createMealPlan() {
        mealDaoPostgres.clearOldMealPlan();
        Map<Integer, String> breakfastOptions, lunchOptions, dinnerOptions;

        breakfastOptions = extractMealNamesAndIds(loadMealsByCategory("breakfast", "ASC"));
        lunchOptions = extractMealNamesAndIds(loadMealsByCategory("lunch", "ASC"));
        dinnerOptions = extractMealNamesAndIds(loadMealsByCategory("dinner", "ASC"));

        generateWeeklyMealPlan(breakfastOptions, lunchOptions, dinnerOptions);

        displayPlanForWeek();
    }

    private void generateWeeklyMealPlan(Map<Integer, String> breakfastMap, Map<Integer, String> lunchMap, Map<Integer, String> dinnerMap) {
        for (DayOfWeek day : DayOfWeek.values()) {
            String dayName = day.getDisplayName();
            System.out.println(day.getDisplayName());

            chooseMealForPlan(breakfastMap, "breakfast", dayName);
            chooseMealForPlan(lunchMap, "lunch", dayName);
            chooseMealForPlan(dinnerMap, "dinner", dayName);

            System.out.println("Yeah! We planned the meals for " + dayName + ".\n");
        }
    }

    private void chooseMealForPlan(Map<Integer, String> meals, String category, String day) {
        String userChoice;
        Map.Entry<Integer, String> selectedMeal = null;
        displayMealsByName(meals);
        System.out.println("Choose the " + category + " for " + day + " from the list above: ");

        do {
            userChoice = userInterface.getMealChoiceForPlan();
            for (Map.Entry<Integer, String> entry : meals.entrySet()) {
                if (entry.getValue().equals(userChoice)) {
                    selectedMeal = entry;
                    break;
                }
            }
            if (selectedMeal == null) {
                System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            }
        } while (selectedMeal == null);

        saveSelectedMeal(selectedMeal, category, day);
    }

    private void saveSelectedMeal(Map.Entry<Integer, String> selectedMeal, String category, String day) {
        mealDaoPostgres.saveMealToPlan(selectedMeal, category, day);
    }

    private void saveShoppingListToFile() {
        Map<String, Integer> shoppingList = loadShoppingList();

        if (shoppingList.isEmpty()) {
            System.out.println("Unable to save. Plan your meals first.");
        } else {
            System.out.println("Input a filename:");
            String fileName = userInterface.getFileName();
            FileUtils.writeShoppingListToFile(fileName, shoppingList);
            System.out.println("Saved!");
        }
    }

    private Map<String, Integer> loadShoppingList() {
        List<String> mealIngredients;
        Map<String, Integer> totalIngredientsList = new LinkedHashMap<>();
        Map<Integer, Integer> mealOrderCounts = mealDaoPostgres.getMealCountsFromPlan();

        for (Map.Entry<Integer, Integer> mealEntry : mealOrderCounts.entrySet()) {
            int mealId = mealEntry.getKey();
            int mealOrderCount = mealEntry.getValue();
            mealIngredients = mealDaoPostgres.loadMealIngredients(mealId);

            for (String ingredient : mealIngredients) {
                totalIngredientsList.put(ingredient, totalIngredientsList.getOrDefault(ingredient, 0) + mealOrderCount);
            }
        }
        return totalIngredientsList;
    }

    private List<Meal> convertToMealObjects(List<List<String>> mealsData) {
        List<Meal> meals = new ArrayList<>();
        for (List<String> mealEntry : mealsData) {
            Meal meal = createMealObjectFromData(mealEntry);
            meals.add(meal);
        }
        return meals;
    }

    private Meal createMealObjectFromData(List<String> mealData) {
        String mealCategory = mealData.get(0);
        String mealName = mealData.get(1);
        String mealIngredients = mealData.get(2);
        return simpleMealFactory.createMeal(mealCategory, mealName, mealIngredients);
    }

    private List<List<String>> loadMealsByCategory(String category, String order) {
        return mealDaoPostgres.loadMeals(category, order);
    }

    private Map<String, String> loadMealPlanByDay(String day) {
        return mealDaoPostgres.loadMealPlanByDays(day);
    }

    private Map<Integer, String> extractMealNamesAndIds(List<List<String>> mealsDataList) {
        return MealUtils.extractMealNamesAndIds(mealsDataList);
    }

    private void displayMealsByName(Map<Integer, String> meals) {
        for (String mealName : meals.values()) {
            System.out.println(mealName);
        }
    }

    private void displayMealsByCategory() {
        String category = userInterface.selectCategoryOfMealsToShow();
        List<List<String>> mealsDataList = loadMealsByCategory(category, null);
        List<Meal> mealsObjList = convertToMealObjects(mealsDataList);

        if (mealsObjList.isEmpty()) {
            System.out.println("No meals found.");
        } else {
            System.out.println("Category: " + category + "\n");
            mealsObjList.forEach(System.out::println);
        }
    }

    private void displayPlanForWeek() {
        for (DayOfWeek day : DayOfWeek.values()) {
            Map<String, String> planForDay = loadMealPlanByDay(day.getDisplayName());
            if (planForDay.isEmpty()) {
                System.out.println("Database does not contain any meal plans!");
                return;
            }
            displayPlanForDay(planForDay, day.getDisplayName());
        }
    }

    private void displayPlanForDay(Map<String, String> planForDay, String dayName) {
        System.out.println(dayName);
        for (Map.Entry<String, String> entry : planForDay.entrySet()) {
            System.out.printf("%s: %s%n", entry.getKey(), entry.getValue());
        }
        System.out.println();
    }

    private void exit() {
        System.out.println("Bye!");
        userInterface.closeScanner();
        System.exit(0);
    }
}
