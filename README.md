# TestAutomation Using Citrus Framework
This is a POC to test the SOAP based web services using Citrus Framework. Citrus framework is a multi-channel testing framework based on spring-java. In this POC I have created 3 Tests for two different Endpoints with Schema &amp; XML Message Validation.

I have added one more test for operating MariaDB and validating the values


Instructions for Database test framework implementation: 

# MariaDB SetUp
Installation Link: https://downloads.mariadb.org/mariadb/10.4.12/
Download the MSI file from the page and install it using the express installation.

# Once MariaDB installed,
Open Command Prompt
Move to the installed location
Run the command ‘Mysql -u root -p’
Enter the password, ‘root’ will be the user.
Problems faced: Nill


# Citrus Framework Configuration
Create a bean for BasicDataSource. This will be used as the connection object in all the tests. It’s autowired with the objects defined in the test classes.

# Code Implementation in the endpointConfig.java: 

```java
@Bean(destroyMethod = "close")
public BasicDataSource todoListDataSource() {
   BasicDataSource dataSource = new BasicDataSource();
   dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
   dataSource.setUrl("jdbc:mariadb://localhost/bookstore");
   dataSource.setUsername("root");
   dataSource.setPassword("PASSWORD");
   dataSource.setInitialSize(1);
   dataSource.setMaxActive(5);
   dataSource.setMaxIdle(2);
   return dataSource;
}
```

And define the autowired object in the test script class as below to use it in the following tests.
@Autowired
public BasicDataSource todoListDataSource;

# Problems faced
Bean configuration error - as the endpoint configuration was not pointing to any endpointConfig.class
Forgot to add .build(); method call in the test step caused test failure.
References : http://citrusframework.org/samples/jdbc/

# Test Case Step Explanation  

Create a test method with annotation @Test and @CitrusTest 

To Execute any SQL statement for Create, Delete, Update use sql() Method as mentioned below 

```Java
sql(executeSQLBuilder -> executeSQLBuilder 
 	.dataSource(todoListDataSource) 
 	.statements("insert into todo_entries values ('uuid', 'citrus','poc work',1)") 
	.build() 
); 
```

To Execute any select statements and validate the retreived data set, use query() method as below 

```java
query(executeSQLBuilder -> executeSQLBuilder 
.dataSource(todoListDataSource) 
.statement("SELECT id, title, description FROM todo_entries where done=1") 
.validate("id", "${todoId}") 
.validate("title", "${todoName}") 
.validate("description", "${todoDescription}") 
); 
``` 
To Extract the values from the retreived dataset, use extract() method to store the value into a ctirus variable which can be done as below  

```java
query(executeSQLQueryBuilder -> executeSQLQueryBuilder 
.dataSource(todoListDataSource) 
.statement("SELECT id, title, description FROM todo_entries where done=1") 
.validate("id", "${updatedTodoId}") 
.validate("title", "${todoName}") 
.validate("description", "${todoDescription}") 
.extract("id","extractedId") 
); 
 
echo("${extractedId}"); 
```
