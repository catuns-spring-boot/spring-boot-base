package xyz.catuns.spring.base.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import xyz.catuns.spring.base.autoconfigure.properties.BaseConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(BaseConfigurationProperties.class)
public class BaseAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BaseAutoConfiguration.class);

    public BaseAutoConfiguration() {
        log.debug("Base Auto-Configuration initialized");
    }
}
