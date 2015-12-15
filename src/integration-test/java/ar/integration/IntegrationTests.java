package ar.integration;

import ar.SpringBootStarterApplication;
import ar.model.User;
import ar.repository.UserRepository;

import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootStarterApplication.class)
@WebIntegrationTest
public abstract class IntegrationTests {

  @Value("${local.server.port}")
  private int port;

  @Autowired
  private UserRepository userRepoistory;

  protected String baseUrl;

  @Before
  public void before() {
    baseUrl = "http://localhost:" + port;
    RestAssured.port = port;

    userRepoistory.deleteAll();
    createTestUsers();
  }

  private void createTestUsers() {
    createTestUser("1", "User", "One");
    createTestUser("2", "User", "One");
    createTestUser("3", "Another", "One");
    createTestUser("4", "User", "Four");
  }

  private User createTestUser(String id, String firstName, String lastName) {
    User user = new User();
    ReflectionTestUtils.setField(user, "id", id);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    return userRepoistory.save(user);
  }

}
