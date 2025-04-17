package com.example.lab4.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD) // Apply to methods
@Retention(RetentionPolicy.RUNTIME) // Keep annotation info at runtime
public @interface RateLimit {
    long limit(); // * The maximum number of requests allowed within the specified time window.
    long duration(); // The duration of the time window.
    TimeUnit timeUnit() default TimeUnit.SECONDS; // The time unit for the duration (e.g., SECONDS, MINUTES).
    String keyPrefix() default "";
}