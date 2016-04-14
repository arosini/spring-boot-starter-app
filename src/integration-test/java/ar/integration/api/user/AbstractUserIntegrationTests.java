package ar.integration.api.user;

import static org.hamcrest.Matchers.equalTo;

import ar.entity.User;
import ar.integration.AbstractIntegrationTests;

import org.junit.Before;

public class AbstractUserIntegrationTests extends AbstractIntegrationTests {

  ////////////////////
  // Initialization //
  ////////////////////

  protected String blank;
  protected String whitespace;
  protected String nullString;
  protected String nonUniqueUsername;

  protected String idFieldName;
  protected String usernameFieldName;

  protected String usernamePresentMessage;
  protected String idUniqueMessage;
  protected String usernameUniqueMessage;

  protected User user;

  @Override
  @Before
  public void before() {
    super.before();

    blank = "";
    whitespace = "  ";
    nullString = "null";
    nonUniqueUsername = "Professor X";

    idFieldName = "id";
    usernameFieldName = "username";

    idUniqueMessage = "Id must be unique";
    usernamePresentMessage = "Username must be present";
    usernameUniqueMessage = "Username must be unique";

    user = new User();
    user.setFirstName("Kurt");
    user.setLastName("Wagner");
    user.setUsername("Nightcrawler");
  }

  ///////////////////////
  // Assertion Helpers //
  ///////////////////////

  // Use after creating a resource
  protected void assertUserInResponse(User user) {
    assertUserInResponse(-1, user.getId(), user.getFirstName(), user.getLastName(), user.getUsername());
  }

  // Use after retrieving a single resource
  protected void assertUserInResponse(String firstName, String lastName, String username) {
    assertUserInResponse(-1, null, firstName, lastName, username);
  }

  // Use after retrieving multiple resources
  protected void assertUserInResponse(int index, String id, String firstName, String lastName,
      String username) {
    String userJsonPath = "";
    if (index >= 0) {
      userJsonPath = "_embedded.users[" + index + "].";
    }

    lastResponse.then()
        .body(userJsonPath + "firstName", equalTo(firstName))
        .body(userJsonPath + "lastName", equalTo(lastName))
        .body(userJsonPath + "username", equalTo(username));

    assertCommonFields(userJsonPath, User.class, id);
  }

}
