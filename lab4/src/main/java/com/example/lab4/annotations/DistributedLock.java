package com.example.lab4.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * The base key prefix for the lock in Redis.
     * A unique identifier (like an entity ID) will usually be appended.
     */
    String keyPrefix();

    /**
     * SpEL expression to extract the unique identifier part of the lock key
     * from the method arguments. Example: "#id" or "#roomDTO.roomId".
     * The argument names must match the parameter names in the method signature.
     */
    String keyIdentifierExpression();

    /**
     * Lock lease duration. How long the lock is held before automatically expiring
     * if the holder crashes. Prevents deadlocks.
     */
    long leaseTime() default 30; // Default 30 seconds

    /**
     * Time unit for the lease time.
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}