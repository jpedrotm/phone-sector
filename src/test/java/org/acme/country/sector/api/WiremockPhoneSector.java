package org.acme.country.sector.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.common.collect.ImmutableMap;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Class to mock request to phone sector api.
 *
 * @author José Monteiro (j.pedroteixeira.monteiro@gmail.com)
 * @since 1.0.0
 */
public class WiremockPhoneSector implements QuarkusTestResourceLifecycleManager {
    /**
     * Numbers to mock phone sector api requests.
     */
    private static final Map<String, String> NUMBERS_TO_MOCK = ImmutableMap.of(
            "+1983248", "Technology",
            "+1%20%20%20%2098%20%203%20%20%20248", "Technology",
            "001382355", "Technology",
            "+147%208192", "Clothing",
            "+4%20439877", "Banking"
    );

    /**
     * Wire mock server.
     */
    private WireMockServer wireMockServer;

    /**
     * Start handler.
     *
     * @return configuration to use during tests
     */
    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        NUMBERS_TO_MOCK.forEach(this::stubNumbersRequest);

        return Collections.singletonMap("phone-sector-api/mp-rest/url", wireMockServer.baseUrl());
    }

    /**
     * Stop handler.
     */
    @Override
    public void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    /**
     * Stubs the requests to phone sector api for the phone numbers used in tests.
     *
     * @param number the number
     * @param sector the sector
     */
    private void stubNumbersRequest(final String number, final String sector) {
        stubFor(get(urlEqualTo("/sector/" + number))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "{" +
                                        "\"number\": \"" + number + "\"," +
                                        "\"sector\": \"" + sector + "\"" +
                                        "}"
                        )
                )
        );
    }
}
