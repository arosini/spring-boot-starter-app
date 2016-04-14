package ar.integration.actuator;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import ar.integration.AbstractIntegrationTests;

import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the health endpoint provided by the Spring Boot Actuator (/admin/health).
 * 
 * @author adam
 *
 */
public class ActuatorHealthEndpointTests extends AbstractIntegrationTests {

  ////////////////////
  // Initialization //
  ////////////////////

  /** The path of the Actuator's health endpoint. */
  private String healthPath;

  /** The full URL of the Actuator's health endpoint. */
  private String healthUrl;

  /** The String value representing a service is up in the Actuator's health endpoint. */
  private String up;

  @Override
  @Before
  public void before() {
    super.before();
    healthPath = "/admin/health";
    healthUrl = rootUrl + healthPath;
    up = "UP";
  }

  ////////////////////////////////////
  // Actuator Health Endpoint Tests //
  ////////////////////////////////////

  @Test
  public void health_DELETE() {
    lastResponse = given().delete(healthUrl);
    assertHealthOkResponse();
  }

  @Test
  public void health_GET() {
    lastResponse = given().get(healthUrl);
    assertHealthOkResponse();
  }

  @Test
  public void health_GET_notAcceptable() {
    lastResponse = given().accept(ContentType.XML).get(healthUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void health_HEAD() {
    lastResponse = given().head(healthUrl);
    assertNoContentResponse();
  }

  @Test
  public void health_PATCH() {
    lastResponse = given().patch(healthUrl);
    assertHealthOkResponse();
  }

  @Test
  public void health_POST() {
    lastResponse = given().post(healthUrl);
    assertHealthOkResponse();
  }

  @Test
  public void health_PUT() {
    lastResponse = given().put(healthUrl);
    assertHealthOkResponse();
  }

  ///////////////////////
  // Assertion Helpers //
  ///////////////////////

  private void assertHealthOkResponse() {
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
