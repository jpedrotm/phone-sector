package org.acme.phone.sector.api;

import org.acme.phone.sector.model.PhoneSectorResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Service handler to perform requests to external phone sector api.
 *
 * @author Jose Monteiro (j.pedroteixeira.monteiro@gmail.com)
 * @since 1.0.0
 */
@Path("/sector")
@RegisterRestClient(configKey = "phone-sector-api")
public interface PhoneService {
    /**
     * Get phone sector given the phone number.
     *
     * @param number the phone number to get the sector
     * @return phone number and sector
     */
    @GET
    @Path("/{number}")
    @Produces("application/json")
    PhoneSectorResponse getByNumber(@PathParam String number);
}
