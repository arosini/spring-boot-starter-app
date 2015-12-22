package ar.integration;

import ar.SpringBootStarterApplication;
import ar.model.User;
import ar.repository.UserRepository;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootStarterApplication.class)
@WebIntegrationTest
public abstract class AbstractIntegrationTests {

  @Value("${local.server.port}")
  private int port;

  @Autowired
  private UserRepository userRepoistory;

  protected String baseUrl;

  @Before
  public void before() {
    baseUrl = "http://localhost:" + port;
    userRepoistory.deleteAll();
    createTestUsers();
  }

  protected Response get(String path) {
    return RestAssured.get(baseUrl + path);
  }

  private void createTestUsers() {
    createTestUser("1", "User", "One");
    createTestUser("2", "User", "One");
    createTestUser("3", "Another", "One");
    createTestUser("4", "User", "Four");
  }

  private User createTestUser(String id, String firstName, String lastName) {
    User user = new User();
    user.setId(id);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    return userRepoistory.save(user);
  }

}
