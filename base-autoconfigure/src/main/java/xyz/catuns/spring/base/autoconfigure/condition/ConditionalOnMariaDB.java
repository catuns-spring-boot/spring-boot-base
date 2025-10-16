package xyz.catuns.spring.base.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;
import java.util.List;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ConditionalOnMariaDB.MariaDBCondition.class)
public @interface ConditionalOnMariaDB {

    class MariaDBCondition extends OnDatabaseCondition {
        @Override
        protected List<String> getDatabaseIdentifiers() {
            return List.of("mariadb");
        }

        @Override
        protected List<String> getDriverClassNames() {
            return List.of("org.mariadb.jdbc.Driver");
        }
    }
}