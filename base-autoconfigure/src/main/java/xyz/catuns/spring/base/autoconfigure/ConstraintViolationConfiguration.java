package xyz.catuns.spring.base.autoconfigure;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import xyz.catuns.spring.base.autoconfigure.condition.*;
import xyz.catuns.spring.base.constraint.handler.GlobalDataIntegrityExceptionHandler;
import xyz.catuns.spring.base.constraint.parser.ConstraintViolationParser;
import xyz.catuns.spring.base.constraint.parser.ConstraintViolationStrategy;
import xyz.catuns.spring.base.constraint.properties.ConstraintViolationProperties;
import xyz.catuns.spring.base.constraint.strategy.generic.GenericStrategy;
import xyz.catuns.spring.base.constraint.strategy.h2.H2ForeignKeyStrategy;
import xyz.catuns.spring.base.constraint.strategy.h2.H2NotNullStrategy;
import xyz.catuns.spring.base.constraint.strategy.h2.H2UniqueStrategy;
import xyz.catuns.spring.base.constraint.strategy.mysql.MariaDBUniqueStrategy;
import xyz.catuns.spring.base.constraint.strategy.mysql.MySQLDuplicateEntryStrategy;
import xyz.catuns.spring.base.constraint.strategy.mysql.MySQLForeignKeyStrategy;
import xyz.catuns.spring.base.constraint.strategy.mysql.MySQLNotNullStrategy;
import xyz.catuns.spring.base.constraint.strategy.oracle.OracleCheckStrategy;
import xyz.catuns.spring.base.constraint.strategy.oracle.OracleForeignKeyStrategy;
import xyz.catuns.spring.base.constraint.strategy.oracle.OracleNotNullStrategy;
import xyz.catuns.spring.base.constraint.strategy.oracle.OracleUniqueStrategy;
import xyz.catuns.spring.base.constraint.strategy.postgresql.PostgreSQLCheckStrategy;
import xyz.catuns.spring.base.constraint.strategy.postgresql.PostgreSQLForeignKeyStrategy;
import xyz.catuns.spring.base.constraint.strategy.postgresql.PostgreSQLNotNullStrategy;
import xyz.catuns.spring.base.constraint.strategy.postgresql.PostgreSQLUniqueStrategy;
import xyz.catuns.spring.base.constraint.strategy.sqlserver.SQLServerForeignKeyStrategy;
import xyz.catuns.spring.base.constraint.strategy.sqlserver.SQLServerNotNullStrategy;
import xyz.catuns.spring.base.constraint.strategy.sqlserver.SQLServerUniqueStrategy;

import java.util.ArrayList;
import java.util.List;


@AutoConfiguration
@ConditionalOnClass({DataIntegrityViolationException.class, ConstraintViolationException.class})
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "app.exception.constraint", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ConstraintViolationProperties.class)
public class ConstraintViolationConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ConstraintViolationConfiguration.class);

    @Bean
    @ConditionalOnPostgreSQL
    @ConditionalOnMissingBean(name = "postgresqlUniqueStrategy")
    public PostgreSQLUniqueStrategy postgresqlUniqueStrategy() {
        log.info("Registering PostgreSQL unique constraint strategy");
        return new PostgreSQLUniqueStrategy();
    }

    @Bean
    @ConditionalOnPostgreSQL
    @ConditionalOnMissingBean(name = "postgresqlForeignKeyStrategy")
    public PostgreSQLForeignKeyStrategy postgresqlForeignKeyStrategy() {
        log.info("Registering PostgreSQL foreign key strategy");
        return new PostgreSQLForeignKeyStrategy();
    }

    @Bean
    @ConditionalOnPostgreSQL
    @ConditionalOnMissingBean(name = "postgresqlNotNullStrategy")
    public PostgreSQLNotNullStrategy postgresqlNotNullStrategy() {
        log.info("Registering PostgreSQL not-null strategy");
        return new PostgreSQLNotNullStrategy();
    }

    @Bean
    @ConditionalOnPostgreSQL
    @ConditionalOnMissingBean(name = "postgresqlCheckStrategy")
    public PostgreSQLCheckStrategy postgresqlCheckStrategy() {
        log.info("Registering PostgreSQL check constraint strategy");
        return new PostgreSQLCheckStrategy();
    }

//     Oracle Strategies
    @Bean
    @ConditionalOnOracle
    @ConditionalOnMissingBean(name = "oracleUniqueStrategy")
    public OracleUniqueStrategy oracleUniqueStrategy() {
        log.info("Registering Oracle unique constraint strategy");
        return new OracleUniqueStrategy();
    }

    @Bean
    @ConditionalOnOracle
    @ConditionalOnMissingBean(name = "oracleForeignKeyStrategy")
    public OracleForeignKeyStrategy oracleForeignKeyStrategy() {
        log.info("Registering Oracle foreign key strategy");
        return new OracleForeignKeyStrategy();
    }

    @Bean
    @ConditionalOnOracle
    @ConditionalOnMissingBean(name = "oracleNotNullStrategy")
    public OracleNotNullStrategy oracleNotNullStrategy() {
        log.info("Registering Oracle not-null strategy");
        return new OracleNotNullStrategy();
    }

    @Bean
    @ConditionalOnOracle
    @ConditionalOnMissingBean(name = "oracleCheckStrategy")
    public OracleCheckStrategy oracleCheckStrategy() {
        log.info("Registering Oracle check constraint strategy");
        return new OracleCheckStrategy();
    }

