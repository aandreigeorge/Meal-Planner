Stage 5 Description

A solid person plans for the week ahead! Let's help our users plan their meals for the entire week. In this stage, we will add two new command
— plan and list plan. We also need to change the main menu accordingly. From now on, it should read as What would you like to do 
(add, show, plan, list plan, exit)?

When users input plan, the program should print the first day of the week, Monday, and print the list of all breakfasts stored in the database
in alphabetical order. After this, the program should ask users to pick a meal with the following message: Choose the breakfast for Monday from
the list above:

After users input a meal option, the program should verify it. If it's not stored in the database, print This meal doesn’t exist. 
Choose a meal from the list above. If the input is correct, move on to the next category — lunch and then to dinner. Once the meals for three
categories are picked, print Yeah! We planned the meals for Monday. Repeat these steps for other weekdays. In the end, print the whole plan for
the week.

Save the plan to the database. For this purpose, create a new table named plan when the program starts. This table contains the meal_option,
meal_category, and meal_id. The third column must match the meal_id columns of the other two tables. You are free to choose how to implement 
the fields in this table. If a new plan is created, delete the old plan.

The list plan command is simple, and it only prints the plan to the console which is stored in the database.

Objectives

Create a table in the database named plan;
Add the plan option to the menu;
When users choose the plan option:
Print Monday;
Print the meal names of the breakfast category in alphabetical order;
Prompt Choose the breakfast for Monday from the list above:
Once users input a meal, print the meal names of the lunch category in alphabetical order;
Prompt Choose the lunch for Monday from the list above:
Once users input a meal, print the meal names of the dinner category in alphabetical order;
Prompt Choose the dinner for Monday from the list above:
Once users input a meal, print Yeah! We planned the meals for Monday.

If a meal option isn't in the provided list, print This meal doesn’t exist. Choose a meal from the list above.;
Print a blank line and repeat for the rest of the week;
Once the plan for the week is drawn, print it. The plan print format is as follows:

Monday
Breakfast: [meal's name]
Lunch: [meal's name]
Dinner: [meal's name]

Tuesday etc.