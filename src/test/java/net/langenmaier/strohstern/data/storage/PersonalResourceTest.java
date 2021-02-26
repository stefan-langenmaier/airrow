package net.langenmaier.strohstern.data.storage;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import net.langenmaier.strohstern.data.storage.resource.PersonalResource;
import net.langenmaier.strohstern.data.storage.resources.Elasticsearch;

@QuarkusTest
@QuarkusTestResource(Elasticsearch.class)
@TestHTTPEndpoint(PersonalResource.class)
public class PersonalResourceTest {

    @Test
    public void testPersonalEmpty() {
        given()
            .cookie("sessionId", "466a73a0-0d8b-46cc-ac9e-9e719bd9ffff")
            .when().get()
            .then()
                .statusCode(200);
    }

    // more tests are needed, figure out how to order it and add data per test case

}
