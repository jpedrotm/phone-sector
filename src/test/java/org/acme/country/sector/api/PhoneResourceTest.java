package org.acme.country.sector.api;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/**
 * Test for {@link org.acme.phone.sector.api.PhoneResource} API, covering the validation
 * of the expected functionality.
 *
 * @author Jose Monteiro (j.pedroteixeira.monteiro@feedzai.com)
 * @since 1.0.0
 */
@QuarkusTest
@QuarkusTestResource(WiremockPhoneSector.class)
public class PhoneResourceTest {
    /**
     * Test request with all valid numbers.
     */
    @Test
    public void testValidPhoneAggregateInput() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("[\"+1983248\", \"+1    98  3   248\", \"001382355\", \"+147 8192\", \"+4 439877\"]")
                .when()
                .post("/aggregate")
                .then()
                .statusCode(200)
                .body("1.Technology", is(3))
                .body("1.Clothing", is(1))
                .body("44.Banking", is(1));
    }

    /**
     * Test request with all non-valid numbers.
     */
    @Test
    public void testInvalidPhoneAggregateInput() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                // 1st with alphabetic characters
                // 2nd with whitespace after leading match ("+" or "00")
                // 3rd with non-existent prefix
                // 4th with not allowed number count of 6 (allow exactly 3 or more than 6 and less than 13)
                // 5th with not allowed number count of 13 (allowed exactly 3 or more than 6 and less than 13)
                .body("[\"001382355A\", \"+ 147 8192\", \"+04439877\", \"+123456\", \"001234567891234\"]")
                .when()
                .post("/aggregate")
                .then()
                .statusCode(200)
                .body("isEmpty()", Matchers.is(true));
    }

    /**
     * Test request with invalid body.
     */
    @Test
    public void testInvalidArrayInput() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("[\"1983248\", \"001382355A\",")
                .when()
                .post("/aggregate")
                .then()
                .statusCode(400);
    }
}
