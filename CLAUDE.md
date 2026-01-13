# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**spring-boot-base** is a reusable Spring Boot 3.5.6 starter library for Java 21 that provides application-wide exception handling and database constraint violation parsing. It automatically detects the database type (PostgreSQL, MySQL, Oracle, MariaDB, SQL Server, H2) and provides user-friendly error messages for constraint violations via RFC 7807 Problem Details responses.

The project is a Maven multi-module build published to GitHub Packages for downstream Spring Boot applications.

## Common Commands

### Build and Package
```bash
mvn clean install                    # Build all modules and install to local Maven repo
mvn clean package                    # Build all modules and create JARs
mvn clean verify                     # Run full build with tests and verification
```

### Testing
```bash
mvn test                             # Run all tests across all modules
mvn test -pl module-name             # Run tests for a specific module (e.g., base-constraint)
mvn test -Dtest=ClassName            # Run a specific test class
mvn test -Dtest=ClassName#methodName # Run a specific test method
```

### Linting and Code Quality
```bash
mvn checkstyle:check                 # Run checkstyle verification
mvn spotbugs:check                   # Run static analysis (if configured)
```

### Dependency Management
```bash
mvn dependency:tree                  # Show dependency tree for all modules
mvn dependency:tree -pl module-name  # Show dependency tree for specific module
mvn dependency:analyze               # Find unused and undeclared dependencies
```

### Building Specific Modules
```bash
mvn clean install -pl base-core      # Build only base-core and its dependencies
mvn clean install -pl base-starter   # Build base-starter (includes all dependencies)
```

### Deployment (to GitHub Packages)
```bash
mvn clean deploy                     # Deploy to GitHub Packages (requires auth token in settings.xml)
```

## Project Structure

### Module Hierarchy

```
spring-boot-base (root, pom packaging)
├── base-dependencies           [BOM for dependency management]
├── base-core                   [Core exceptions and properties]
├── base-constraint             [Database constraint violation parsing]
├── base-autoconfigure          [Spring Boot auto-configuration]
├── base-starter                [Convenience starter aggregating autoconfigure + core]
└── base-starter-parent         [Parent POM for downstream applications]
```

### Key Modules Explained

**base-dependencies**
- BOM (Bill of Materials) module
- Centralized dependency management for all other modules
- Managed versions: Spring Boot 3.5.6, Lombok, SLF4J 2.0.17, MapStruct 1.5.5.Final

**base-core**
- Core exception hierarchy: `ControllerException` and subclasses (`BadRequestException`, `NotFoundException`, `UnauthorizedException`, `ForbiddenException`)
- Global exception handler with `@RestControllerAdvice` for handling generic exceptions and validation errors
- Configuration properties and metadata classes for exception handling

**base-constraint**
- Strategy-based constraint violation parser supporting 6 databases
- Database-specific implementations for UNIQUE, FOREIGN_KEY, NOT_NULL, and CHECK constraints
- Global data integrity exception handler converting `DataIntegrityViolationException` to user-friendly messages
- Pluggable strategy pattern for easy extension to new databases

**base-autoconfigure**
- Spring Boot `@AutoConfiguration` that conditionally registers beans based on:
  - Detected database type (custom conditions: `ConditionalOnMySQL`, `ConditionalOnPostgreSQL`, etc.)
  - Whether servlet web context exists
  - Feature enablement via properties
- Registers 15+ database-specific constraint violation strategies, exception handlers, and parsers
- Configuration properties: `app.exception.*` and `app.exception.constraint.*`

**base-starter**
- Aggregator starter bringing together `base-autoconfigure` and `base-core`
- Single dependency for applications to include all functionality

## Architecture Highlights

### Strategy Pattern for Database Abstraction

The constraint violation handling uses the **Strategy Pattern** extensively:
- `ConstraintViolationStrategy` interface defines the contract
- Multiple database-specific implementations: `MySQLDuplicateEntryStrategy`, `PostgreSQLUniqueConstraintStrategy`, etc.
- `ConstraintViolationParser` orchestrates strategy chain with fallback to generic strategy
- Each database (MySQL, PostgreSQL, Oracle, SQL Server, MariaDB, H2) has 3-4 specialized parsers

**Location**: `base-constraint/src/main/java/xyz/catuns/spring/base/constraint/strategy/`

### Spring Boot Auto-Configuration

Auto-configuration uses:
- **Meta-annotations** for database detection without runtime reflection overhead
- **Conditional beans** to register only necessary database-specific strategies
- **Configuration properties** with prefix `app.exception` and `app.exception.constraint`
- **AutoConfiguration.imports** in META-INF for Spring Boot discovery

The configuration hierarchy:
- `BaseConfigurationProperties` (base, prefix: "app")
- `ExceptionHandlerProperties` extends metadata + adds Spring annotations
- `ConstraintViolationProperties` extends metadata + adds Spring annotations

### Exception Handling Flow

1. **Controller exceptions**: Thrown by application code extending `ControllerException`
2. **Generic exception handler** (`GlobalExceptionHandler`): Catches and returns RFC 7807 `ProblemDetail`
3. **Database constraint violations**: `DataIntegrityViolationException` caught by `GlobalDataIntegrityExceptionHandler`
4. **Strategy-based parsing**: Parser runs through applicable strategies to extract constraint details
5. **User-friendly response**: Technical error message converted to readable message (e.g., "A record with this email already exists")

