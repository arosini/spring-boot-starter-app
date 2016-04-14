package ar.integration.api.user;

import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

public class UsersSearchNonExposedEndpointTests extends AbstractUserIntegrationTests {

  ////////////////////
  // Initialization //
  ////////////////////

  private String usersSearchNonExposedFieldUrl;

  @Override
  @Before
  public void before() {
    super.before();
    usersSearchNonExposedFieldUrl = rootUrl + "/search/users/findByFirstName";
  }

  //////////////////////////////////////////////
  // Users Search Non Exposed Field URL Tests //
  //////////////////////////////////////////////

  @Test
  public void usersSearchNonExposedField_DELETE() {
    lastResponse = given().delete(usersSearchNonExposedFieldUrl);
    assertNotFoundResponse();
  }

  @Test
  public void usersSearchNonExposedField_GET() {
    lastResponse = given().get(usersSearchNonExposedFieldUrl);
    assertNotFoundResponse();
  }

  @Test
  public void usersSearchNonExposedField_GET_notAcceptable() {
    lastResponse = given().accept(ContentType.XML).get(usersSearchNonExposedFieldUrl);
    assertNotFoundResponse();
  }

  @Test
  public void usersSearchNonExposedField_HEAD() {
    lastResponse = given().head(usersSearchNonExposedFieldUrl);
    assertNotFoundResponse();
  }

  @Test
  public void usersSearchNonExposedField_PATCH() {
    lastResponse = given().patch(usersSearchNonExposedFieldUrl);
    assertNotFoundResponse();
  }

  @Test
  public void usersSearchNonExposedField_POST() {
    lastResponse = given().post(usersSearchNonExposedFieldUrl);
    assertNotFoundResponse();
  }

  @Test
  public void usersSearchNonExposedField_PUT() {
    lastResponse = given().put(usersSearchNonExposedFieldUrl);
    assertNotFoundResponse();
  }

}
