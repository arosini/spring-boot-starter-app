package ar.integration.api.user;

import static com.jayway.restassured.RestAssured.given;

import ar.entity.User;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class UserEndpointTests extends AbstractUserIntegrationTests {

  ////////////////////
  // Initialization //
  ////////////////////

  private String userUrl;

  @Override
  @Before
  public void before() {
    super.before();
    userUrl = rootUrl + "/users/1";
  }

  /////////////////////////
  // User Endpoint Tests //
  /////////////////////////

  @Test
  public void user_DELETE() {
    lastResponse = given().delete(userUrl);
    assertNoContentResponse();
  }

  @Test
  public void user_GET() {
    lastResponse = given().get(userUrl);
    assertOkResponse();
    assertUserInResponse("Charles", "Xavier", "Professor X");
  }

  @Test
  public void user_GET_notAcceptable() {
    lastResponse = given().accept(ContentType.XML).get(userUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void user_HEAD() {
    lastResponse = given().head(userUrl);
    assertNoContentResponse();
  }

  @Test
  public void user_PATCH() {
    lastResponse = given().body(user).contentType(ContentType.JSON).patch(userUrl);
    assertOkResponse();
    assertUserInResponse(user);
  }

  @Test
  public void user_PATCH_nonTrimmedValues() {
    user.setUsername(whitespace + user.getUsername() + whitespace);
    user.setFirstName(whitespace + user.getFirstName() + whitespace);
    user.setLastName(whitespace + user.getLastName() + whitespace);

    lastResponse = given().body(user).contentType(ContentType.JSON).patch(userUrl);
    assertOkResponse();

    user.setUsername(user.getUsername().trim());
    user.setFirstName(user.getFirstName().trim());
    user.setLastName(user.getLastName().trim());
    assertUserInResponse(user);
  }

  @Test
  public void user_PATCH_nullUsername() {
    user.setUsername(null);

    lastResponse = given().body(user).contentType(ContentType.JSON).patch(userUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, "null", usernameFieldName);
  }

  @Test
  public void user_PATCH_blankUsername() {
    user.setUsername(blank);

    lastResponse = given().body(user).contentType(ContentType.JSON).patch(userUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, blank, usernameFieldName);
  }

  @Test
  public void user_PATCH_whitespaceOnlyUsername() {
    user.setUsername(whitespace);

    lastResponse = given().body(user).contentType(ContentType.JSON).patch(userUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, whitespace, usernameFieldName);
  }

  @Test
  public void user_PATCH_nonUniqueUsername() {
    user.setUsername(nonUniqueUsername);

    lastResponse = given().body(user).contentType(ContentType.JSON).patch(userUrl);
    assertErrorResponse(HttpStatus.SC_CONFLICT, User.class, usernameUniqueMessage, nonUniqueUsername,
        usernameFieldName);
  }

  @Test
  public void user_PATCH_notAcceptable() {
    lastResponse = given().body(user).accept(ContentType.XML).patch(userUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void user_PATCH_unsupportedMediaType() {
    lastResponse = given().body(user).contentType(ContentType.XML).patch(userUrl);
    assertUnsupportedMediaTypeResponse();
  }

  @Test
  public void user_PUT() {
    lastResponse = given().body(user).put(userUrl);
    assertOkResponse();
  }

  @Test
  public void user_PUT_idAlreadyExists() {
    lastResponse = given().body(user).put(userUrl);
    assertErrorResponse(HttpStatus.SC_CONFLICT, User.class, idUniqueMessage, "1", idFieldName);
  }

  @Test
  public void user_PUT_notAcceptable() {
    lastResponse = given().body(user).accept(ContentType.XML).put(userUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void user_PUT_unsupportedMediaType() {
    lastResponse = given().body(user).contentType(ContentType.XML).put(userUrl);
    assertUnsupportedMediaTypeResponse();
  }

}
