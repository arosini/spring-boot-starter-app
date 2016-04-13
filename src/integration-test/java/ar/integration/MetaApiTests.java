package ar.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 * Tests API endpoints which are not related to a specific resource.
 * 
 * @author adam
 *
 */
public class MetaApiTests extends AbstractIntegrationTests {

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

  ////////////////////
  // Root URL Tests //
  ////////////////////

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
        .body("_links.profile.href", equalTo(rootProfileUrl))
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

  ////////////////////////////
  // Root Profile URL Tests //
  ////////////////////////////

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
