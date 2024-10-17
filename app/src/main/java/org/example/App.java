package org.example;

import java.sql.Connection;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {
        try (Connection dbConnection = DbManager.getDbConnection()) {
            new MealManager(dbConnection);
        } catch (SQLException e) {
            System.err.println("Database Connection Failed!");
        }
    }

    public String getGreeting() {
        return "Hello World!";
    }

}
