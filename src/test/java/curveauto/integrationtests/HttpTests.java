package curveauto.integrationtests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import curveauto.*;
import org.junit.*;
import ratpack.http.client.ReceivedResponse;
import ratpack.jackson.internal.JsonParser;
import ratpack.test.ServerBackedApplicationUnderTest;
import ratpack.test.http.TestHttpClient;

/**
 * This set of integration tests ensures that our basic list/get/post/put/delete api's are working correctly.  They
 * test over the MaintenanceType api's because they are the simplest.
 */
public class HttpTests {
    private static DataAccessFactory factory;
    private static HttpServer server;
    private static API api;

    @BeforeClass
    public static void setup() throws Exception {
        factory = new HibernateDataAccessFactory();
        api = new API();
        server = new HttpServer(factory, api, 0);
    }

    @AfterClass
    public static void cleanup() throws Exception {
        server.stop();
        factory = null;
    }

    @Test
    public void testList() {
        String response = TestHttpClient.testHttpClient(ServerBackedApplicationUnderTest.of(server.getServer()))
                .get("api/maintenanceTypes").getBody().getText();
        Assert.assertEquals("[{\"id\":1,\"name\":\"Oil Change\"},{\"id\":2,\"name\":\"Tire Rotation\"}]", response);
    }

    @Test
    public void testGet() {
        String response = TestHttpClient.testHttpClient(ServerBackedApplicationUnderTest.of(server.getServer()))
                .get("api/maintenanceTypes/1").getBody().getText();
        Assert.assertEquals("{\"id\":1,\"name\":\"Oil Change\"}", response);
    }

    @Test
    public void testGet404() {
        int response = TestHttpClient.testHttpClient(ServerBackedApplicationUnderTest.of(server.getServer()))
                .get("api/maintenanceTypes/3413434").getStatusCode();
        Assert.assertEquals(404, response);
    }

    @Test
    public void testUnmapped404() {
        int responseCode = TestHttpClient.testHttpClient(ServerBackedApplicationUnderTest.of(server.getServer()))
                .get("api/thisDoesNotExist").getStatusCode();
        Assert.assertEquals(404, responseCode);
    }

    @Test
    public void testGetAndPost() throws Exception {
        String currentValue = TestHttpClient.testHttpClient(ServerBackedApplicationUnderTest.of(server.getServer()))
                .get("api/maintenanceTypes/1").getBody().getText();


        // Change the value to "Hello World"
        TestHttpClient.testHttpClient(ServerBackedApplicationUnderTest.of(server.getServer()))
                .request("api/maintenanceTypes/1", r -> r
                        .body(b -> b.text("{\"id\":1,\"name\":\"Hello World\"}"))
                        .method("POST")
                        .headers(h -> h.set("Content-Type", "application/json"))
                )
                .getBody().getText();

        ReceivedResponse getResponse = TestHttpClient.testHttpClient(ServerBackedApplicationUnderTest.of(server.getServer()))
                .get("api/maintenanceTypes/1");
        Assert.assertEquals("{\"id\":1,\"name\":\"Hello World\"}", getResponse.getBody().getText());

        // Reset the value back
        TestHttpClient.testHttpClient(ServerBackedApplicationUnderTest.of(server.getServer()))
                .request("api/maintenanceTypes/1", r -> r
                        .body(b -> b.text(currentValue))
                        .method("POST")
                        .headers(h -> h.set("Content-Type", "application/json"))
                )
                .getBody().getText();
    }

    @Test
    public void testPutGetAndDelete() throws Exception {
        ReceivedResponse putResponse = TestHttpClient.testHttpClient(ServerBackedApplicationUnderTest.of(server.getServer()))
                .request("api/maintenanceTypes", r -> r
                        .body(b -> b.text("{\"name\":\"Test\"}"))
                        .method("PUT")
                        .headers(h -> h.set("Content-Type", "application/json"))
                );

        Assert.assertEquals(200, putResponse.getStatusCode());
        String putResponseBody = putResponse.getBody().getText();


        long id = JsonUtils.toJsonNode(putResponseBody).get("id").asLong();

        ReceivedResponse getResponse = TestHttpClient.testHttpClient(ServerBackedApplicationUnderTest.of(server.getServer()))
                .get("api/maintenanceTypes/" + id);
        Assert.assertEquals(200, getResponse.getStatusCode());
        Assert.assertEquals(putResponseBody, getResponse.getBody().getText());

        TestHttpClient.testHttpClient(ServerBackedApplicationUnderTest.of(server.getServer()))
                .deleteText("api/maintenanceTypes/" + id);

    }

}
