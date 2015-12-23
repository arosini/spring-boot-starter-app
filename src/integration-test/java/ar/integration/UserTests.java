package ar.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.startsWith;

import ar.model.User;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class UserTests extends AbstractIntegrationTests {

  private User postUser;

  @Override
  @Before
  public void before() {
    super.before();

    postUser = new User();
    postUser.setFirstName("Test");
    postUser.setLastName("User");
  }

  // CREATE
  @Test
  public void createUser() {
    given().body(postUser).contentType(ContentType.JSON).accept(ContentType.JSON).post(baseUrl + "/users").then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_CREATED)
        .body("firstName", equalTo("Test"))
        .body("lastName", equalTo("User"))
        .body("_links.self.href", startsWith(baseUrl + "/users/"))
        .body("_links.user.href", startsWith(baseUrl + "/users/"));
  }

  @Test
  public void createUser_mediaTypeNotAcceptable() {
    given().body(postUser).contentType(ContentType.JSON).accept(ContentType.XML).post(baseUrl + "/users").then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_NOT_ACCEPTABLE)
        .body(isEmptyString());
  }

  @Test
  public void createUser_unsupportedMediaType() {
    given().body(postUser).contentType(ContentType.XML).accept(ContentType.JSON).post(baseUrl + "/users").then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE)
        .body(isEmptyString());
  }

  @Test
  public void createUser_missingFirstName() {
    postUser.setFirstName(null);

    given().body(postUser).contentType(ContentType.JSON).accept(ContentType.JSON).post(baseUrl + "/users").then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE)
        .body(isEmptyString());
  }

  @Test
  public void createUser_missingLastName() {
  }

  // READ
  @Test
  public void getUser() {
    given().accept(ContentType.JSON).get(baseUrl + "/users/1").then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_OK)
        .body("firstName", equalTo("User"))
        .body("lastName", equalTo("One"))
        .body("_links.self.href", equalTo(baseUrl + "/users/1"))
        .body("_links.user.href", equalTo(baseUrl + "/users/1"));
  }

  @Test
  public void getUser_notFound() {
    given().accept(ContentType.JSON).get(baseUrl + "/users/0").then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .body(isEmptyString());
  }

  @Test
  public void getUser_mediaTypeNotAcceptable() {
    given().accept(ContentType.XML).get(baseUrl + "/users/0").then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .body(isEmptyString());
  }

  @Test
  public void findUserByLastName_singleResult() {
    String searchUrl = baseUrl + "/users/search/findByLastName?lastName=Four";
    Response response = given().accept(ContentType.JSON).get(searchUrl);

    response.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_OK)
        .body("_embedded.users", hasSize(1))
        .body("_links.self.href", equalTo(baseUrl + searchUrl));

    assertEmbeddedUser(response, 0, "4", "User", "Four");
  }

  @Test
  public void findUserByLastName_multipleResults() {
    String searchUrl = baseUrl + "/users/search/findByLastName?lastName=One";
    Response response = given().accept(ContentType.JSON).get(searchUrl);

    response.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_OK)
        .body("_embedded.users", hasSize(3))
        .body("_links.self.href", equalTo(baseUrl + searchUrl));

    assertEmbeddedUser(response, 0, "1", "User", "One");
    assertEmbeddedUser(response, 1, "2", "User", "One");
    assertEmbeddedUser(response, 2, "3", "Another", "One");
  }

  private void assertEmbeddedUser(Response response, int index, String id, String firstName, String lastName) {
    String userJsonPath = "_embedded.users[" + index + "]";
    String userUrl = baseUrl + "/users/" + id;

    response.then()
        .body(userJsonPath + ".firstName", equalTo(firstName))
        .body(userJsonPath + ".lastName", equalTo(lastName))
        .body(userJsonPath + "._links.self.href", equalTo(userUrl))
        .body(userJsonPath + "._links.user.href", equalTo(userUrl));
  }

}
