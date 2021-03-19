package org.acme.phone.sector.cache.redis;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.acme.phone.sector.model.PhoneData;

import javax.inject.Singleton;

/**
 * Redis service to manage phone data.
 *
 * @author Jose Monteiro (j.pedroteixeira.monteiro@gmail.com)
 * @since 1.0.0
 */
@Singleton
public class PhoneRedisService extends RedisService {
    /**
     * Expiration time of a phone number. Set to 1 day.
     */
    private static final String EXPIRATION_TIME = "86400";
    /**
     * Object mapper to serialize and deserialize
     */
    private final ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());

    /**
     * Checks if a given phone number exists in redis.
     *
     * @param phone phone number to check
     * @return true if exists, false otherwise
     */
    public boolean phoneExists(final String phone) {
        return this.keyExists(phone);
    }

    /**
     * Get phone data from redis.
     *
     * @param key phone number
     * @return the phone data
     */
    public PhoneData getPhone(final String key) throws JsonProcessingException {
        final String result = this.get(key);
        return objectMapper.readValue(result, PhoneData.class);
    }

    /**
     * Set phone in redis.
     *
     * @param phoneData phone data to set
     * @throws JsonProcessingException when fails to serialize
     */
    public void setPhone(final PhoneData phoneData) throws JsonProcessingException {
        final String value = objectMapper.writeValueAsString(phoneData);
        this.setWithExpire(phoneData.number(), EXPIRATION_TIME, value);
    }
}
