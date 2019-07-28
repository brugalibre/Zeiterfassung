/**
 * 
 */
package com.myownb3.dominic.ui.core.view.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Class which is annotated with this annotation declares that it contains
 * Java-FX-content such as FXML or CSS files which are located in the same
 * folder as the page
 * 
 * @author Dominic Stalder
 * @since rel2.00.00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface FXContent {

    /**
     * Returns the name of the fxml-file of this Page
     * 
     * @return the name of the fxml-file of this Page
     */
    String fxmlFileName() default "";
}
