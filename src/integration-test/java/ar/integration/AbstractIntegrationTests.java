package ar.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

import ar.SpringBootStarterApplication;
import ar.entity.Entity;
import ar.entity.User;
import ar.repository.UserRepository;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.atteo.evo.inflector.English;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Pattern;

/**
 * Base integration test class which all integration test classes should extend.
 * 
 * @author adam
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootStarterApplication.class)
@WebIntegrationTest
public abstract class AbstractIntegrationTests {

  /** The path and port of the service. **/
  protected String baseUrl;

  /** The last response received from the service. **/
  protected Response response;

  /** The port the service is listening on (randomly assigned when context is initiated). **/
  @Value("${local.server.port}")
  private int port;

  /** Repository used for manipulating {@link User} resources. **/
  @Autowired
  private UserRepository userRepoistory;

  /** Initializes a test by setting global information and creating test data. Called before each test. **/
  @Before
  public void before() {
    RestAssured.port = port;
    baseUrl = "http://localhost:" + port;
    response = null;

    userRepoistory.deleteAll();
    createUsers();
  }

  ///////////////////////
  // ASSERTION HELPERS //
  ///////////////////////

  /**
   * Asserts the last response had a status of 400, a content type of JSON, and a body with the provided information.
   * 
   * @param response The response containing the error.
   * @param status The HTTP status code of the response.
   * @param entityClass The entity class which caused the error.
   * @param message The message associated with the error.
   * @param invalidValue The value which caused the error.
   * @param property The property which caused the error.
   */
  protected void assertErrorResponse(int status, Class<? extends Entity> entityClass, String message,
      String invalidValue, String property) {
    response.then()
        .contentType(ContentType.JSON)
        .statusCode(status)
        .body("errors[0].entity", equalTo(entityClass.getSimpleName()))
        .body("errors[0].message", equalTo(message))
        .body("errors[0].invalidValue", equalTo(invalidValue))
        .body("errors[0].property", equalTo(property));
  }

  /**
   * Asserts a response resource's common fields such as audit fields and links.
   * 
   * @param jsonPath The path to the resource within the response. Should not be null.
   * @param resourceClass The POJO class of the resource to validate.
   * @param resourceId The ID of the resource to the validate. Ignored if null.
   */
  protected void assertCommonFields(String jsonPath, Class<? extends Entity> resourceClass,
      String resourceId) {
    String resourceType = resourceClass.getSimpleName().toLowerCase();
    String resourceClassUrl = baseUrl + "/" + English.plural(resourceType) + "/";

    Matcher<String> linkMatcher = resourceId == null ? startsWith(resourceClassUrl)
        : equalTo(resourceClassUrl + resourceId);

    response.then()
        .body(jsonPath + "createdBy", nullValue())
        .body(jsonPath + "createdDate", notNullValue())
        .body(jsonPath + "lastModifiedBy", nullValue())
        .body(jsonPath + "lastModifiedDate", notNullValue())
        .body(jsonPath + "_links.self.href", linkMatcher)
        .body(jsonPath + "_links." + resourceType + ".href", linkMatcher);
  }

  /**
   * Asserts the last response had a status of 201 and a content type of JSON.
   */
  protected void assertCreatedResponse() {
    response.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_CREATED);
  }

  /**
   * Asserts the last response had a status of 200, no content type and no body.
   */
  protected void assertNoContentResponse() {
    response.then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_NO_CONTENT)
        .body(isEmptyString());
  }

  /**
   * Asserts the last response had a status of 406, no content type and no body.
   */
  protected void assertNotAcceptableResponse() {
    response.then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_NOT_ACCEPTABLE)
        .body(isEmptyString());
  }

  /**
   * Asserts the last response had a status of 404, no content type and no body.
   */
  protected void assertNotFoundResponse() {
    response.then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .body(isEmptyString());
  }

  /**
   * Asserts the last response had a status of 200 and a content type of JSON.
   */
  protected void assertOkResponse() {
    response.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_OK);
  }

  /**
   * Asserts the last response contained the specified number of results, the correct self link, a status of 200 and a
   * content type of JSON.
   * 
   * @param searchUrl The URL of the search which generated the last response.
   */
  protected void assertSearchResponse(String searchUrl, int numResults) {
    Pattern pattern = Pattern.compile("/(.*?)/search");
    java.util.regex.Matcher matcher = pattern.matcher(searchUrl);
    matcher.find();
    String pluralEntity = matcher.group(1);

    assertOkResponse();
    response.then()
        .body("_embedded." + pluralEntity, hasSize(numResults))
        .body("_links.self.href", equalTo(baseUrl + searchUrl));
  }

  /**
   * Asserts the last response had a status of 415, no content type and no body.
   */
  protected void assertUnsupportedMediaTypeResponse() {
    response.then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE)
        .body(isEmptyString());
  }

  ///////////////////////////
  // DATA CREATION HELPERS //
  ///////////////////////////

  private void createUsers() {
    createUser("1", "Charles", "Xavier", "Professor X");
    createUser("2", "Scott", "Summers", "Cyclops");
    createUser("3", "Alex", "Summers", "Havok");
  }

  private void createUser(String id, String firstName, String lastName, String username) {
    User user = new User();
    user.setId(id);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setUsername(username);
    userRepoistory.save(user);
  }

}
