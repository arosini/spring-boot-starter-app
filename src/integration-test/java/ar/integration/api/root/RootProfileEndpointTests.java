package ar.integration.api.root;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import ar.integration.AbstractIntegrationTests;

import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 * Tests for the root's profile endpoint (/profile).
 * 
 * @author adam
 *
 */
public class RootProfileEndpointTests extends AbstractIntegrationTests {

  ////////////////////
  // Initialization //
  ////////////////////

  /** The path of the root's profile. */
  private String rootProfilePath;

  /** The complete URL of the root's profile. */
  private String rootProfileUrl;

  @Override
  @Before
  public void before() {
    super.before();
    rootProfilePath = "/profile";
    rootProfileUrl = rootUrl + rootProfilePath;
  }

  /////////////////////////////////
  // Root Profile Endpoint Tests //
  /////////////////////////////////

  @Test
  public void rootProfile_DELETE() {
    lastResponse = given().delete(rootProfileUrl);
    assertMethodNotAllowedResponse(HttpMethod.DELETE, rootProfilePath);
  }

  @Test
  public void rootProfile_GET() {
    lastResponse = given().get(rootProfileUrl);
    assertOkResponse();
    lastResponse.then()
        .body("_links.self.href", equalTo(rootProfileUrl))
        .body("_links.users.href", equalTo(rootUrl + "/profile/users"));
  }

  @Test
  public void rootProfile_GET_notAcceptable() {
    lastResponse = given().accept(ContentType.XML).get(rootProfileUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void rootProfile_HEAD() {
    lastResponse = given().head(rootProfileUrl);
    assertNoContentResponse();
  }

  @Test
  public void rootProfile_PATCH() {
    lastResponse = given().patch(rootProfileUrl);
    assertMethodNotAllowedResponse(HttpMethod.PATCH, rootProfilePath);
  }

  @Test
  public void rootProfile_POST() {
    lastResponse = given().post(rootProfileUrl);
    assertMethodNotAllowedResponse(HttpMethod.POST, rootProfilePath);
  }

  @Test
  public void rootProfile_PUT() {
    lastResponse = given().put(rootProfileUrl);
    assertMethodNotAllowedResponse(HttpMethod.PUT, rootProfilePath);
  }

}
