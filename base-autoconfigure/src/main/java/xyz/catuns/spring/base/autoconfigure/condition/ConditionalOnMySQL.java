package xyz.catuns.spring.base.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;
import java.util.List;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ConditionalOnMySQL.MySQLCondition.class)
public @interface ConditionalOnMySQL {

    class MySQLCondition extends OnDatabaseCondition {
        @Override
        protected List<String> getDatabaseIdentifiers() {
            return List.of("mysql");
        }

        @Override
        protected List<String> getDriverClassNames() {
            return List.of(
                    "com.mysql.cj.jdbc.Driver",
                    "com.mysql.jdbc.Driver"
            );
        }
    }
}