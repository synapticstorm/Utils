package com.synstorm.common.Utils.Annotations.Methods;

import java.lang.annotation.*;

@Inherited
@Target(value= ElementType.METHOD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface UpdateStateApply {
}
