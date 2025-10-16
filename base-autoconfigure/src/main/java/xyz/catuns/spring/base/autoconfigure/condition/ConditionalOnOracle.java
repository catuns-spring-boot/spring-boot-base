package xyz.catuns.spring.base.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;
import java.util.List;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ConditionalOnOracle.OracleCondition.class)
public @interface ConditionalOnOracle {

    class OracleCondition extends OnDatabaseCondition {
        @Override
        protected List<String> getDatabaseIdentifiers() {
            return List.of("oracle");
        }

        @Override
        protected List<String> getDriverClassNames() {
            return List.of(
                    "oracle.jdbc.OracleDriver",
                    "oracle.jdbc.driver.OracleDriver"
            );
        }
    }
}