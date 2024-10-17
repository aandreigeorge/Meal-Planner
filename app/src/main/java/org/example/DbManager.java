package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DbManager {

    private final Connection dbConnection;

    DbManager(Connection dbConnection) {
        this.dbConnection = dbConnection;
        createTables();
    }

    static Connection getDbConnection() throws SQLException {
        String DB_URL = "jdbc:postgresql://localhost:5432/meals_db";
        String USER = "postgres";
        String PASS = "1111";
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private void createTables() {
        String createMealsTable = "CREATE TABLE IF NOT EXISTS meals (" +
                "meal_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                "category VARCHAR(255), " +
                "meal VARCHAR(255)" +
                ");";

        String createIngredientsTable = "CREATE TABLE IF NOT EXISTS ingredients (" +
                "ingredient_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                "ingredient VARCHAR(255), " +
                "meal_id INTEGER, " +
                "FOREIGN KEY (meal_id) REFERENCES meals(meal_id) ON DELETE CASCADE" +
                ");";

        try (Statement statement = dbConnection.createStatement()) {
            statement.executeUpdate(createMealsTable);
            statement.executeUpdate(createIngredientsTable);
        } catch (SQLException e) {
            System.err.println("Error occurred while creating tables: " + e.getMessage());
        }
    }

    List<Meal> loadMeals() {
        List<Meal> mealsList = new ArrayList<>();
        String mealQuery = "SELECT * FROM meals;";

        try (PreparedStatement loadMeal = dbConnection.prepareStatement(mealQuery);
             ResultSet mealsResultSet = loadMeal.executeQuery()) {

            while (mealsResultSet.next()) {
                String mealCategory = mealsResultSet.getString("category");
                String mealName = mealsResultSet.getString("meal");
                int mealId = mealsResultSet.getInt("meal_id");
                String mealIngredients = loadMealIngredients(mealId);

                Meal newMeal = new Meal();
                newMeal.setFields("CATEGORY", mealCategory);
                newMeal.setFields("NAME", mealName);
                newMeal.setFields("INGREDIENTS", mealIngredients);
                mealsList.add(newMeal);
            }

        } catch (SQLException e) {
            System.err.println("Error loading meals from the database!");
        }

        return mealsList;
    }

    private String loadMealIngredients(int mealId) {
        List<String> ingredientsList = new ArrayList<>();
        String ingredientQuery = "SELECT ingredient FROM ingredients WHERE meal_id = ?";

        try (PreparedStatement ingredientStatement = dbConnection.prepareStatement(ingredientQuery)) {
            ingredientStatement.setInt(1, mealId);

            try (ResultSet ingredientsResultSet = ingredientStatement.executeQuery()) {
                while (ingredientsResultSet.next()) {
                    String ingredient = ingredientsResultSet.getString("ingredient");
                    ingredientsList.add(ingredient);
                }

            } catch (SQLException e) {
                System.err.println("Error retrieving ingredients for meal ID: " + mealId);
                return null;
            }

        } catch (SQLException e) {
            System.err.println("Error preparing statement for meal ID: " + e.getMessage());
        }

        return String.join(",", ingredientsList);
    }

    void addMeal(Meal mealToAdd) {
        String insertMealQuery = "INSERT INTO meals (category, meal) VALUES (?, ?) RETURNING meal_id;";

        try (PreparedStatement insertMeal = dbConnection.prepareStatement(insertMealQuery)) {
            insertMeal.setString(1, mealToAdd.getCategory());
            insertMeal.setString(2, mealToAdd.getMealName());
            ResultSet generatedKeys = insertMeal.executeQuery();

            if (generatedKeys.next()) {
                int mealId = generatedKeys.getInt(1);
                addMealIngredients(mealId, mealToAdd.getIngredients());
            }
        } catch (SQLException e) {
            System.err.println("Error adding meal: " + e.getMessage());
        }
    }

    private void addMealIngredients(int mealId, List<String> ingredients) {
        String insertIngredientQuery = "INSERT INTO ingredients (meal_id, ingredient) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(insertIngredientQuery)) {
            for (String ingredient : ingredients) {
                preparedStatement.setInt(1, mealId);
                preparedStatement.setString(2, ingredient);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error adding ingredients for meal ID: " + e.getMessage());
        }
    }

    public void truncateTables(String tableName1, String tableName2) {
        truncateTable(tableName1);
        truncateTable(tableName2);
    }

    private void truncateTable(String tableName) {
        String truncateQuery = "TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE;";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(truncateQuery)) {
            preparedStatement.executeUpdate();
            System.out.println("All data deleted from table: " + tableName);
        } catch (SQLException e) {
            System.err.println("Error truncating table " + tableName + ": " + e.getMessage());
        }
    }
    
}
