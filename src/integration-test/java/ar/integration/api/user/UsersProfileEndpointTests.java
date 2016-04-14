package ar.integration.api.user;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

public class UsersProfileEndpointTests extends AbstractUserIntegrationTests {

  ////////////////////
  // Initialization //
  ////////////////////

  private String usersProfilePath;
  private String usersProfileUrl;

  @Override
  @Before
  public void before() {
    super.before();
    usersProfilePath = "/profile/users";
    usersProfileUrl = rootUrl + usersProfilePath;

  }

  /////////////////////////////
  // Users Profile URL Tests //
  /////////////////////////////

  @Test
  public void usersProfile_DELETE() {
    lastResponse = given().delete(usersProfileUrl);
    assertMethodNotAllowedResponse(HttpMethod.DELETE, usersProfilePath);
  }

  @Test
  public void usersProfile_GET() {
    lastResponse = given().accept(ContentType.JSON).get(usersProfileUrl);
    assertOkResponse();
    lastResponse.then()
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

  @Test
  public void usersProfile_GET_notAcceptable() {
    lastResponse = given().accept(ContentType.XML).get(usersProfileUrl);
    assertNotAcceptableResponse();
  }

  @Test
  public void usersProfile_HEAD() {
    lastResponse = given().head(usersProfileUrl);
    assertNoContentResponse();
  }

  @Test
  public void usersProfile_PATCH() {
    lastResponse = given().patch(usersProfileUrl);
    assertMethodNotAllowedResponse(HttpMethod.PATCH, usersProfilePath);
  }

  @Test
  public void usersProfile_POST() {
    lastResponse = given().post(usersProfileUrl);
    assertMethodNotAllowedResponse(HttpMethod.POST, usersProfilePath);
  }

  @Test
  public void usersProfile_PUT() {
    lastResponse = given().put(usersProfileUrl);
    assertMethodNotAllowedResponse(HttpMethod.PUT, usersProfilePath);
  }

}
