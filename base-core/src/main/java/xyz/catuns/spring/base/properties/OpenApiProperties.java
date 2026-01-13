package xyz.catuns.spring.base.properties;

import lombok.Data;

/*
 * Open Api Properties
 * @author Devin Catuns
 */
@Data
public class OpenApiProperties {
    /*
     * Open Api Title
     */
    private String title;
    /*
     * Open Api Version
     */
    private String version;
    /*
     * Open Api Email
     */
    private String email;
    /*
     * Open Api Description
     */
    private String description;
    /*
     * Open Api url
     */
    private String url;
}
