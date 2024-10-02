Stage 2 Description

Description
One meal is not going to get you far! Let's create the main menu to add several meals and display their properties.
For this, we need to add a few commands:

Add starts the meal input process and prompts users for the meal properties;
Show prints the list of saved meals with their names, categories, and ingredients;
After executing add or show, the program should ask what users want to do next;
The program must run until users input exit — the command that terminates the program.

In this stage, your program should also check the user input. What if users enter something wrong?

If the input meal category is something other than breakfast, lunch, or dinner, print Wrong meal category! Choose from: breakfast, 
lunch, dinner. and prompt users for input;

If the meal's name or ingredients have a wrong format (for example, there are numbers or non-alphabet characters; ingredients are blank,
and so on), print Wrong format. Use letters only! and prompt users for input. Meal's name or ingredients containing several words
like "peanut butter" should not be matched as wrong format.

Objectives
To complete this stage, the program must comply with the following requirements:

1.Create an infinite loop of your program that can be terminated with the exit command only;
2.Prompt users to choose an operation with the message What would you like to do (add, show, exit)?
3.After the command has been processed, ask for another operation;
4.Make sure that the input and output formats are correct;
5.If users want to add a meal, follow the sequence from the previous stage. Don't forget to validate input as explained above. 
Output The meal has been added! before proceeding;
6.If users want to show the meals when no meals have been added, print No meals saved. Add a meal first. If there are meals that can 
be displayed, print them in the order they've been added, following the format from Stage 1;
7.Print Bye! and end the program once the exit command is entered;
8.If users fail to input a valid command, print the following message again: What would you like to do (add, show, exit)?