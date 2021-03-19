package org.acme.phone.sector.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * Phone number data.
 *
 * @author Jose Monteiro (j.pedroteixeira.monteiro@gmail.com)
 * @since 1.0.0
 */
@Value.Immutable
@JsonSerialize(as = ImmutablePhoneData.class)
@JsonDeserialize(as = ImmutablePhoneData.class)
public abstract class PhoneData {
    /**
     * Phone number.
     *
     * @return the phone number
     */
    public abstract String number();

    /**
     * Phone prefix.
     *
     * @return the phone prefix
     */
    public abstract String prefix();

    /**
     * Phone sector.
     *
     * @return the phone sector
     */
    public abstract String sector();
}
