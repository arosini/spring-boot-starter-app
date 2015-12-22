package ar.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyString;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class UserTests extends AbstractIntegrationTests {

  @Test
  public void getUser() {
    get("/users/1").then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_OK)
        .body("firstName", equalTo("User"))
        .body("lastName", equalTo("One"))
        .body("_links.self.href", equalTo(baseUrl + "/users/1"))
        .body("_links.user.href", equalTo(baseUrl + "/users/1"));
  }

  @Test
  public void getUser_notFound() {
    get("/users/0").then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .assertThat().body(isEmptyString());
  }

  @Test
  public void findUserByLastName_singleResult() {
    String searchUrl = "/users/search/findByLastName?lastName=Four";
    Response response = get(searchUrl);

    response.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_OK)
        .body("_embedded.users", hasSize(1))
        .body("_links.self.href", equalTo(baseUrl + searchUrl));

    assertEmbeddedUser(response, 0, "4", "User", "Four");
  }

  @Test
  public void findUserByLastName_multipleResults() {
    String searchUrl = "/users/search/findByLastName?lastName=One";
    Response response = get(searchUrl);

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
