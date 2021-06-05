package net.langenmaier.airrow.backend.app;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import net.langenmaier.airrow.backend.app.resource.RefreshResource;
import net.langenmaier.airrow.backend.app.resources.Elasticsearch;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import static net.javacrumbs.jsonunit.JsonMatchers.*;

@QuarkusTest
@QuarkusTestResource(Elasticsearch.class)
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

    @Test
    public void testRefreshFound() {
        given()
            .contentType("application/json")
            .body("{\"uuid\":\"466a73a0-0d8b-46cc-ac9e-9e719bd9ffff\",\"location\":{\"lat\":48.33197,\"lon\":10.86655},\"status\":\"\",\"accuracy\":10}")
            .when().post()
            .then()
                .statusCode(200)
                .body(jsonEquals("{\"angle\":126.20299829986558,\"geo_distance\":6.1111539086248685,\"target\":{\"refCode\":\"r01\",\"status\":\"\uD83C\uDF7A\uD83C\uDF3D\",\"mimeType\":null,\"fileHash\":null},\"searchState\":\"FOUND\",\"capability\":{\"canUpload\":false}}"));
    }

    @Test
    public void testRefreshNotFound() {
        given()
            .contentType("application/json")
            .body("{\"uuid\":\"466a73a0-0d8b-46cc-ac9e-9e719bd9ffff\",\"location\":{\"lat\":48.3,\"lon\":10.8},\"status\":\"\",\"accuracy\":10}")
            .when().post()
            .then()
                .statusCode(200)
                .body(jsonEquals("{\"angle\":38.940010179065325,\"geo_distance\":4476.19948922886,\"target\":{\"refCode\":null,\"status\":\"\uD83E\uDE91\uD83D\uDCBA\",\"mimeType\":null,\"fileHash\":null},\"searchState\":\"SEARCHING\",\"capability\":{\"canUpload\":false}}"));
    }

    /*
     * Currently not working, as the seems to be a problem with multiple QuarkusTestResources
     * even with test profiles
    */
    // @Test
    // @Ignore
    // public void testRefreshEmptyData() {
    //     given()
    //         .contentType("application/json")
    //         .body("{\"uuid\":\"466a73a0-0d8b-46cc-ac9e-9e719bd9ffff\",\"location\":{\"lat\":48.3,\"lon\":10.8},\"status\":\"\",\"accuracy\":10}")
    //         .when().post()
    //         .then()
    //             .statusCode(204)
    //             .body(is(""));
    // }

}
