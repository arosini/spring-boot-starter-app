package ar.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

import ar.SpringBootStarterApplication;
import ar.model.Resource;
import ar.model.User;
import ar.repository.UserRepository;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.atteo.evo.inflector.English;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

  @Value("${local.server.port}")
  private int port;

  @Autowired
  private UserRepository userRepoistory;

  @Before
  public void before() {
    RestAssured.port = port;
    baseUrl = "http://localhost:" + port;

    userRepoistory.deleteAll();

    createTestUsers();
  }

  /**
   * Validates a response resource's common fields such as audit fields and links.
   * 
   * @param response The response containing the resource.
   * @param jsonPath The path to the resource within the response. Should not be null.
   * @param resourceClass The POJO class of the resource to validate.
   * @param resourceId The ID of the resource to the validate. Ignored if null.
   */
  protected void validateCommonFields(Response response, String jsonPath, Class<? extends Resource> resourceClass,
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

  private void createTestUsers() {
    createTestUser("1", "Charles", "Xavier", "Professor X");
    createTestUser("2", "Scott", "Summers", "Cyclops");
    createTestUser("3", "Alex", "Summers", "Havok");
  }

  private void createTestUser(String id, String firstName, String lastName, String username) {
    User user = new User();
    user.setId(id);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setUsername(username);
    userRepoistory.save(user);
  }

}
