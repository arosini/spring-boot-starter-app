package ar.integration;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class AuditTests extends AbstractIntegrationTests {

  @Test
  public void verifyAuditFields() {
    get("/users/1").then()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.SC_OK)
        .body("createdBy", nullValue())
        .body("createdDate", notNullValue())
        .body("lastModifiedBy", nullValue())
        .body("lastModifiedDate", notNullValue());
  }

}
