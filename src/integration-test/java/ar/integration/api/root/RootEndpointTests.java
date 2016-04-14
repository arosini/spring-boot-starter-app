package ar.integration.api.root;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import ar.integration.AbstractIntegrationTests;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 * Tests for the root endpoint (/).
 * 
 * @author adam
 *
 */
public class RootEndpointTests extends AbstractIntegrationTests {

  /////////////////////////
  // Root Endpoint Tests //
  /////////////////////////

  @Test
  public void root_DELETE() {
    lastResponse = given().delete(rootUrl);
    assertMethodNotAllowedResponse(HttpMethod.DELETE, rootPath);
  }

  @Test
  public void root_GET() {
    lastResponse = given().get(rootUrl);
    assertOkResponse();
    lastResponse.then()
        .body("_links.profile.href", equalTo(rootUrl + "/profile"))
        .body("_links.users.href", equalTo(rootUrl + "/users{?page,size,sort}"))
        .body("_links.users.templated", equalTo(true));
  }

  @Test
  public void root_GET_notAcceptable() {
    lastResponse = given().accept(ContentType.XML).get(rootUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void root_HEAD() {
    lastResponse = given().head(rootUrl);
    assertNoContentResponse();
  }

  @Test
  public void root_PATCH() {
    lastResponse = given().patch(rootUrl);
    assertMethodNotAllowedResponse(HttpMethod.PATCH, rootPath);
  }

  @Test
  public void root_POST() {
    lastResponse = given().post(rootUrl);
    assertMethodNotAllowedResponse(HttpMethod.POST, rootPath);
  }

  @Test
  public void root_PUT() {
    lastResponse = given().put(rootUrl);
    assertMethodNotAllowedResponse(HttpMethod.PUT, rootPath);
  }
}
