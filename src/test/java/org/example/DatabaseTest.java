package org.example;

import com.consol.citrus.annotations.CitrusTest;
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
    public void createTable(){
        async().actions(
        sql(executeSQLBuilder -> executeSQLBuilder.
            dataSource(todoListDataSource)
                .statement("CREATE TABLE IF NOT EXISTS todo_entries (id VARCHAR(50), title VARCHAR(255), description VARCHAR(255), done BOOLEAN)")
        ));
    }

    @CitrusTest
    @Test
    public void validationTest(){
        variable("todoId", "uuid");
        variable("todoName", "citrus");
        variable("todoDescription", "poc work");

        echo("Clear old data and insert a new row for validation");
        List<String> initialStatements = new ArrayList<>();
        initialStatements.add("delete from todo_entries");
        initialStatements.add("insert into todo_entries values ('uuid', 'citrus','poc work',1)");

        echo("inital statements");
        async()
                .actions(sql(executeSQLBuilder -> executeSQLBuilder
                        .dataSource(todoListDataSource)
                        .statements(initialStatements)
                ));

        echo("select statement for validation");
        async()
                .actions(query(executeSQLBuilder-> executeSQLBuilder
                        .dataSource(todoListDataSource)
                        .statement("SELECT id, title, description FROM todo_entries where done=1")
                        .validate("id", "${todoId}")
                        .validate("title", "${todoName}")
                        .validate("description", "${todoDescription}")
                ));
    }
}
