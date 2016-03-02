package ar.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests API endpoints which are not related to a specific resource.
 * 
 * @author adam
 *
 */
public class MetaApiTests extends AbstractIntegrationTests {

  //////////
  // URLs //
  //////////

  private String profileUrl;

  @Override
  @Before
  public void before() {
    super.before();

    profileUrl = baseUrl + "/profile";
  }

  @Test
  public void getBaseUrl() {
    lastResponse = given().accept(ContentType.JSON).get(baseUrl);
    assertOkResponse();
    lastResponse.then()
        .body("_links.profile.href", equalTo(profileUrl))
        .body("_links.users.href", equalTo(baseUrl + "/users{?page,size,sort}"))
        .body("_links.users.templated", equalTo(true));
  }

  // TODO: Why doesn't this work?
  // @Test
  // public void checkProfile() {
  // lastResponse = given().head(profileUrl);
  // assertNoContentResponse();
  // }

  @Test
  public void getProfile() {
    lastResponse = given().accept(ContentType.JSON).get(profileUrl);
    assertOkResponse();
    lastResponse.then()
        .body("_links.self.href", equalTo(profileUrl))
        .body("_links.users.href", equalTo(baseUrl + "/profile/users"));
  }

  @Test
  public void getProfile_notAcceptable() {
    lastResponse = given().accept(ContentType.XML).get(profileUrl);
    assertNotAcceptableResponse();
  }

}
