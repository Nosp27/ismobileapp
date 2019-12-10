package com.example.ismobileapp.network.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Consumer;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONField {
    String processResultMethod() default "";
}
