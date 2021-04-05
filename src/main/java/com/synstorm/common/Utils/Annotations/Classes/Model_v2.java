package com.synstorm.common.Utils.Annotations.Classes;

import java.lang.annotation.*;

/**
 * Created by dvbozhko on 2019-09-12.
 */


/**
 * A class annotated with Model_v2 claims the actual code of BCNNM Platform.
 */

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface Model_v2 {
    String value() default "Model_v1";
    Class<? extends Exception> exception() default Exception.class;
}
