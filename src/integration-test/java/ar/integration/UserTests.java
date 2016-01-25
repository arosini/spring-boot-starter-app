package ar.integration;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.nullValue;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import ar.entity.User;

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

  // CREATE
  @Test
  public void createUser() {
    Response response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post("/users");

    System.out.println(response.getHeaders().asList());

    response.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_CREATED)
        .body("firstName", equalTo("Kurt"))
        .body("lastName", equalTo("Wagner"))
        .body("username", equalTo("Nightcrawler"));

    validateCommonFields(response, "", User.class, null);
  }

  @Test
  public void createUser_sparseProperties() {
    user.setFirstName(null);
    user.setLastName(null);

    Response response = given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post("/users");

    response.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_CREATED)
        .body("firstName", nullValue())
        .body("lastName", nullValue())
        .body("username", equalTo("Nightcrawler"));

    validateCommonFields(response, "", User.class, null);
  }

  @Test
  public void createUser_nullUsername() {
    user.setUsername(null);

    given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post("/users").then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body("errors[0].entity", equalTo("User"))
        .body("errors[0].message", equalTo("Username must be present"))
        .body("errors[0].invalidValue", equalTo("null"))
        .body("errors[0].property", equalTo("username"));
  }

  @Test
  public void createUser_blankUsername() {
    user.setUsername("");

    given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post("/users").then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body("errors[0].entity", equalTo("User"))
        .body("errors[0].message", equalTo("Username must be present"))
        .body("errors[0].invalidValue", equalTo(""))
        .body("errors[0].property", equalTo("username"));
  }

  @Test
  public void createUser_whitespaceOnlyUsername() {
    user.setUsername("  ");

    given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post("/users").then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body("errors[0].entity", equalTo("User"))
        .body("errors[0].message", equalTo("Username must be present"))
        .body("errors[0].invalidValue", equalTo("  "))
        .body("errors[0].property", equalTo("username"));
  }

  @Test
  public void createUser_nonUniqueUsername() {
    user.setUsername("Professor X");

    given().body(user).contentType(ContentType.JSON).accept(ContentType.JSON).post("/users").then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_CONFLICT)
        .body("errors[0].entity", equalTo("User"))
        .body("errors[0].message", equalTo("Username must be unique"))
        .body("errors[0].invalidValue", equalTo("Professor X"))
        .body("errors[0].property", equalTo("username"));
  }

  @Test
  public void createUser_mediaTypeNotAcceptable() {
    given().body(user).contentType(ContentType.JSON).accept(ContentType.XML).post("/users").then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_NOT_ACCEPTABLE)
        .body(isEmptyString());
  }

  @Test
  public void createUser_unsupportedMediaType() {
    given().body(user).contentType(ContentType.XML).accept(ContentType.JSON).post("/users").then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE)
        .body(isEmptyString());
  }

  // READ
  @Test
  public void getUser() {
    String userOnePath = "/users/1";

    given().accept(ContentType.JSON).get(userOnePath).then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_OK)
        .body("firstName", equalTo("Charles"))
        .body("lastName", equalTo("Xavier"))
        .body("username", equalTo("Professor X"))
        .body("_links.self.href", equalTo(baseUrl + userOnePath))
        .body("_links.user.href", equalTo(baseUrl + userOnePath));
  }

  @Test
  public void getUser_notFound() {
    get("/users/0").then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .body(isEmptyString());
  }

  @Test
  public void findUserByLastName_noResults() {
    String searchUrl = "/users/search/findByLastName?lastName=Lensherr";
    Response response = given().accept(ContentType.JSON).get(searchUrl);

    response.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_OK)
        .body("_embedded.users", hasSize(0))
        .body("_links.self.href", equalTo(baseUrl + searchUrl));
  }

  @Test
  public void findUserByLastName_singleResult() {
    String searchUrl = "/users/search/findByLastName?lastName=Xavier";
    Response response = given().accept(ContentType.JSON).get(searchUrl);

    response.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_OK)
        .body("_embedded.users", hasSize(1))
        .body("_links.self.href", equalTo(baseUrl + searchUrl));

    assertEmbeddedUser(response, 0, "1", "Charles", "Xavier", "Professor X");
  }

  @Test
  public void findUserByLastName_multipleResults() {
    String searchUrl = "/users/search/findByLastName?lastName=Summers";
    Response response = given().accept(ContentType.JSON).get(searchUrl);

    response.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_OK)
        .body("_embedded.users", hasSize(2))
        .body("_links.self.href", equalTo(baseUrl + searchUrl));

    assertEmbeddedUser(response, 0, "2", "Scott", "Summers", "Cyclops");
    assertEmbeddedUser(response, 1, "3", "Alex", "Summers", "Havok");
  }

  @Test
  public void findUserByNonExposedProperty() {
    String searchUrl = "/users/search/findByFirstName?lastName=Summers";
    Response response = given().accept(ContentType.JSON).get(searchUrl);

    response.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_OK)
        .body("_embedded.users", hasSize(2))
        .body("_links.self.href", equalTo(baseUrl + searchUrl));

    assertEmbeddedUser(response, 0, "2", "Scott", "Summers", "Cyclops");
    assertEmbeddedUser(response, 1, "3", "Alex", "Summers", "Havok");
  }

  private void assertEmbeddedUser(Response response, int index, String id, String firstName, String lastName,
      String username) {
    String userJsonPath = "_embedded.users[" + index + "].";

    response.then()
        .body(userJsonPath + "firstName", equalTo(firstName))
        .body(userJsonPath + "lastName", equalTo(lastName))
        .body(userJsonPath + "username", equalTo(username));

    validateCommonFields(response, userJsonPath, User.class, id);
  }

}
