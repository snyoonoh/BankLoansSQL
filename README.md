# BankLoansSQL
SQL database and a JDBC file that recursively calls upon the database to find all transfer funds
#### ddl.sql
a database file for sql that creates three tables : customer, loan and borrowers as well as inserting data into the tables<br />
Takes into account key dependencies (foreign keys) such as borrower having foreign key attributes: customer name, loan number<br />
#### SQL_Recursion
A JDBC file that accesses the database created from ddl.sql and uses semi-naive evaluation to recursively go through the data to find transfer funds. Recursion of this sort cannot be done using soley SQL.
