package ar.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

import ar.SpringBootStarterApplication;
import ar.entity.Entity;
import ar.entity.User;
import ar.filter.RequestLimitFilter;
import ar.repository.UserRepository;

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
import org.springframework.http.HttpMethod;
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

  /////////////////////////
  // Shared Test Objects //
  /////////////////////////

  /** The last response received from the service. */
  protected Response lastResponse;

  /** The maximum number of requests that can be made to the API each week. */
  protected int requestLimit;

  /** The path of the API root. */
  protected String rootPath;

  /** The location and port of the service. */
  protected String rootUrl;

  ///////////////////////
  // Spring Components //
  ///////////////////////

  /** The port the service is listening on (randomly assigned when context is initialized). */
  @Value("${local.server.port}")
  private int port;

  /** Limits the number of requests to the application. */
  @Autowired
  private RequestLimitFilter requestLimitFilter;

  /**
   * Manipulates {@link User} entities.
   */
  @Autowired
  private UserRepository userRepository;

  ////////////////////
  // Initialization //
  ////////////////////

  /** Initializes a test by setting information and creating data. Called before each test. */
  @Before
  public void before() {
    lastResponse = null;

    rootPath = "/";
    rootUrl = "http://localhost:" + port;

    requestLimitFilter.resetRequestCount();
    requestLimit = requestLimitFilter.getRequestLimit();

    userRepository.deleteAll();
    createUsers();
  }

  ///////////////////////
  // Assertion Helpers //
  ///////////////////////

  /**
   * Asserts a response resource's common fields such as audit fields and links.
   * 
   * @param jsonPath The path to the resource within the response. Should not be null, but may be blank.
   * @param resourceClass The POJO class of the resource to validate.
   * @param resourceId The ID of the resource to the validate. Not used if null.
   */
  protected void assertCommonFields(String jsonPath, Class<? extends Entity> resourceClass,
      String resourceId) {
    String resourceType = resourceClass.getSimpleName().toLowerCase();
    String resourceClassUrl = rootUrl + "/" + English.plural(resourceType) + "/";

    Matcher<String> linkMatcher = resourceId == null ? startsWith(resourceClassUrl)
        : equalTo(resourceClassUrl + resourceId);

    lastResponse.then()
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
    lastResponse.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_CREATED);
  }

  /**
   * Asserts the last response had a status of 400, a content type of JSON, and a body with the provided information.
   * 
   * @param lastResponse The response containing the error.
   * @param status The HTTP status code of the response.
   * @param entityClass The entity class which caused the error.
   * @param message The message associated with the error.
   * @param invalidValue The value which caused the error.
   * @param property The property which caused the error.
   */
  protected void assertErrorResponse(int status, Class<? extends Entity> entityClass, String message,
      String invalidValue, String property) {
    lastResponse.then()
        .contentType(ContentType.JSON)
        .statusCode(status)
        .body("errors[0].entity", equalTo(entityClass.getSimpleName()))
        .body("errors[0].message", equalTo(message))
        .body("errors[0].invalidValue", equalTo(invalidValue))
        .body("errors[0].property", equalTo(property));
  }

  /**
   * Asserts the last response had a status of 204, no content type and no body.
   */
  protected void assertNoContentResponse() {
    lastResponse.then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_NO_CONTENT)
        .body(isEmptyString());
  }

  /**
   * Asserts the last response had a status of 406, no content type and no body.
   */
  protected void assertNotAcceptableResponse() {
    lastResponse.then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_NOT_ACCEPTABLE)
        .body(isEmptyString());
  }

  /**
   * Asserts the last response had a status of 404, no content type and no body.
   */
  protected void assertNotFoundResponse() {
    lastResponse.then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .body(isEmptyString());
  }

  /**
   * Asserts the last response had a status of 405, a content type of JSON and specific body elements.
   */
  protected void assertMethodNotAllowedResponse(HttpMethod method, String url) {
    lastResponse.then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED)
        .body("timestamp", lessThan(System.currentTimeMillis()))
        .body("status", equalTo(405))
        .body("error", equalTo("Method Not Allowed"))
        .body("exception", equalTo("org.springframework.web.HttpRequestMethodNotSupportedException"))
        .body("message", equalTo("Request method '" + method + "' not supported"))
        .body("path", equalTo(url));
  }

  /**
   * Asserts the last response had a status of 200 and a content type of JSON.
   */
  protected void assertOkResponse() {
    lastResponse.then()
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
    Pattern pattern = Pattern.compile("[0-9]+/(.*?)/search");
    java.util.regex.Matcher matcher = pattern.matcher(searchUrl);
    matcher.find();
    String pluralEntity = matcher.group(1);

    assertOkResponse();
    lastResponse.then()
        .body("_embedded." + pluralEntity, hasSize(numResults))
        .body("_links.self.href", equalTo(searchUrl));
  }

  /**
   * Asserts the last response had a status of 415, no content type and no body.
   */
  protected void assertUnsupportedMediaTypeResponse() {
    lastResponse.then()
        .contentType(isEmptyString())
        .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE)
        .body(isEmptyString());
  }

  ///////////////////////////
  // Data Creation Helpers //
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
    userRepository.save(user);
  }

}
