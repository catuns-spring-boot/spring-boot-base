package xyz.catuns.spring.base.autoconfigure.condition;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;

public abstract class OnDatabaseCondition extends SpringBootCondition {

    protected abstract List<String> getDatabaseIdentifiers();
    protected abstract List<String> getDriverClassNames();

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // Try to detect from DataSource
        try {
            if (context.getBeanFactory() != null &&
                    context.getBeanFactory().containsBean("dataSource")) {

                DataSource dataSource = context.getBeanFactory().getBean(DataSource.class);
                try (Connection connection = dataSource.getConnection()) {
                    DatabaseMetaData metaData = connection.getMetaData();
                    String databaseProductName = metaData.getDatabaseProductName().toLowerCase();

                    for (String identifier : getDatabaseIdentifiers()) {
                        if (databaseProductName.contains(identifier.toLowerCase())) {
                            return ConditionOutcome.match(
                                    "Database product name '" + databaseProductName +
                                            "' matches identifier '" + identifier + "'"
                            );
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Continue to driver class detection
        }

        // Fallback: detect from driver class on classpath
        for (String driverClassName : getDriverClassNames()) {
            try {
                Class.forName(driverClassName);
                return ConditionOutcome.match(
                        "Database driver class '" + driverClassName + "' found on classpath"
                );
            } catch (ClassNotFoundException e) {
                // Continue
            }
        }

        return ConditionOutcome.noMatch(
                "No matching database detected for identifiers: " + getDatabaseIdentifiers()
        );
    }
}