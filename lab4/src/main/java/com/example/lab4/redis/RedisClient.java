package com.example.lab4.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisClient {

    private final StringRedisTemplate redisTemplate;
    private final ValueOperations<String, String> valueOps;

    @Autowired
    public RedisClient(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    /**
     * Sets a value in Redis with an optional expiration time.
     *
     * @param key      The key to set.
     * @param value    The value to set.
     * @param timeout  Optional duration after which the key should expire.
     */
    public void set(String key, String value, Duration timeout) {
        if (timeout != null) {
            valueOps.set(key, value, timeout);
        } else {
            valueOps.set(key, value);
        }
    }

    /**
     * Sets a value in Redis without expiration.
     *
     * @param key   The key to set.
     * @param value The value to set.
     */
    public void set(String key, String value) {
        this.set(key, value, null);
    }

    /**
     * Gets a value from Redis.
     *
     * @param key The key to retrieve.
     * @return The value associated with the key, or null if the key doesn't exist.
     */
    public String get(String key) {
        return valueOps.get(key);
    }

    /**
     * Deletes a key from Redis.
     *
     * @param key The key to delete.
     * @return true if the key was deleted, false otherwise.
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
    
    /**
     * Sets the value of a key, only if the key does not exist.
     * Useful for basic locking mechanisms.
     *
     * @param key     The key to set.
     * @param value   The value to set.
     * @param timeout Optional duration after which the key should expire if set.
     * @return true if the key was set, false otherwi   se (key already exists).
     */
    public Boolean setIfAbsent(String key, String value, Duration timeout) {
        Boolean success = valueOps.setIfAbsent(key, value);
        if (Boolean.TRUE.equals(success) && timeout != null) {
            // Set expiration only if the key was successfully set
            redisTemplate.expire(key, timeout);
        }
        return success;
    }

     /**
     * Sets the value of a key, only if the key does not exist (without timeout).
     *
     * @param key   The key to set.
     * @param value The value to set.
     * @return true if the key was set, false otherwise.
     */
    public Boolean setIfAbsent(String key, String value) {
         return setIfAbsent(key, value, null);
    }

            /**
         * Increments the integer value of a key by one.
         * If the key does not exist, it is set to 0 before performing the operation.
         *
         * @param key The key to increment.
         * @return The value of key after the increment.
         */
        public Long increment(String key) {
            return valueOps.increment(key);
        }

         /**
         * Sets the expiration time for a key.
         *
         * @param key     The key to set expiration for.
         * @param timeout The duration until expiration.
         * @return true if the timeout was set, false otherwise (e.g., key doesn't exist).
         */
        public Boolean expire(String key, Duration timeout) {
            return redisTemplate.expire(key, timeout);
        }


}