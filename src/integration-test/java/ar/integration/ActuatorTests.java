package ar.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for endpoints provided by the Spring Boot Actuator.
 * 
 * @author adam
 *
 */
public class ActuatorTests extends AbstractIntegrationTests {

  //////////
  // URLs //
  //////////

  private String adminUrl;
  private String healthUrl;

  ////////////
  // Values //
  ////////////

  private String up;

  @Override
  @Before
  public void before() {
    super.before();

    // URLs
    adminUrl = baseUrl + "/admin";
    healthUrl = adminUrl + "/health";

    // Values
    up = "UP";
  }

  @Test
  public void healthEndpoint() {
    lastResponse = given().accept(ContentType.JSON).get(healthUrl);
    assertOkResponse();
    lastResponse.then()
        .body("status", equalTo(up))
        .body("diskSpace.status", equalTo(up))
        .body("diskSpace.total", notNullValue())
        .body("diskSpace.free", notNullValue())
        .body("diskSpace.threshold", notNullValue())
        .body("mongo.status", equalTo(up))
        .body("mongo.version", notNullValue())
        .body("_links.self.href", equalTo(healthUrl));
  }

}
