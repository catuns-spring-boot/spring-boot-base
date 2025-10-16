package xyz.catuns.spring.base.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;
import java.util.List;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ConditionalOnH2.H2Condition.class)
public @interface ConditionalOnH2 {

    class H2Condition extends OnDatabaseCondition {
        @Override
        protected List<String> getDatabaseIdentifiers() {
            return List.of("h2");
        }

        @Override
        protected List<String> getDriverClassNames() {
            return List.of("org.h2.Driver");
        }
    }
}