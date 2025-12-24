package xyz.catuns.spring.base.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.Map;

/*
 * Open Feign Properties
 * @author Devin Catuns
 */
@Getter
@Setter
public class OpenFeignProperties {

    /*
     * Open Feign client names
     */
    @NestedConfigurationProperty
    private Map<String, String> clients = new HashMap<>();

}
