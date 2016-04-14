package ar.integration.actuator;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import ar.integration.AbstractIntegrationTests;

import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the root endpoint of the Spring Boot Actuator (/admin).
 * 
 * @author adam
 *
 */
public class ActuatorRootEndpointTests extends AbstractIntegrationTests {

  ////////////////////
  // Initialization //
  ////////////////////

  /** The path of the Actuator's root endpoint. */
  private String actuatorRootPath;

  /** The full URL of the Actuator's root endpoint. */
  private String actuatorRootUrl;

  @Override
  @Before
  public void before() {
    super.before();
    actuatorRootPath = "/admin";
    actuatorRootUrl = rootUrl + actuatorRootPath;
  }

  //////////////////////////////////
  // Actuator Root Endpoint Tests //
  //////////////////////////////////

  @Test
  public void admin_DELETE() {
    lastResponse = given().delete(actuatorRootUrl);
    assertAdminResponse();
  }

  @Test
  public void admin_GET() {
    lastResponse = given().get(actuatorRootUrl);
    assertAdminResponse();
  }

  @Test
  public void admin_GET_notAcceptable() {
    lastResponse = given().accept(ContentType.XML).get(actuatorRootUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void admin_HEAD() {
    lastResponse = given().head(actuatorRootUrl);
    assertNoContentResponse();
  }

  @Test
  public void admin_PATCH() {
    lastResponse = given().patch(actuatorRootUrl);
    assertAdminResponse();
  }

  @Test
  public void admin_POST() {
    lastResponse = given().post(actuatorRootUrl);
    assertAdminResponse();
  }

  @Test
  public void admin_PUT() {
    lastResponse = given().put(actuatorRootUrl);
    assertAdminResponse();
  }

  ///////////////////////
  // Assertion Helpers //
  ///////////////////////

  private void assertAdminResponse() {
    assertOkResponse();
    lastResponse.then()
        .body("_links.autoconfig.href", equalTo(rootUrl + actuatorRootPath + "/autoconfig"))
        .body("_links.beans.href", equalTo(rootUrl + actuatorRootPath + "/beans"))
        .body("_links.configprops.href", equalTo(rootUrl + actuatorRootPath + "/configprops"))
        .body("_links.dump.href", equalTo(rootUrl + actuatorRootPath + "/dump"))
        .body("_links.env.href", equalTo(rootUrl + actuatorRootPath + "/env"))
        .body("_links.health.href", equalTo(rootUrl + actuatorRootPath + "/health"))
        .body("_links.info.href", equalTo(rootUrl + actuatorRootPath + "/info"))
        .body("_links.mappings.href", equalTo(rootUrl + actuatorRootPath + "/mappings"))
        .body("_links.metrics.href", equalTo(rootUrl + actuatorRootPath + "/metrics"))
        .body("_links.self.href", equalTo(actuatorRootUrl))
        .body("_links.trace.href", equalTo(rootUrl + actuatorRootPath + "/trace"));
  }

}
