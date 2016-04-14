package ar.integration.api.user;

import static com.jayway.restassured.RestAssured.given;

import ar.entity.User;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class NonExistentUserEndpointTests extends AbstractUserIntegrationTests {

  ////////////////////
  // Initialization //
  ////////////////////

  private String nonExistentUserUrl;

  @Override
  @Before
  public void before() {
    super.before();
    nonExistentUserUrl = rootUrl + "/users/0";
  }

  //////////////////////////////
  // User Not Found URL Tests //
  //////////////////////////////

  @Test
  public void nonExistentUser_DELETE() {
    lastResponse = given().delete(nonExistentUserUrl);
    assertNotFoundResponse();
  }

  @Test
  public void nonExistentUser_GET() {
    lastResponse = given().get(nonExistentUserUrl);
    assertNotFoundResponse();
  }

  @Test
  public void nonExistentUser_GET_notAcceptable() {
    lastResponse = given().contentType(ContentType.XML).get(nonExistentUserUrl);
    assertNotFoundResponse();
  }

  @Test
  public void nonExistentUser_HEAD() {
    lastResponse = given().head(nonExistentUserUrl);
    assertNotFoundResponse();
  }

  @Test
  public void nonExistentUser_PATCH() {
    lastResponse = given().patch(nonExistentUserUrl);
    assertNotFoundResponse();
  }

  @Test
  public void nonExistentUser_POST() {
    lastResponse = given().post(nonExistentUserUrl);
    assertNotFoundResponse();
  }

  @Test
  public void nonExistentUser_PUT() {
    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put(nonExistentUserUrl);
    assertCreatedResponse();
    assertUserInResponse(user);
  }

  @Test
  public void nonExistentUser_PUT_sparseProperties() {
    user.setFirstName(null);
    user.setLastName(null);

    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put(nonExistentUserUrl);
    assertCreatedResponse();
    assertUserInResponse(user);
  }

  @Test
  public void nonExistentUser_PUT_nonTrimmedValues() {
    user.setUsername(whitespace + user.getUsername() + whitespace);
    user.setFirstName(whitespace + user.getFirstName() + whitespace);
    user.setLastName(whitespace + user.getLastName() + whitespace);

    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put(nonExistentUserUrl);
    assertCreatedResponse();

    user.setUsername(user.getUsername().trim());
    user.setFirstName(user.getFirstName().trim());
    user.setLastName(user.getLastName().trim());
    assertUserInResponse(user);
  }

  @Test
  public void nonExistentUser_PUT_nullUsername() {
    user.setUsername(null);

    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put(nonExistentUserUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, "null", usernameFieldName);
  }

  @Test
  public void nonExistentUser_PUT_blankUsername() {
    user.setUsername(blank);

    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put(nonExistentUserUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, blank, usernameFieldName);
  }

  @Test
  public void nonExistentUser_PUT_whitespaceOnlyUsername() {
    user.setUsername(whitespace);

    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put(nonExistentUserUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, whitespace, usernameFieldName);
  }

  @Test
  public void nonExistentUser_PUT_nonUniqueUsername() {
    user.setUsername(nonUniqueUsername);

    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put(nonExistentUserUrl);
    assertErrorResponse(HttpStatus.SC_CONFLICT, User.class, usernameUniqueMessage, nonUniqueUsername,
        usernameFieldName);
  }

  @Test
  public void nonExistentUser_PUT_notAcceptable() {
    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.XML).put(nonExistentUserUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void nonExistentUser_PUT_unsupportedMediaType() {
    lastResponse = given().body(user).contentType(ContentType.XML).accept(ContentType.JSON).put(nonExistentUserUrl);
    assertUnsupportedMediaTypeResponse();
  }

}