## Configuration Properties

### Exception Handler (`app.exception.*`)
```yaml
app:
  exception:
    enabled: true                    # Enable/disable exception handler
    includeStackTrace: false         # Include stack trace in response
    includeCause: false              # Include root cause exception
    includeBindingErrors: true       # Include validation binding errors
    logExceptions: true              # Log exceptions to SLF4J
```

### Constraint Violation Handler (`app.exception.constraint.*`)
```yaml
app:
  exception:
    constraint:
      enabled: true                  # Enable/disable constraint handler
      statusCode: 409                # HTTP status (409 Conflict, 400 Bad Request, or 422 Unprocessable Entity)
      includeRejectedValues: false   # Don't expose sensitive data in responses
      fieldNameStrategy: TITLE_CASE  # Can be TITLE_CASE, CAMEL_CASE, or ORIGINAL
```

**Note**: There is a typo in some configuration properties where `exception` is spelled `exeption`. Be aware when searching or modifying config-related code.

## Key Classes and Packages

### Exception Hierarchy (base-core)
- `xyz.catuns.spring.base.exception.controller.ControllerException` (base with HTTP status)
  - `BadRequestException` (400)
  - `NotFoundException` (404)
  - `UnauthorizedException` (401)
  - `ForbiddenException` (403)

### Constraint Handling (base-constraint)
- `xyz.catuns.spring.base.constraint.parser.ConstraintViolationParser` - orchestrator
- `xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo` - DTO with violation details
- `xyz.catuns.spring.base.constraint.strategy.ConstraintViolationStrategy` - interface
- `xyz.catuns.spring.base.constraint.handler.GlobalDataIntegrityExceptionHandler` - REST advice

### Auto-Configuration (base-autoconfigure)
- `xyz.catuns.spring.base.autoconfigure.BaseAutoConfiguration` - main auto-config
- `xyz.catuns.spring.base.autoconfigure.ExceptionHandlerAutoConfiguration` - exception handler setup
- `xyz.catuns.spring.base.autoconfigure.ConstraintViolationConfiguration` - constraint handling setup
- `xyz.catuns.spring.base.autoconfigure.condition.OnDatabaseCondition` - base for database detection

## Development Notes

### Adding Support for a New Database

1. Create strategy classes in `base-constraint/src/main/java/xyz/catuns/spring/base/constraint/strategy/{database-name}/`
2. Implement `ConstraintViolationStrategy` for each constraint type (UNIQUE, FOREIGN_KEY, NOT_NULL, CHECK)
3. Add `@ConditionalOn{Database}` annotation class in `base-autoconfigure/src/main/java/xyz/catuns/spring/base/autoconfigure/condition/`
4. Register strategies in `ConstraintViolationConfiguration` with appropriate conditional bean definitions
5. Test parsing with real error messages from the target database

### Dependency Management

- All dependencies are centralized in `base-dependencies/pom.xml` as a BOM
- Use `<dependencyManagement>` to reference the BOM in other modules
- Never add version tags to dependencies in child modules (except test dependencies)

### Maven Flatten Plugin

The project uses `flatten-maven-plugin` to flatten POM files for distribution:
- Parent pom uses `revision` property instead of hardcoding version (1.0.1-SNAPSHOT)
- Flatten plugin replaces `${revision}` with actual version in distributed JARs
- Run `mvn clean` to remove `.flattened-pom.xml` files

### Building and Testing Workflow

1. **Local development**: `mvn clean install` to build all modules
2. **Single module testing**: `mvn test -pl base-constraint` for quick feedback
3. **Full verification before commit**: `mvn clean verify` to run all checks
4. **Deployment**: GitHub Actions likely handles deployment; check `.github/workflows/`

## Distribution and Versioning

- **Current Version**: 1.0.1-SNAPSHOT (development version)
- **Group ID**: xyz.catuns.spring
- **Artifact IDs**: base-core, base-constraint, base-autoconfigure, base-starter, base-starter-parent
- **Distribution**: GitHub Packages at https://maven.pkg.github.com/catuns-spring-boot/spring-boot-base
- **License**: Apache License 2.0
- **Requires Java 21** and Spring Boot 3.5.6

## Notable Implementation Details

1. **Database Detection**: Uses Spring Boot's `DatabaseDriver` detection mechanism wrapped in custom conditions
2. **Regex Parsing**: Each database strategy uses regex to extract constraint name/field from error messages (database-specific)
3. **Field Name Strategies**: Converts database field names to readable formats (snake_case → Title Case)
4. **Fallback Behavior**: Generic strategy used when specific database strategy doesn't match
5. **Zero-Config**: Auto-configuration only requires the starter dependency; applications need no explicit bean definitions

## Deprecations and TODOs

- `exposeTechnicalDetails` property in `ConstraintViolationProperties` marked for removal in 2.0.0
- Watch for configuration property typo: `app.exeption` (missing 'c') vs correct `app.exception`