//     MySQL Strategies
    @Bean
    @ConditionalOnMySQL
    @ConditionalOnMissingBean(name = "mysqlDuplicateEntryStrategy")
    public MySQLDuplicateEntryStrategy mysqlDuplicateEntryStrategy() {
        log.info("Registering MySQL duplicate entry strategy");
        return new MySQLDuplicateEntryStrategy();
    }

    @Bean
    @ConditionalOnMySQL
    @ConditionalOnMissingBean(name = "mysqlForeignKeyStrategy")
    public MySQLForeignKeyStrategy mysqlForeignKeyStrategy() {
        log.info("Registering MySQL foreign key strategy");
        return new MySQLForeignKeyStrategy();
    }

    @Bean
    @ConditionalOnMySQL
    @ConditionalOnMissingBean(name = "mysqlNotNullStrategy")
    public MySQLNotNullStrategy mysqlNotNullStrategy() {
        log.info("Registering MySQL not-null strategy");
        return new MySQLNotNullStrategy();
    }

//     MariaDB Strategies
    @Bean
    @ConditionalOnMariaDB
    @ConditionalOnMissingBean(name = "mariaDBUniqueStrategy")
    public MariaDBUniqueStrategy mariaDBUniqueStrategy() {
        log.info("Registering MariaDB unique constraint strategy");
        return new MariaDBUniqueStrategy();
    }

//     SQL Server Strategies
    @Bean
    @ConditionalOnSQLServer
    @ConditionalOnMissingBean(name = "sqlServerUniqueStrategy")
    public SQLServerUniqueStrategy sqlServerUniqueStrategy() {
        log.info("Registering SQL Server unique constraint strategy");
        return new SQLServerUniqueStrategy();
    }

    @Bean
    @ConditionalOnSQLServer
    @ConditionalOnMissingBean(name = "sqlServerForeignKeyStrategy")
    public SQLServerForeignKeyStrategy sqlServerForeignKeyStrategy() {
        log.info("Registering SQL Server foreign key strategy");
        return new SQLServerForeignKeyStrategy();
    }

    @Bean
    @ConditionalOnSQLServer
    @ConditionalOnMissingBean(name = "sqlServerNotNullStrategy")
    public SQLServerNotNullStrategy sqlServerNotNullStrategy() {
        log.info("Registering SQL Server not-null strategy");
        return new SQLServerNotNullStrategy();
    }

//     H2 Strategies
    @Bean
    @ConditionalOnH2
    @ConditionalOnMissingBean(name = "h2UniqueStrategy")
    public H2UniqueStrategy h2UniqueStrategy() {
        log.info("Registering H2 unique constraint strategy");
        return new H2UniqueStrategy();
    }

    @Bean
    @ConditionalOnH2
    @ConditionalOnMissingBean(name = "h2ForeignKeyStrategy")
    public H2ForeignKeyStrategy h2ForeignKeyStrategy() {
        log.info("Registering H2 foreign key strategy");
        return new H2ForeignKeyStrategy();
    }

    @Bean
    @ConditionalOnH2
    @ConditionalOnMissingBean(name = "h2NotNullStrategy")
    public H2NotNullStrategy h2NotNullStrategy() {
        log.info("Registering H2 not-null strategy");
        return new H2NotNullStrategy();
    }

//     Generic fallback strategy (always registered)
    @Bean
    @ConditionalOnMissingBean(GenericStrategy.class)
    public GenericStrategy genericStrategy() {
        log.info("Registering generic fallback strategy");
        return new GenericStrategy();
    }

//     Parser bean
    @Bean
    @ConditionalOnMissingBean
    public ConstraintViolationParser constraintViolationParser(
            List<ConstraintViolationStrategy> strategies,
            GenericStrategy genericStrategy
    ) {
        List<ConstraintViolationStrategy> allStrategies = new ArrayList<>(strategies);

//         Ensure generic strategy is last
        allStrategies.remove(genericStrategy);
        allStrategies.add(genericStrategy);

        log.info("Constraint Violation Parser initialized with {} strategies", allStrategies.size());

        return new ConstraintViolationParser(allStrategies);
    }

//     Exception handler bean
    @Bean
    @ConditionalOnMissingBean
    public GlobalDataIntegrityExceptionHandler globalDataIntegrityExceptionHandler(
            ConstraintViolationParser parser,
            ConstraintViolationProperties properties
    ) {
        log.info("Registering Global Data Integrity Exception Handler");
        return new GlobalDataIntegrityExceptionHandler(parser, properties);
    }
}