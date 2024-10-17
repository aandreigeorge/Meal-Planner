Stage 3 Description

At this point, when we close our program, we lose all our stored meals! Let's improve our planner and connect a database to retrieve
all meals after a restart.

To connect a database to our project, we can use Java DataBase Connectivity (JDBC), an API for database-independent connectivity between
programs and various databases. This standard ensures the same methods for connecting, updating, querying, and results handling, regardless of
the database you employ. However, the choice of the database affects the SQL syntax, available data types, and supported features.

In this project, we will refer to PostgreSQL. It is a powerful, open source object-relational database system that contains great capabilities.
PostgreSQL runs on all major operating systems and supports advanced data types such as arrays, hstore, and user-defined types. Although, there
may be some differences between different SQL databases. You can find more information in the official PostgreSQL documentation

In this stage, we're going to store meal data in database tables. When the program restarts, the saved data should be available in the program.

To use PostgreSQL in Java, you need to import the postgresql-jdbc library into your project. For the Meal Planner, this is already done.
You can find an example of this driver on the postgresql-jdbc website.

Mind that the nested resultset requires different statement instances.

The jdbc:postgresql:person_db string includes three strings divided by semicolons. The first one is the database interface, the second is the 
database, and the third is the name of your database.

It's a good idea to use pgAdmin — a nice GUI tool for browsing and managing PostgreSQL databases. You can inspect the tables you've created and 
the data in your database.

If you are connected to the database file when you check your code, it may lead to issues.
Make sure to create meals_db database and add postgres user with 1111 password to it before initializing tests.

Objectives
Your program should connect to a database named meals_db;
Create two tables in this database schema. Name the first one as meals with three columns: category (varchar), meal (varchar), 
and meal_id (integer). Name the second table ingredients; it must contain three columns: ingredient (varchar), ingredient_id (integer),
and meal_id (integer). meal_id in both tables must match!
Read all data in the tables, so their contents are available before a show operation is requested;
When users add a new meal, store it in your database.
There are no changes in the input/output structure in this stage.

Note: The contents of the database tables are cleared at the beginning of the testing process.