package xyz.catuns.spring.base.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;
import java.util.List;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ConditionalOnSQLServer.SQLServerCondition.class)
public @interface ConditionalOnSQLServer {

    class SQLServerCondition extends OnDatabaseCondition {
        @Override
        protected List<String> getDatabaseIdentifiers() {
            return List.of("microsoft sql server", "sql server");
        }

        @Override
        protected List<String> getDriverClassNames() {
            return List.of("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }
    }
}