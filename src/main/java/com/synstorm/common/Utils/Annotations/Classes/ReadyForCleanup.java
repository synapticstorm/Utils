package com.synstorm.common.Utils.Annotations.Classes;

import java.lang.annotation.*;

/**
 * Created by human-research on 2019-06-03.
 */


/**
 * Annotates legacy code ready for cleanup.
 * Use it for planning future work.
 */

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface ReadyForCleanup {
    String value() default "ReadyForCleanup";
    Class<? extends Exception> exception() default Exception.class;
}