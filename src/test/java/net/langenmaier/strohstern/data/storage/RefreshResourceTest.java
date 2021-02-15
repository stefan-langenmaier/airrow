package net.langenmaier.strohstern.data.storage;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import net.langenmaier.strohstern.data.storage.resource.RefreshResource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestHTTPEndpoint(RefreshResource.class) 
public class RefreshResourceTest {

    @Test
    public void testRefreshEndpoint() {
        given()
            .contentType("application/json")
            .body("")
            .when().post()    
            .then()
                .statusCode(204)
                .body(is(""));
    }
}
