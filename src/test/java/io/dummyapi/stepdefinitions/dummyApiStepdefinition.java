package stepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.interactions.Put;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class dummyApiStepDefinition {
    private final String baseUrl = "https://dummyapi.io/data/v1/";
    private final String apiKey = "665a7d2064dc77f2049a485c";
    Actor user;
    private String email;
    private String idUser;

    @Before
    public void config (){
        user = Actor.named("Mariana").whoCan(CallAnApi.at(baseUrl));
    }

    @When("the user requests to get list of users")
    public void getListOfUsers() {
        user.attemptsTo(
                Get.resource("user").with(requestSpecification -> requestSpecification.header("app-id",apiKey))
        );
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        assertThat(SerenityRest.lastResponse().getStatusCode(), equalTo(expectedStatusCode));
    }

    @Then("the response should contain a list of users")
    public void verifyResponseContainsUsers() {
        assertThat(SerenityRest.lastResponse().getBody().jsonPath().get("data"), is(notNullValue()));
    }

    @When("the user requests to create a new user with username {string}, email {string}, firstName {string} and lastName {string}")
    public void createUser(String username, String email, String firstName, String lastName) {
        user.attemptsTo(
                Post.to("user/create").withRequest(requestSpecification ->
                        requestSpecification.header("app-id",apiKey)
                                .header("Content-Type", "application/json")
                                .body("{\"username\": \""+username+"\",\"email\": \""+email+"\",\"lastName\": \""+lastName+"\",\"firstName\": \""+firstName+"\"}"))
        );
    }

    @Then("the response should contain the newly created user information email {string}, firstName {string} and lastName {string}")
    public void verifyResponseContainsNewUser(String email, String firstName, String lastName) {
        assertThat(SerenityRest.lastResponse().getBody().jsonPath().get("firstName"), equalTo(firstName));
        assertThat(SerenityRest.lastResponse().getBody().jsonPath().get("lastName"), equalTo(lastName));
        assertThat(SerenityRest.lastResponse().getBody().jsonPath().get("email"), equalTo(email));
    }

    @When("the user requests to get user details by ID")
    public void the_user_requests_to_get_user_details_by_id() {
        System.out.println("**************" + SerenityRest.lastResponse().getBody().jsonPath().get("id").toString());
        idUser = SerenityRest.lastResponse().getBody().jsonPath().get("id").toString();
        user.attemptsTo(
                Get.resource("user/"+idUser).with(requestSpecification -> requestSpecification.header("app-id",apiKey))
        );
    }
    @Then("the response should contain the user details with the provided ID")
    public void the_response_should_contain_the_user_details_with_the_provided_id() {
        idUser = SerenityRest.lastResponse().getBody().jsonPath().get("id").toString();
        assertThat(SerenityRest.lastResponse().getBody().jsonPath().get("id"), equalTo(idUser));
    }

    @When("the user requests to delete user with the provided ID")
    public void the_user_requests_to_delete_user_with_the_provided_id() {
        idUser = SerenityRest.lastResponse().getBody().jsonPath().get("id").toString();
        user.attemptsTo(
                Delete.from("user/"+idUser).with(requestSpecification -> requestSpecification.header("app-id",apiKey))
        );
    }
    @Then("the user should no longer exist in the system")
    public void the_user_should_no_longer_exist_in_the_system() {
        idUser = SerenityRest.lastResponse().getBody().jsonPath().get("id").toString();
        user.attemptsTo(
                Get.resource("user/"+idUser).with(requestSpecification -> requestSpecification.header("app-id",apiKey))
        );
        assertThat(SerenityRest.lastResponse().getStatusCode(), equalTo(404));
        assertThat(SerenityRest.lastResponse().jsonPath().get("error"), equalTo("RESOURCE_NOT_FOUND"));
    }

    @When("the user requests to update user details with new email {string} for the provided ID")
    public void the_user_requests_to_update_user_details_with_new_email_for_the_provided_id(String email) {
        idUser = SerenityRest.lastResponse().getBody().jsonPath().get("id").toString();
        user.attemptsTo(
                Put.to("user/"+idUser).withRequest(requestSpecification ->
                        requestSpecification.header("app-id",apiKey)
                                .header("Content-Type", "application/json")
                                .body("{\"email\": \""+email+"\"}"))
        );
    }
    @Then("the response should confirm the user details have been updated new email {string}")
    public void the_response_should_confirm_the_user_details_have_been_updated(String email) {
        assertThat(SerenityRest.lastResponse().getBody().jsonPath().get("email"), equalTo(email));
    }
}
