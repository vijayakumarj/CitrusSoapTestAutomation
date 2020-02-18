package org.example;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.jdbc.server.JdbcServer;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTest extends TestNGCitrusTestRunner {
    @Autowired
    public BasicDataSource todoListDataSource;

    @CitrusTest
    @Test
    public void createTable() {
        async().actions(
                sql(executeSQLBuilder -> executeSQLBuilder.
                        dataSource(todoListDataSource)
                        .statement("CREATE TABLE IF NOT EXISTS todo_entries (id VARCHAR(50), title VARCHAR(255), description VARCHAR(255), done BOOLEAN)")
                ));
    }

    @CitrusTest
    @Test
    public void validationTest() {
        variable("todoId", "uuid");
        variable("todoName", "citrus");
        variable("todoDescription", "poc work");
        variable("updatedTodoId", "new id");

        echo("Clear old data and insert a new row for validation");
        List<String> initialInsertStatements = new ArrayList<>();
        initialInsertStatements.add("delete from todo_entries");
        initialInsertStatements.add("insert into todo_entries values ('uuid', 'citrus','poc work',1)");

        String updateStatement = "update todo_entries set id='new id' where done =1";
        echo("initial insert statements");
        sql(executeSQLBuilder -> executeSQLBuilder
                .dataSource(todoListDataSource)
                .statements(initialInsertStatements)
                .build()
        );

        echo("select statement for validation");

        query(executeSQLBuilder -> executeSQLBuilder
                .dataSource(todoListDataSource)
                .statement("SELECT id, title, description FROM todo_entries where done=1")
                .validate("id", "${todoId}")
                .validate("title", "${todoName}")
                .validate("description", "${todoDescription}")
        );

        echo("executing update statements");
        sql(executeSQLBuilder -> executeSQLBuilder
                .dataSource(todoListDataSource)
                .statement(updateStatement)
                .build()
        );

        echo("Validating the updated row");
        query(executeSQLQueryBuilder -> executeSQLQueryBuilder
                .dataSource(todoListDataSource)
                .statement("SELECT id, title, description FROM todo_entries where done=1")
                .validate("id", "${updatedTodoId}")
                .validate("title", "${todoName}")
                .validate("description", "${todoDescription}")
                .extract("id","extractedId")
        );

        echo("${extractedId}");
    }
}
