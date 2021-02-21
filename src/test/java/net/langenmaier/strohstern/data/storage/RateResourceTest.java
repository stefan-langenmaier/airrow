package net.langenmaier.strohstern.data.storage;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import net.langenmaier.strohstern.data.storage.resource.RateResource;
import net.langenmaier.strohstern.data.storage.resources.Elasticsearch;

@QuarkusTest
@QuarkusTestResource(Elasticsearch.class)
@TestHTTPEndpoint(RateResource.class)
public class RateResourceTest {

    @Test
    public void testRate() {
        given()
            .contentType("application/json")
            .body("{\"uuid\":\"466a73a0-0d8b-46cc-ac9e-9e719bd9ffff\",\"refCode\": \"r01\", \"status\":\"\",\"rating\":\"UP\"}")
            .when().post()
            .then()
                .statusCode(200);
    }

}
