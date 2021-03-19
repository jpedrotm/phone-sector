package org.acme.phone.sector.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.acme.phone.sector.api.analyzer.PhoneNumberAnalyzer;
import org.acme.phone.sector.cache.redis.PhoneRedisService;
import org.acme.phone.sector.model.ImmutablePhoneData;
import org.acme.phone.sector.model.PhoneData;
import org.acme.phone.sector.model.PhoneSectorResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Phone resource API to handle requests.
 *
 * @author Jose Monteiro (j.pedroteixeira.monteiro@gmail.com)
 * @since 1.0.0
 */
@Path("/aggregate")
public class PhoneResource {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneResource.class);
    /**
     * Rest client to external phone service.
     */
    @Inject
    @RestClient
    private PhoneService phoneService;

    /**
     * Phone redis client to cache requests performed with {@link PhoneService}.
     */
    @Inject
    private PhoneRedisService phoneRedisService;

    /**
     * Phone number analyzer.
     */
    @Inject
    private PhoneNumberAnalyzer analyzer;

    /**
     * Aggregates phone numbers given a list of phone numbers obtained from user input and returns the
     * count of valid phones broken down per prefix and per business sector.
     *
     * @param numbers numbers to aggregate
     * @return count of valid {@code numbers} broken down per prefix and per business sector
     */
    @POST
    public Map<String, Map<String, Integer>> aggregate(final List<String> numbers) {
        try {
            final List<PhoneData> phonesData = getPhonesData(numbers);
            return aggregatePhonesData(phonesData);
        } catch (final Exception e) {
            LOGGER.error("Failed to aggregate phone numbers.", e);
        }

        return Collections.emptyMap();
    }

    /**
     * Get {@link PhoneData} for each phone number provided. For each number it validates the number initially,
     * if the number is not valid it proceeds to the next number, otherwise it checks if the number was
     * previously requested to the external api (checks if exists in redis). If the number exists then it used
     * uses data stored and proceeds, otherwise it requests the number data to the external api.
     *
     * @param numbers number to get {@link PhoneData}
     * @return phones data
     * @throws JsonProcessingException when redis interaction fails
     */
    private List<PhoneData> getPhonesData(final List<String> numbers) throws JsonProcessingException {
        final List<PhoneData> phonesData = new ArrayList<>();
        for (final String number : numbers) {
            final String prefix = analyzer.getPhonePrefix(number);
            if (prefix == null) {
                continue;
            }

            if (phoneRedisService.phoneExists(number)) {
                phonesData.add(phoneRedisService.getPhone(number));
                continue;
            }

            final PhoneSectorResponse response = handleGetSectorRequest(number);
            if (response != null) {
                final PhoneData phoneData = ImmutablePhoneData.builder()
                        .number(number)
                        .prefix(prefix)
                        .sector(response.sector())
                        .build();

                phoneRedisService.setPhone(phoneData);
                phonesData.add(phoneData);
            }
        }

        return phonesData;
    }

    /**
     * Handler for {@link PhoneService#getByNumber(String)} to deal with exception during the request.
     *
     * @param number the number to request the sector
     * @return phone sector response
     */
    private PhoneSectorResponse handleGetSectorRequest(final String number) {
        try {
            return phoneService.getByNumber(number);
        } catch (final Exception e) {
            LOGGER.error("Failed request to get number {} sector.", number, e);
            return null;
        }
    }

    /**
     * Aggregates {@link PhoneData} per prefix and per business sector.
     *
     * @param phonesData phone numbers data to aggregate
     * @return phones data aggregated per prefix and per business sector
     */
    private Map<String, Map<String, Integer>> aggregatePhonesData(final List<PhoneData> phonesData) {
        final Map<String, Map<String, Integer>> response = new HashMap<>();
        for (final PhoneData phoneData : phonesData) {
            if (response.containsKey(phoneData.prefix())) {
                final Map<String, Integer> sectors = response.get(phoneData.prefix());
                if (sectors.containsKey(phoneData.sector())) {
                    sectors.merge(
                            phoneData.sector(),
                            1,
                            Integer::sum
                    );
                } else {
                    sectors.put(
                            phoneData.sector(),
                            1
                    );
                }
                continue;
            }

            final Map<String, Integer> sectors = new HashMap<String, Integer>() {{
                put(phoneData.sector(), 1);
            }};
            response.put(
                    phoneData.prefix(),
                    sectors
            );
        }

        return response;
    }
}
