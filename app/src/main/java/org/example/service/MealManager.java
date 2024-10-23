package org.example.service;

import org.example.database.MealDao;
import org.example.database.MealDaoPostgresImpl;
import org.example.model.Meal;
import org.example.model.MealFactory;
import org.example.model.SimpleMealFactory;
import org.example.ui.UserInterface;
import org.example.utils.DayOfWeek;
import org.example.utils.MealUtils;

import java.sql.Connection;
import java.util.ArrayList;
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
        startApp();
    }

    @SuppressWarnings("InfiniteLoopStatement") // exit() method handles the InfiniteLoopStatement
    private void startApp() {
        String action;

        do {
            action = userInterface.getMenuChoice();

            switch (action) {
                case "ADD" -> addMeal();
                case "SHOW" -> listMealsByCategory();
                case "PLAN" -> createPlan();
                case "LIST PLAN" -> listPlan();
                case "EXIT" -> exit();
            }
        } while (true);
    }

    private void addMeal() {
        List<String> mealData = getMealDataFromUser();
        Meal mealToAdd = createMealFromData(mealData);
        mealDaoPostgres.addMeal(mealToAdd);
        System.out.println("The meal has been added!");
    }

    private List<String> getMealDataFromUser() {
        return userInterface.getMealDataFromUser();
    }

    private Meal createMealFromData(List<String> mealData) {
        String mealCategory = mealData.get(0);
        String mealName = mealData.get(1);
        String mealIngredients = mealData.get(2);
        return simpleMealFactory.createMeal(mealCategory, mealName, mealIngredients);
    }

    private void listMealsByCategory() {
        String category = userInterface.selectMealCategoryToShow();
        List<List<String>> mealsDataList = mealDaoPostgres.loadMeals(category);
        List<Meal> mealsObjList = constructMeals(mealsDataList);
        System.out.println();
        mealsObjList.forEach(System.out::println);
    }

    private void createPlan() {
        mealDaoPostgres.clearOldPlan();

        Map<Integer, String> breakfastMap = loadMealsForCategory("breakfast");
        Map<Integer, String> lunchMap = loadMealsForCategory("lunch");
        Map<Integer, String> dinnerMap = loadMealsForCategory("dinner");

        planForWeek(breakfastMap, lunchMap, dinnerMap);

        listPlan();
    }

    private Map<Integer, String> loadMealsForCategory(String category) {
        List<List<String>> dbMeals = mealDaoPostgres.loadMeals(category);
        return MealUtils.getMealNamesAndIds(dbMeals);
    }

    private void planForWeek(Map<Integer, String> breakfastMap, Map<Integer, String> lunchMap, Map<Integer, String> dinnerMap) {

        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println(day.getDisplayName());
            saveMealForDay(breakfastMap, "breakfast", day);
            saveMealForDay(lunchMap, "lunch", day);
            saveMealForDay(dinnerMap, "dinner", day);
            System.out.println("Yeah! We planned the meals for " + day.getDisplayName() + ".\n");
        }
    }

    private void saveMealForDay(Map<Integer, String> mealMap, String category, DayOfWeek day) {
        Map.Entry<Integer, String> selectedMeal = chooseMealForPlan(mealMap, category, day.getDisplayName());
        mealDaoPostgres.saveMealPlan(selectedMeal, category, day.getDisplayName());
    }

    private Map.Entry<Integer, String> chooseMealForPlan(Map<Integer, String> meals, String category, String day) {
        String userChoice;
        Map.Entry<Integer, String> selectedMeal = null;
        listMealsByName(meals);
        System.out.println("Choose the " + category + " for " + day + " from the list above: ");

        do {
            userChoice = userInterface.pickMealForPlan();
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

        return selectedMeal;
    }


    private List<Meal> constructMeals(List<List<String>> mealsData) {
        List<Meal> meals = new ArrayList<>();

        for (List<String> mealEntry : mealsData) {
            String mealCategory = mealEntry.get(0);
            String mealName = mealEntry.get(1);
            String mealIngredients = mealEntry.get(2);
            Meal meal = simpleMealFactory.createMeal(mealCategory, mealName, mealIngredients);
            meals.add(meal);
        }

        return meals;
    }

    private void listMealsByName(Map<Integer, String> meals) {
        for (String mealName : meals.values()) {
            System.out.println(mealName);
        }
    }

    private void listPlan() {
        for (DayOfWeek day : DayOfWeek.values()) {
            Map<String, String> planForDay = mealDaoPostgres.loadMealPlanByDays(day.getDisplayName());
            if (planForDay.isEmpty()) {
                System.out.println("Database does not contain any meal plans!");
                return;
            }
            listPlanForDay(planForDay, day.getDisplayName());
        }
    }

    private void listPlanForDay(Map<String, String> planForDay, String dayName) {
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
