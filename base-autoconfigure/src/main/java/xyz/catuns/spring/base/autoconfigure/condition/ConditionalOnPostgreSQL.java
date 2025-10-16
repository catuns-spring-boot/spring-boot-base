package xyz.catuns.spring.base.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;
import java.util.List;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ConditionalOnPostgreSQL.PostgreSQLCondition.class)
public @interface ConditionalOnPostgreSQL {

    class PostgreSQLCondition extends OnDatabaseCondition {
        @Override
        protected List<String> getDatabaseIdentifiers() {
            return List.of("postgresql", "postgres");
        }

        @Override
        protected List<String> getDriverClassNames() {
            return List.of("org.postgresql.Driver");
        }
    }
}