package xyz.catuns.spring.base.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.catuns.spring.base.exception.handler.GlobalExceptionHandler;
import xyz.catuns.spring.base.properties.ExceptionHandlerProperties;


/**
 * Auto-configuration for exception handling.
 * Only activates when:
 * - Spring Web is on the classpath
 * - Application is a web application
 * - Property is enabled (default: true)
 */
@AutoConfiguration
@ConditionalOnClass({RestControllerAdvice.class, ProblemDetail.class})  // Only if Spring Web present
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)  // Only for servlet web apps
@ConditionalOnProperty(
        prefix = "app.exception",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(ExceptionHandlerProperties.class)
public class ExceptionHandlerAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler baseGlobalExceptionHandler(ExceptionHandlerProperties properties) {
        log.debug("Registering Global Exception Handler");
        return new GlobalExceptionHandler(properties);
    }
}
