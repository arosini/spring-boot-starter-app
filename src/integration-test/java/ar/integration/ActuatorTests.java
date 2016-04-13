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

  ////////////////////
  // Initialization //
  ////////////////////

  /** The path of the actuator's admin endpoint. */
  private String adminPath;

  /** The full URL of the actuator's admin endpoint. */
  private String adminUrl;

  /** The path of the actuator's health endpoint. */
  private String healthPath;

  /** The full URL of the actuator's health endpoint. */
  private String healthUrl;

  /** The String value representing a service is up in the health endpoint. */
  private String up;

  @Override
  @Before
  public void before() {
    super.before();

    adminPath = "/admin";
    adminUrl = rootUrl + adminPath;

    healthPath = adminPath + "/health";
    healthUrl = rootUrl + healthPath;

    up = "UP";
  }

  /////////////////////
  // Admin URL Tests //
  /////////////////////

  @Test
  public void admin_DELETE() {
    lastResponse = given().delete(adminUrl);
    assertAdminResponse();
  }

  @Test
  public void admin_GET() {
    lastResponse = given().get(adminUrl);
    assertAdminResponse();
  }

  @Test
  public void admin_GET_notAcceptable() {
    lastResponse = given().accept(ContentType.XML).get(adminUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void admin_HEAD() {
    lastResponse = given().head(adminUrl);
    assertNoContentResponse();
  }

  @Test
  public void admin_PATCH() {
    lastResponse = given().patch(adminUrl);
    assertAdminResponse();
  }

  @Test
  public void admin_POST() {
    lastResponse = given().post(adminUrl);
    assertAdminResponse();
  }

  @Test
  public void admin_PUT() {
    lastResponse = given().put(adminUrl);
    assertAdminResponse();
  }

  //////////////////////
  // Health URL Tests //
  //////////////////////

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

  private void assertAdminResponse() {
    assertOkResponse();
    lastResponse.then()
        .body("_links.autoconfig.href", equalTo(rootUrl + adminPath + "/autoconfig"))
        .body("_links.beans.href", equalTo(rootUrl + adminPath + "/beans"))
        .body("_links.configprops.href", equalTo(rootUrl + adminPath + "/configprops"))
        .body("_links.dump.href", equalTo(rootUrl + adminPath + "/dump"))
        .body("_links.env.href", equalTo(rootUrl + adminPath + "/env"))
        .body("_links.health.href", equalTo(healthUrl))
        .body("_links.info.href", equalTo(rootUrl + adminPath + "/info"))
        .body("_links.mappings.href", equalTo(rootUrl + adminPath + "/mappings"))
        .body("_links.metrics.href", equalTo(rootUrl + adminPath + "/metrics"))
        .body("_links.self.href", equalTo(adminUrl))
        .body("_links.trace.href", equalTo(rootUrl + adminPath + "/trace"));
  }

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
