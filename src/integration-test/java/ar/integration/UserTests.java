package ar.integration;

import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class UserTests extends IntegrationTests {

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
    System.out.println(get("/users/0").asString());
  }

}
