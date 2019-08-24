package com.datasphere.engine.core.annotation;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoFilter {
    boolean updatable() default true;
    
    boolean queryable() default true;
}
