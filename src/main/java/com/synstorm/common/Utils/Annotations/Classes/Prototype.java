package com.synstorm.common.Utils.Annotations.Classes;

import java.lang.annotation.*;

/**
 * Created by human-research on 2020-04-28.
 */


/**
 * A class annotated with Prototype claims the experimental functionality.
 */

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface Prototype {
    String value() default "Prototype";
    Class<? extends Exception> exception() default Exception.class;
}
