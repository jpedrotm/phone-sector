package org.acme.phone.sector.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * Phone sector response payload.
 *
 * @author Jose Monteiro (j.pedroteixeira.monteiro@gmail.com)
 * @since 1.0.0
 */
@Value.Immutable
@JsonSerialize(as = ImmutablePhoneSectorResponse.class)
@JsonDeserialize(as = ImmutablePhoneSectorResponse.class)
public abstract class PhoneSectorResponse {
    /**
     * Number.
     *
     * @return number.
     */
    public abstract String number();

    /**
     * Sector.
     *
     * @return sector.
     */
    public abstract String sector();
}
