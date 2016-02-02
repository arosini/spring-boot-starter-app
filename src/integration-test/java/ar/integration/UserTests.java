package ar.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import ar.entity.User;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class UserTests extends AbstractIntegrationTests {

  /////////////////////
  // URL Definitions //
  /////////////////////

  private String usersUrl;

  private String userUrl;
  private String userUrlNotFound;

  private String usersProfileUrl;

  private String usersSearchUrl;
  private String usersSearchByLastNameUrl;
  private String usersSearchNotFoundUrl;

  //////////////////
  // Error Fields //
  //////////////////

  private String usernameFieldName;

  //////////////////
  // Error Values //
  //////////////////

  private String blank;
  private String whitespace;
  private String nullString;
  private String nonUniqueUsername;

  ////////////////////
  // Error Messages //
  ////////////////////

  private String usernamePresentMessage;
  private String usernameUniqueMessage;

  ///////////////////
  // Example Users //
  ///////////////////

  private User user;
  private User userWithId;

  @Override
  @Before
  public void before() {
    super.before();

    // URLs
    usersUrl = baseUrl + "/users";

    userUrl = baseUrl + "/users/1";
    userUrlNotFound = baseUrl + "/users/0";

    usersProfileUrl = baseUrl + "/profile/users";

    usersSearchUrl = usersUrl + "/search";
    usersSearchByLastNameUrl = usersSearchUrl + "/findByLastName";
    usersSearchNotFoundUrl = usersSearchUrl + "/findByFirstName";

    // Error Fields
    usernameFieldName = "username";

    // Error Values
    blank = "";
    whitespace = "  ";
    nullString = "null";
    nonUniqueUsername = "Professor X";

    // Error Messages
    usernamePresentMessage = "Username must be present";
    usernameUniqueMessage = "Username must be unique";

    // Example Users
    user = new User();
    user.setFirstName("Kurt");
    user.setLastName("Wagner");
    user.setUsername("Nightcrawler");

    userWithId = new User();
    userWithId.setId("0");
    userWithId.setFirstName("Hank");
    userWithId.setLastName("McCoy");
    userWithId.setUsername("Beast");
  }

  ////////////////////////////
  // Check Users API (HEAD) //
  ////////////////////////////

  @Test
  public void checkUsers() {
    response = given().head(usersUrl);
    assertNoContentResponse();
  }

  // TODO: Research, possibly file a Spring Data Rest issue.
  // @Test
  // public void checkUsersProfile() {
  // response = given().head(usersProfileUrl);
  // System.out.println(response.asString());
  // assertNoContentResponse();
  // }

  @Test
  public void checkUser() {
    response = given().head(userUrl);
    assertNoContentResponse();
  }

  @Test
  public void checkUser_notFound() {
    response = given().head(userUrlNotFound);
    assertNotFoundResponse();
  }

  @Test
  public void checkUsersSearch() {
    response = given().head(usersSearchUrl);
    assertNoContentResponse();
  }

  @Test
  public void checkUsersSearch_byLastName() {
    response = given().head(usersSearchByLastNameUrl);
    assertNoContentResponse();
  }

  @Test
  public void checkUserSearch_notFound() {
    response = given().head(usersSearchNotFoundUrl);
    assertNotFoundResponse();
  }

  ///////////////////
  // GET Users API //
  ///////////////////

  @Test
  public void getUsersApi() {
    response = given().accept(ContentType.JSON).get(usersUrl);
    assertOkResponse();
    assertUserInResponse(0, "1", "Charles", "Xavier", "Professor X");
    assertUserInResponse(1, "2", "Scott", "Summers", "Cyclops");
    assertUserInResponse(2, "3", "Alex", "Summers", "Havok");
    response.then()
        .body("_links.self.href", equalTo(usersUrl))
        .body("_links.profile.href", equalTo(usersProfileUrl))
        .body("_links.search.href", equalTo(usersSearchUrl))
        .body("page.size", equalTo(20))
        .body("page.totalElements", equalTo(3))
        .body("page.totalPages", equalTo(1))
        .body("page.number", equalTo(0));
  }

  @Test
  public void getUsersApi_notAcceptable() {
    response = given().accept(ContentType.XML).get(usersUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void getUsersProfile() {
    response = given().accept(ContentType.JSON).get(usersProfileUrl);
    assertOkResponse();
    response.then()
        .body("alps.version", equalTo("1.0"))
        .body("alps.descriptors[0].id", equalTo("user-representation"))
        .body("alps.descriptors[0].href", equalTo(usersProfileUrl))
        .body("alps.descriptors[0].descriptors[0].name", equalTo("firstName"))
        .body("alps.descriptors[0].descriptors[1].name", equalTo("lastName"))
        .body("alps.descriptors[0].descriptors[2].name", equalTo("username"))
        .body("alps.descriptors[0].descriptors[3].name", equalTo("version"))
        .body("alps.descriptors[0].descriptors[4].name", equalTo("createdBy"))
        .body("alps.descriptors[0].descriptors[5].name", equalTo("createdDate"))
        .body("alps.descriptors[0].descriptors[6].name", equalTo("lastModifiedBy"))
        .body("alps.descriptors[0].descriptors[7].name", equalTo("lastModifiedDate"));
  }

  // TODO: Research, possibly file a Spring Data Rest issue.
  // @Test
  // public void getUsersProfile_notAcceptable() {
  // response = given().accept(ContentType.XML).get(usersProfileUrl);
  // assertNotAcceptableResponse();
  // }

  @Test
  public void getUsersSearch() {
    response = given().get(usersSearchUrl);
    assertOkResponse();
    response.then()
        .body("_links.findByLastName.href", equalTo(usersSearchByLastNameUrl + "{?lastName}"))
        .body("_links.findByLastName.templated", equalTo(true))
        .body("_links.self.href", equalTo(usersSearchUrl));
  }

  @Test
  public void getUserSearch_notAcceptable() {
    response = given().accept(ContentType.XML).get(usersSearchUrl);
    assertNotAcceptableResponse();
  }

  ///////////////////////////////////
  // CREATE (Single Resource POST) //
  ///////////////////////////////////

  @Test
  public void createUser() {
    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertCreatedResponse();
    assertUserInResponse(user);
  }

  @Test
  public void createUser_sparseProperties() {
    user.setFirstName(null);
    user.setLastName(null);

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertCreatedResponse();
    assertUserInResponse(user);
  }

  @Test
  public void createUser_nullUsername() {
    user.setUsername(null);

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, nullString, usernameFieldName);
  }

  @Test
  public void createUser_blankUsername() {
    user.setUsername(blank);

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, blank, usernameFieldName);
  }

  @Test
  public void createUser_whitespaceOnlyUsername() {
    user.setUsername(whitespace);

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, whitespace, usernameFieldName);
  }

  @Test
  public void createUser_nonUniqueUsername() {
    user.setUsername(nonUniqueUsername);

    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post(usersUrl);
    assertErrorResponse(HttpStatus.SC_CONFLICT, User.class, usernameUniqueMessage, nonUniqueUsername,
        usernameFieldName);
  }

  @Test
  public void createUser_mediaTypeNotAcceptable() {
    response = given().body(user).contentType(ContentType.JSON).accept(ContentType.XML).post(usersUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void createUser_unsupportedMediaType() {
    response = given().body(user).contentType(ContentType.XML).accept(ContentType.JSON).post(usersUrl);
    assertUnsupportedMediaTypeResponse();
  }

  //////////////////////////////////
  // CREATE (Single Resource PUT) //
  //////////////////////////////////

  @Test
  public void createUser_specificId() {
    response = given().body(userWithId).contentType(ContentType.JSON).accept(ContentType.JSON).put(userUrlNotFound);
    assertCreatedResponse();
    assertUserInResponse(userWithId);
  }

  @Test
  public void createUser_specificId_sparseProperties() {
    userWithId.setFirstName(null);
    userWithId.setLastName(null);

    response = given().body(userWithId).contentType(ContentType.JSON).accept(ContentType.JSON).put(userUrlNotFound);
    assertCreatedResponse();
    assertUserInResponse(userWithId);
  }

  @Test
  public void createUser_specificId_nullUsername() {
    userWithId.setUsername(null);

    response = given().body(userWithId).contentType(ContentType.JSON).accept(ContentType.JSON).put(userUrlNotFound);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, "null", usernameFieldName);
  }

  @Test
  public void createUser_specificId_blankUsername() {
    userWithId.setUsername(blank);

    response = given().body(userWithId).contentType(ContentType.JSON).accept(ContentType.JSON).put(userUrlNotFound);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, blank, usernameFieldName);
  }

  @Test
  public void createUser_specificId_whitespaceOnlyUsername() {
    userWithId.setUsername(whitespace);

    response = given().body(userWithId).contentType(ContentType.JSON).accept(ContentType.JSON).put(userUrlNotFound);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, whitespace, usernameFieldName);
  }

  @Test
  public void createUser_specificId_nonUniqueUsername() {
    userWithId.setUsername(nonUniqueUsername);

    response = given().body(userWithId).contentType(ContentType.JSON).accept(ContentType.JSON).put(userUrlNotFound);
    assertErrorResponse(HttpStatus.SC_CONFLICT, User.class, usernameUniqueMessage, nonUniqueUsername,
        usernameFieldName);
  }

  @Test
  public void createUser_specificId_mediaTypeNotAcceptable() {
    response = given().body(userWithId).contentType(ContentType.JSON).accept(ContentType.XML).put(userUrlNotFound);
    assertNotAcceptableResponse();
  }

  @Test
  public void createUser_specificId_unsupportedMediaType() {
    response = given().body(userWithId).contentType(ContentType.XML).accept(ContentType.JSON).put(userUrlNotFound);
    assertUnsupportedMediaTypeResponse();
  }

  ////////////////////////////////
  // READ (Single Resource GET) //
  ////////////////////////////////

  @Test
  public void getUser() {
    response = given().accept(ContentType.JSON).get(userUrl);
    assertOkResponse();
    assertUserInResponse("Charles", "Xavier", "Professor X");
  }

  @Test
  public void getUser_notFound() {
    response = given().accept(ContentType.JSON).get(userUrlNotFound);
    assertNotFoundResponse();
  }

  @Test
  public void getUser_mediaTypeNotAcceptable() {
    response = given().accept(ContentType.XML).get(userUrl);
    assertNotAcceptableResponse();
  }

  ///////////////////////////////////
  // READ (Multiple Resources GET) //
  //////////////////////////////////

  @Test
  public void findUsersByLastName_singleResult() {
    String searchUrl = usersSearchByLastNameUrl + "?lastName=Xavier";

    response = given().accept(ContentType.JSON).get(searchUrl);
    assertSearchResponse(searchUrl, 1);
    assertUserInResponse(0, "1", "Charles", "Xavier", "Professor X");
  }

  @Test
  public void findUsersByLastName_multipleResults() {
    String searchUrl = usersSearchByLastNameUrl + "?lastName=Summers";

    response = given().accept(ContentType.JSON).get(searchUrl);
    assertSearchResponse(searchUrl, 2);
    assertUserInResponse(0, "2", "Scott", "Summers", "Cyclops");
    assertUserInResponse(1, "3", "Alex", "Summers", "Havok");
  }

  @Test
  public void findUsersByLastName_noResults() {
    String searchUrl = usersSearchByLastNameUrl + "?lastName=Lensherr";

    response = given().accept(ContentType.JSON).get(searchUrl);
    assertSearchResponse(searchUrl, 0);
  }

  @Test
  public void findUsersByLastName_missingParameter() {
    response = given().accept(ContentType.JSON).get(usersSearchByLastNameUrl);
    assertSearchResponse(usersSearchByLastNameUrl, 0);
  }

  @Test
  public void findUserByLastName_mediaTypeNotAcceptable() {
    response = given().accept(ContentType.XML).get(usersSearchByLastNameUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void findUser_nonExposedProperty() {
    response = given().accept(ContentType.JSON).get(usersSearchUrl + "/findByFirstName");
    assertNotFoundResponse();
  }

  ////////////////////////////////////
  // Update (Single Resource PATCH) //
  ////////////////////////////////////

  @Test
  public void updateUser() {
    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.JSON).patch(userUrl);
    assertOkResponse();
    assertUserInResponse(user);
  }

  @Test
  public void updateUser_notFound() {
    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.JSON).patch(userUrlNotFound);
    assertNotFoundResponse();
  }

  @Test
  public void updateUser_nullUsername() {
    user.setUsername(null);

    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.JSON).patch(userUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, "null", usernameFieldName);
  }

  @Test
  public void updateUser_blankUsername() {
    user.setUsername(blank);

    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.JSON).patch(userUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, blank, usernameFieldName);
  }

  @Test
  public void updateUser_whitespaceOnlyUsername() {
    user.setUsername(whitespace);

    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.JSON).patch(userUrl);
    assertErrorResponse(HttpStatus.SC_BAD_REQUEST, User.class, usernamePresentMessage, whitespace, usernameFieldName);
  }

  @Test
  public void updateUser_nonUniqueUsername() {
    user.setUsername(nonUniqueUsername);

    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.JSON).patch(userUrl);
    assertErrorResponse(HttpStatus.SC_CONFLICT, User.class, usernameUniqueMessage, nonUniqueUsername,
        usernameFieldName);
  }

  @Test
  public void updateUser_mediaTypeNotAcceptable() {
    response = given().body(user).accept(ContentType.XML).contentType(ContentType.JSON).patch(userUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void updateUser_unsupportedMediaType() {
    response = given().body(user).accept(ContentType.JSON).contentType(ContentType.XML).patch(userUrl);
    assertUnsupportedMediaTypeResponse();
  }

  /////////////////////////////////////
  // Delete (Single Resource DELETE) //
  /////////////////////////////////////

  @Test
  public void deleteUser() {
    response = given().delete(userUrl);
    assertNoContentResponse();
  }

  @Test
  public void deleteUser_notFound() {
    response = given().delete(userUrlNotFound);
    assertNotFoundResponse();
  }

  /////////////
  // Helpers //
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
