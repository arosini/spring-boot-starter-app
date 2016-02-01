package ar.integration;

import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;

public class ActuatorTests extends AbstractIntegrationTests {

  @Test
  public void healthEndpoint() {
    response = given().accept(ContentType.JSON).get("/admin/health");
    assertOkResponse();
  }

}
