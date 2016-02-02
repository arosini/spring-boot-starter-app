package ar.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;

public class ActuatorTests extends AbstractIntegrationTests {

  // TODO: Refactor these to be defined in @Before and use baseUrl
  private static final String ADMIN_URL = "/admin";
  private static final String HEALTH_URL = ADMIN_URL + "/health";
  private static final String UP = "UP";

  @Test
  public void healthEndpoint() {
    response = given().accept(ContentType.JSON).get(HEALTH_URL);
    assertOkResponse();
    response.then()
        .body("status", equalTo("UP"))
        .body("diskSpace.status", equalTo(UP))
        .body("diskSpace.total", greaterThan(0))
        .body("diskSpace.free", greaterThan(0))
        .body("diskSpace.threshold", greaterThan(0))
        .body("mongo.status", equalTo(UP))
        .body("mongo.version", notNullValue())
        .body("_links.self.href", equalTo(baseUrl + HEALTH_URL));
  }

  // Move these to a new file (REST API Tests)
  //
  // @Test
  // public void checkProfile() {
  // response = given().head(profileUrl);
  // assertNoContentResponse();
  // }
  //
  // @Test
  // public void getProfile() {
  // response = given().accept(ContentType.JSON).get(profileUrl);
  // assertOkResponse();
  // }
  //
  // @Test
  // public void getProfile_notAcceptable() {
  // response = given().accept(ContentType.XML).get(profileUrl);
  // assertNotAcceptableResponse();
  // }

}
