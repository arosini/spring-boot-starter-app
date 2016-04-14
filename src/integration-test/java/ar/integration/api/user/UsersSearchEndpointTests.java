package ar.integration.api.user;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

public class UsersSearchEndpointTests extends AbstractUserIntegrationTests {

  ////////////////////
  // Initialization //
  ////////////////////

  private String usersSearchPath;
  private String usersSearchUrl;

  @Override
  @Before
  public void before() {
    super.before();
    usersSearchPath = "/users/search";
    usersSearchUrl = rootUrl + usersSearchPath;
  }

  ////////////////////////////
  // Users Search URL Tests //
  ////////////////////////////

  @Test
  public void usersSearch_DELETE() {
    lastResponse = given().delete(usersSearchUrl);
    assertMethodNotAllowedResponse(HttpMethod.DELETE, usersSearchPath);
  }

  @Test
  public void usersSearch_GET() {
    lastResponse = given().get(usersSearchUrl);
    assertOkResponse();
    lastResponse.then()
        .body("_links.findByLastName.href", equalTo(rootUrl + "/users/search/findByLastName{?lastName}"))
        .body("_links.findByLastName.templated", equalTo(true))
        .body("_links.self.href", equalTo(usersSearchUrl));
  }

  @Test
  public void usersSearch_GET_notAcceptable() {
    lastResponse = given().accept(ContentType.XML).get(usersSearchUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void usersSearch_HEAD() {
    lastResponse = given().head(usersSearchUrl);
    assertNoContentResponse();
  }

  @Test
  public void usersSearch_PATCH() {
    lastResponse = given().patch(usersSearchUrl);
    assertMethodNotAllowedResponse(HttpMethod.PATCH, usersSearchPath);
  }

  @Test
  public void usersSearch_POST() {
    lastResponse = given().post(usersSearchUrl);
    assertMethodNotAllowedResponse(HttpMethod.POST, usersSearchPath);
  }

  @Test
  public void usersSearch_PUT() {
    lastResponse = given().put(usersSearchUrl);
    assertMethodNotAllowedResponse(HttpMethod.PUT, usersSearchPath);
  }

}
