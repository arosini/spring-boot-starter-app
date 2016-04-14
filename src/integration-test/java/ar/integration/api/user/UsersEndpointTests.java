package ar.integration.api.user;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import ar.entity.User;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 * Tests for the users endpoint (/users).
 * 
 * @author adam
 *
 */
public class UsersEndpointTests extends AbstractUserIntegrationTests {

  ////////////////////
  // Initialization //
  ////////////////////

  private String usersPath;
  private String usersUrl;

  @Override
  @Before
  public void before() {
    super.before();

    usersPath = "/users";
    usersUrl = rootUrl + usersPath;
  }

  //////////////////////////
  // Users Endpoint Tests //
  //////////////////////////

  @Test
  public void users_DELETE() {
    lastResponse = given().delete(usersUrl);
    assertMethodNotAllowedResponse(HttpMethod.DELETE, usersPath);
  }

  @Test
  public void users_GET() {
    lastResponse = given().get(usersUrl);
    assertOkResponse();
    assertUserInResponse(0, "1", "Charles", "Xavier", "Professor X");
    assertUserInResponse(1, "2", "Scott", "Summers", "Cyclops");
    assertUserInResponse(2, "3", "Alex", "Summers", "Havok");
    lastResponse.then()
        .body("_links.self.href", equalTo(usersUrl))
        .body("_links.profile.href", equalTo(rootUrl + "/profile/users"))
        .body("_links.search.href", equalTo(rootUrl + "/users/search"))
        .body("page.size", equalTo(20))
        .body("page.totalElements", equalTo(3))
        .body("page.totalPages", equalTo(1))
        .body("page.number", equalTo(0));
  }

  @Test
  public void users_GET_notAcceptable() {
    lastResponse = given().accept(ContentType.XML).get(usersUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void users_HEAD() {
    lastResponse = given().head(usersUrl);
    assertNoContentResponse();
  }

  @Test
  public void users_PATCH() {
    lastResponse = given().patch(usersUrl);
    assertMethodNotAllowedResponse(HttpMethod.PATCH, usersPath);
  }

  @Test
  public void users_POST() {
    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertCreatedResponse();
    assertUserInResponse(user);
  }

  @Test
  public void users_POST_sparseProperties() {
    user.setFirstName(null);
    user.setLastName(null);

    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertCreatedResponse();
    assertUserInResponse(user);
  }

  @Test
  public void users_POST_nonTrimmedValues() {
    user.setUsername(whitespace + user.getUsername() + whitespace);
    user.setFirstName(whitespace + user.getFirstName() + whitespace);
    user.setLastName(whitespace + user.getLastName() + whitespace);

    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertCreatedResponse();

    user.setUsername(user.getUsername().trim());
    user.setFirstName(user.getFirstName().trim());
    user.setLastName(user.getLastName().trim());
    assertUserInResponse(user);
  }

  @Test
  public void users_POST_nullUsername() {
    user.setUsername(null);

    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, nullString, usernameFieldName);
  }

  @Test
  public void users_POST_emptyUsername() {
    user.setUsername(blank);

    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, blank, usernameFieldName);
  }

  @Test
  public void users_POST_whitespaceOnlyUsername() {
    user.setUsername(whitespace);

    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, whitespace, usernameFieldName);
  }

  @Test
  public void users_POST_nonUniqueUsername() {
    user.setUsername("Professor X");

    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertErrorResponse(HttpStatus.SC_CONFLICT, User.class, usernameUniqueMessage, nonUniqueUsername,
        usernameFieldName);
  }

  @Test
  public void users_POST_notAcceptable() {
    lastResponse = given().body(user).contentType(ContentType.JSON).accept(ContentType.XML).post(usersUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void users_POST_unsupportedMediaType() {
    lastResponse = given().body(user).contentType(ContentType.XML).accept(ContentType.JSON).post(usersUrl);
    assertUnsupportedMediaTypeResponse();
  }

  @Test
  public void users_PUT() {
    lastResponse = given().put(usersUrl);
    assertMethodNotAllowedResponse(HttpMethod.PUT, usersPath);
  }

}
