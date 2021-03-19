package org.acme.phone.sector.api.analyzer;

import org.acme.phone.sector.cache.local.PrefixesCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Phone number analyzer. Responsible to analyze and validate phone numbers received.
 *
 * @author Jose Monteiro (j.pedroteixeira.monteiro@gmail.com)
 * @since 1.0.0
 */
@Singleton
public class PhoneNumberAnalyzer {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneNumberAnalyzer.class);
    /**
     * Phone number regex expression. Checks if the first character is a optional "+" or if the first two characters
     * are "00", followed by exactly 3 digits or more than 6 and less than 13 digits. Allows whitespaces
     * except after the leading match ("+" or "00).
     */
    private static final String REGEX_PHONE_NUMBER = "^(?<leading>([+]|[0]{2}))?(\\d)(( *?\\d){2}|( *?\\d){6,11})$";
    /**
     * Leading symbol used in phone numbers prefix.
     */
    private static final String LEADING_PHONE_NUMBER_SYMBOL = "+";

    /**
     * Pattern to validate if phone number matches regex.
     */
    private final Pattern pattern = Pattern.compile(REGEX_PHONE_NUMBER);
    /**
     * Valid prefixes cache.
     */
    @Inject
    private PrefixesCache prefixesCache;

    /**
     * Get a phone prefix. Validates the number and if valid, extracts the prefix, otherwise it returns null.
     *
     * @param number the number to get prefix
     * @return the number prefix
     */
    public String getPhonePrefix(final String number) {
        final Matcher matcher = pattern.matcher(number);
        if (!matcher.matches()) {
            LOGGER.error("Phone number {} is not valid.", number);
            return null;
        }

        StringBuilder buffer = new StringBuilder();
        int i = getPhoneIndex(matcher.group("leading"));
        for (; i < number.length(); i++) {
            final char digit = number.charAt(i);
            if (!Character.isWhitespace(digit)) {
                buffer.append(number.charAt(i));
                final String prefix = buffer.toString();

                if (prefixesCache.exists(prefix)) {
                    return prefix;
                }
            }
        }

        LOGGER.error("Could not find number prefix.");
        return null;
    }

    /**
     * Get index to start iteration over phone number. In case a leading was provided ("+" or "00"), it starts with index
     * 1 or 2, respectively. If a leading was not provided, it starts on index with index 0.
     *
     * @param leading the leading extracted in the number
     * @return the index to start the number iteration
     */
    private int getPhoneIndex(final String leading) {
        if (leading != null) {
            return leading.equals(LEADING_PHONE_NUMBER_SYMBOL) ? 1 : 2;
        }

        return 0;
    }
}
