Stage 4 Description

Let's improve the navigation in the program and make it more user-friendly! In this stage, we will enhance the show command. 
The program will ask users to specify the meal category with the following message: Which category do you want to print (breakfast, lunch, dinner)?
After this, the program will search through the database and print only the chosen category. If the requested category is empty, the program should
show an appropriate message.

Objectives
When users input show, your program should:

Ask users for the meal category;
Search through the database for meals from the chosen category;
Print Category: <category>. For each meal, print Meal's name: <meal name>, followed by the specific meal ingredients list, each on a new line.
The meals and ingredients should be printed in the same order they've been added;
If the input category doesn't exist, print Wrong meal category! Choose from: breakfast, lunch, dinner.;
If there are no meals in the category, print No meals found.