package ar.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import ar.entity.User;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class UserTests extends AbstractIntegrationTests {

  private User user;

  @Override
  @Before
  public void before() {
    super.before();
    user = new User();
    user.setFirstName("Kurt");
    user.setLastName("Wagner");
    user.setUsername("Nightcrawler");
  }

  ///////////////////////////////////
  // CREATE (Single Resource POST) //
  ///////////////////////////////////

  @Test
  public void createUser() {
    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post("/users");
    assertCreatedResponse();
    assertUserInResponse(user);
  }

  @Test
  public void createUser_sparseProperties() {
    user.setFirstName(null);
    user.setLastName(null);

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post("/users");
    assertCreatedResponse();
    assertUserInResponse(user);
  }

  @Test
  public void createUser_nullUsername() {
    user.setUsername(null);

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post("/users");
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, "Username must be present", "null", "username");
  }

  @Test
  public void createUser_blankUsername() {
    user.setUsername("");

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post("/users");
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, "Username must be present", "", "username");
  }

  @Test
  public void createUser_whitespaceOnlyUsername() {
    user.setUsername("  ");

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post("/users");
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, "Username must be present", "  ", "username");
  }

  @Test
  public void createUser_nonUniqueUsername() {
    user.setUsername("Professor X");

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post("/users");
    assertErrorResponse(HttpStatus.SC_CONFLICT, User.class, "Username must be unique", "Professor X",
        "username");
  }

  @Test
  public void createUser_mediaTypeNotAcceptable() {
    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.XML).post("/users");
    assertNotAcceptableResponse();
  }

  @Test
  public void createUser_unsupportedMediaType() {
    response = given().body(user).contentType(ContentType.XML).accept(ContentType.JSON).post("/users");
    assertUnsupportedMediaTypeResponse();
  }

  //////////////////////////////////
  // CREATE (Single Resource PUT) //
  //////////////////////////////////

  @Test
  public void createUser_specificId() {
    user.setId("0");

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put("/users/0");
    assertCreatedResponse();
    assertUserInResponse(user);
  }

  @Test
  public void createUser_specificId_sparseProperties() {
    user.setId("0");
    user.setFirstName(null);
    user.setLastName(null);

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put("/users/0");
    assertCreatedResponse();
    assertUserInResponse(user);
  }

  @Test
  public void createUser_specificId_nullUsername() {
    user.setId("0");
    user.setUsername(null);

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put("/users/0");
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, "Username must be present", "null", "username");
  }

  @Test
  public void createUser_specificId_blankUsername() {
    user.setId("0");
    user.setUsername("");

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put("/users/0");
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, "Username must be present", "", "username");
  }

  @Test
  public void createUser_specificId_whitespaceOnlyUsername() {
    user.setId("0");
    user.setUsername("  ");

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put("/users/0");
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, "Username must be present", "  ", "username");
  }

  @Test
  public void createUser_specificId_nonUniqueUsername() {
    user.setId("0");
    user.setUsername("Professor X");

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).put("/users/0");
    assertErrorResponse(HttpStatus.SC_CONFLICT, User.class, "Username must be unique", "Professor X",
        "username");
  }

  @Test
  public void createUser_specificId_mediaTypeNotAcceptable() {
    user.setId("0");

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.XML).put("/users/0");
    assertNotAcceptableResponse();
  }

  @Test
  public void createUser_specificId_unsupportedMediaType() {
    user.setId("0");

    response = given().body(user).contentType(ContentType.XML).accept(ContentType.JSON).put("/users/0");
    assertUnsupportedMediaTypeResponse();
  }

  ////////////////////////////////
  // READ (Single Resource GET) //
  ////////////////////////////////

  @Test
  public void getUser() {
    String userId = "1";

    response = given().accept(ContentType.JSON).get("/users/" + userId);
    assertOkResponse();
    assertUserInResponse("Charles", "Xavier", "Professor X");
  }

  @Test
  public void getUser_notFound() {
    response = given().accept(ContentType.JSON).get("/users/0");
    assertNotFoundResponse();
  }

  @Test
  public void getUser_mediaTypeNotAcceptable() {
    response = given().accept(ContentType.XML).get("/users/1");
    assertNotAcceptableResponse();
  }

  ///////////////////////////////////
  // READ (Multiple Resources GET) //
  //////////////////////////////////

  @Test
  public void findUserByLastName_noResults() {
    String searchUrl = "/users/search/findByLastName?lastName=Lensherr";

    response = given().accept(ContentType.JSON).get(searchUrl);
    assertSearchResponse(searchUrl, 0);
  }

  @Test
  public void findUserByLastName_singleResult() {
    String searchUrl = "/users/search/findByLastName?lastName=Xavier";

    response = given().accept(ContentType.JSON).get(searchUrl);
    assertSearchResponse(searchUrl, 1);
    assertUserInResponse(0, "1", "Charles", "Xavier", "Professor X");
  }

  @Test
  public void findUserByLastName_multipleResults() {
    String searchUrl = "/users/search/findByLastName?lastName=Summers";

    response = given().accept(ContentType.JSON).get(searchUrl);
    assertSearchResponse(searchUrl, 2);
    assertUserInResponse(0, "2", "Scott", "Summers", "Cyclops");
    assertUserInResponse(1, "3", "Alex", "Summers", "Havok");
  }

  @Test
  public void findUserByLastName_mediaTypeNotAcceptable() {
    response = given().accept(ContentType.XML).get("/users/search/findByLastName?lastName=Summers");
    assertNotAcceptableResponse();
  }

  @Test
  public void findUser_nonExposedProperty() {
    String searchUrl = "/users/search/findByFirstName?firstName=Charles";

    response = given().accept(ContentType.JSON).get(searchUrl);
    assertNotFoundResponse();
  }

  ////////////////////////////////////
  // Update (Single Resource PATCH) //
  ////////////////////////////////////

  @Test
  public void updateUser() {
    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.JSON).patch("/users/1");
    assertOkResponse();
    assertUserInResponse(user);
  }

  @Test
  public void updateUser_notFound() {
    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.JSON).patch("/users/0");
    assertNotFoundResponse();
  }

  @Test
  public void updateUser_nullUsername() {
    user.setUsername(null);

    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.JSON).patch("/users/1");
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, "Username must be present", "null", "username");
  }

  @Test
  public void updateUser_blankUsername() {
    user.setUsername("");

    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.JSON).patch("/users/1");
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, "Username must be present", "", "username");
  }

  @Test
  public void updateUser_whitespaceOnlyUsername() {
    user.setUsername("  ");

    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.JSON).patch("/users/1");
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, "Username must be present", "  ", "username");
  }

  @Test
  public void updateUser_nonUniqueUsername() {
    user.setUsername("Cyclops");

    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.JSON).patch("/users/1");
    assertErrorResponse(HttpStatus.SC_CONFLICT, User.class, "Username must be unique", "Cyclops", "username");
  }

  @Test
  public void updateUser_mediaTypeNotAcceptable() {
    response = given().body(user).accept(ContentType.XML).contentType(ContentType.JSON).patch("/users/1");
    assertNotAcceptableResponse();
  }

  @Test
  public void updateUser_unsupportedMediaType() {
    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.XML).patch("/users/1");
    assertUnsupportedMediaTypeResponse();
  }

  /////////////
  // HELPERS //
  /////////////

  // Use after creating a resource
  private void assertUserInResponse(User user) {
    assertUserInResponse(-1, user.getId(), user.getFirstName(), user.getLastName(), user.getUsername());
  }

  // Use after retrieving a single resource
  private void assertUserInResponse(String firstName, String lastName, String username) {
    assertUserInResponse(-1, null, firstName, lastName, username);
  }

  // Use after retrieving multiple resources
  private void assertUserInResponse(int index, String id, String firstName, String lastName,
      String username) {
    String userJsonPath = "";
    if (index >= 0) {
      userJsonPath = "_embedded.users[" + index + "].";
    }

    response.then()
        .body(userJsonPath + "firstName", equalTo(firstName))
        .body(userJsonPath + "lastName", equalTo(lastName))
        .body(userJsonPath + "username", equalTo(username));

    assertCommonFields(userJsonPath, User.class, id);
  }

}
