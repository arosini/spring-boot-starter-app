package ar.integration.api.user;

import static com.jayway.restassured.RestAssured.given;

import ar.entity.User;

import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 * Tests for {@link User} resource endpoints.
 * 
 * @author adam
 *
 */
public class UsersSearchByLastNameEndpointTests extends AbstractUserIntegrationTests {

  private String usersSearchByLastNamePath;
  private String usersSearchByLastNameUrl;

  ////////////////////
  // Initialization //
  ////////////////////

  @Override
  @Before
  public void before() {
    super.before();
    usersSearchByLastNameUrl = rootUrl + "/users/search/findByLastName";
  }

  /////////////////////////////////////////
  // Users Search By Last Name URL Tests //
  /////////////////////////////////////////

  @Test
  public void usersSearchByLastName_DELETE() {
    lastResponse = given().delete(usersSearchByLastNameUrl);
    assertMethodNotAllowedResponse(HttpMethod.DELETE, usersSearchByLastNamePath);
  }

  @Test
  public void usersSearchByLastName_GET() {
    lastResponse = given().get(usersSearchByLastNameUrl);
    assertSearchResponse(usersSearchByLastNameUrl, 0);
  }

  @Test
  public void usersSearchByLastName_GET_singleResult() {
    String searchUrl = usersSearchByLastNameUrl + "?lastName=Xavier";

    lastResponse = given().accept(ContentType.JSON).get(searchUrl);
    assertSearchResponse(searchUrl, 1);
    assertUserInResponse(0, "1", "Charles", "Xavier", "Professor X");
  }

  @Test
  public void usersSearchByLastName_GET_multipleResults() {
    String searchUrl = usersSearchByLastNameUrl + "?lastName=Summers";

    lastResponse = given().accept(ContentType.JSON).get(searchUrl);
    assertSearchResponse(searchUrl, 2);
    assertUserInResponse(0, "2", "Scott", "Summers", "Cyclops");
    assertUserInResponse(1, "3", "Alex", "Summers", "Havok");
  }

  @Test
  public void usersSearchByLastName_GET_noResults() {
    String searchUrl = usersSearchByLastNameUrl + "?lastName=Lensherr";

    lastResponse = given().accept(ContentType.JSON).get(searchUrl);
    assertSearchResponse(searchUrl, 0);
  }

  @Test
  public void usersSearchByLastName_GET_notAcceptable() {
    lastResponse = given().accept(ContentType.XML).get(usersSearchByLastNameUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void usersSearchByLastName_HEAD() {
    lastResponse = given().head(usersSearchByLastNameUrl);
    assertNoContentResponse();
  }

  @Test
  public void usersSearchByLastName_PATCH() {
    lastResponse = given().patch(usersSearchByLastNameUrl);
    assertMethodNotAllowedResponse(HttpMethod.PATCH, usersSearchByLastNamePath);
  }

  @Test
  public void usersSearchByLastName_POST() {
    lastResponse = given().post(usersSearchByLastNameUrl);
    assertMethodNotAllowedResponse(HttpMethod.POST, usersSearchByLastNamePath);
  }

  @Test
  public void usersSearchByLastName_PUT() {
    lastResponse = given().put(usersSearchByLastNameUrl);
    assertMethodNotAllowedResponse(HttpMethod.PUT, usersSearchByLastNamePath);
  }

}
