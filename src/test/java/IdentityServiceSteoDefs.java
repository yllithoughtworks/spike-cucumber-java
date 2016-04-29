import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import org.hamcrest.Matchers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotEquals;

public class IdentityServiceSteoDefs extends BaseJSONServiceTest {

    int userCount;

    @Given("In case there are totally N users")
    public void getExistingUsersCount() {
        userCount = RestAssured.when().get("/api/identity/users").jsonPath().getList("").size();
        System.out.format("userCount %s\n", userCount);
        assertNotEquals(0, userCount);
    }


    @Given("There is NO following users:")
    public void assureThereIsNOTestUser(DataTable userEmailTable) {

        for (DataTableRow userEmail : userEmailTable.getGherkinRows()) {
            if (!userEmail.getCells().get(0).equals("email")) {
                System.out.format("check user %s\n", userEmail.getCells().get(0));
                checkAndDeleteTestUser(userEmail.getCells().get(0));
            }
        }
    }


    private void checkAndDeleteTestUser(String email) {

        ValidatableResponse validate = null;

        try {
            validate = fetchOneUserByEmail(email);
            validate.assertThat().body(Matchers.isEmptyString());
        } catch (AssertionError ae) {
            Long existingUserId = validate.extract().jsonPath().getLong("id");
            deleteUser(existingUserId);
        }
    }

    private ValidatableResponse fetchOneUserByEmail(String email) {

        return RestAssured.given().queryParam("email", email).get("/api/identity/users/search").then();
    }

    private void deleteUser(Long id) {

        RestAssured.given().pathParam("id", id).
                when().delete("/api/identity/users/{id}").
                then().statusCode(200); //success
    }


    @When("Create following test users:")
    public void createNewTestUser(DataTable userTable) {

        for (DataTableRow user : userTable.getGherkinRows()) {
            if (!user.getCells().get(0).equals("email")) {
                System.out.format("create user %s\n", user.getCells().get(0));
                createNewTestUser(user.getCells().get(0),user.getCells().get(1));
            }

        }
    }

    private void createNewTestUser(String email, String name) {

        Map<String, Object> newUserParams = new HashMap<>();
        newUserParams.put("email", email);
        newUserParams.put("name", name);
        newUserParams.put("password", "random");

        Long userUserId = RestAssured.given().contentType(ContentType.JSON).body(newUserParams).
                when().post("/api/identity/users").
                then().statusCode(200).extract().jsonPath().getLong("id"); //success

        // cache the id for future deletion
        //storeToScenario(email, userUserId);
    }

    @Given("There should have N plus (\\d+) users")
    public void verifyNewTestUsersAdditionSuccessful(int addition) {

        System.out.println("usercount: "+ userCount);
        RestAssured.when().get("/api/identity/users").
                then().assertThat().contentType(ContentType.JSON).body("id", Matchers.hasSize(userCount + addition));
    }

    @Then("User (\\S+) can be found with name (\\S+)")
    public void verifyNewTestUserInfoCorrect(String email, String name) {

        RestAssured.given().queryParam("email", email).
                when().get("/api/identity/users/search").
                then().assertThat().body("name", Matchers.equalTo(name));
    }

    @When("Delete a user (\\S+)")
    public void deleteExistingUser(String email) {

        // get id from the created users' id cache
        checkAndDeleteTestUser(email);
    }

    @Then("User (\\S+) can NOT be found")
    public void verifyUserDeletionSuccessful(String email) {

        ValidatableResponse validate = fetchOneUserByEmail(email);
        validate.assertThat().body(Matchers.isEmptyString());
    }


}
