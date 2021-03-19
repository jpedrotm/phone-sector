package org.acme.phone.sector.cache.redis;

import io.quarkus.redis.client.RedisClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;

/**
 * Redis service, responsible to interact with redis client.
 *
 * @author Jose Monteiro (j.pedroteixeira.monteiro@gmail.com)
 * @since 1.0.0
 */
@Singleton
public class RedisService {
    /**
     * Redis client.
     */
    @Inject
    private RedisClient redisClient;

    /**
     * Checks if a given key exists in redis.
     *
     * @param key key to check if exists
     * @return true if exists, false otherwise
     */
    public boolean keyExists(final String key) {
        return redisClient.exists(Collections.singletonList(key)).toBoolean();
    }

    /**
     * Get document given the key.
     *
     * @param key the redis key
     * @return the response
     */
    public String get(final String key) {
        return redisClient.get(key).toString();
    }

    /**
     * Create document given the key and value and sets to expire.
     *
     * @param key   the redis key
     * @param time  the time until expire, in milliseconds
     * @param value the value
     */
    public void setWithExpire(final String key, final String time, final String value) {
        redisClient.setex(key, time, value);
    }
}
