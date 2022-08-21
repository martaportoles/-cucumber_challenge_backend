package backend.steps;


import io.restassured.response.Response;
// TODO: Question, this library did not appear on pom at the beginning
import org.junit.Assert;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;

public class AddPetSteps {

    /*
        API is not working properly, new pets seems to be added, but although that is ok,
        all pets are created with the same id, so it is impossible to search by its ID later.

        Because of that, testing uploading data have to be done with already known
        pets and work with that.

        Other way is searching unused IDs and use it to keep database safe with our interactions (USED THIS ONE)
    */

    public static Response response;
    public static String jsonAsString;

    @When("the user adds a new pet with {int}, {string}, {string} and {string}")
    public void theUserAddsANewPetWithNamePetAndStatus(Integer id, String name, String category, String status) {
        response = given().contentType(ContentType.JSON)
                .body(makePet(id, name, category, status))
                .when().post("https://petstore.swagger.io/v2/pet")
                .then().statusCode(200)
                .extract().response();
        jsonAsString = response.asString();
    }

    @Then("the pet {string} exist")
    public void thePetNameExist(String name) {
        Assert.assertTrue(name.equals(response.path("name")));
    }

    @Then("the pet exists at database with ID {int}")
    public void the_pet_exists_at_database_with(Integer id) {
        response = getPetById(id);

        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(id, response.path("id"));
    }

    @When("the user updates the pet with id {int} status to {string}")
    public void theUserUpdatesThePetWithIdStatusTo(Integer id, String status) {
        response = given()
                .contentType(ContentType.URLENC)
                .body("status=" + status)
                .when().post("https://petstore.swagger.io/v2/pet/" + id)
                .then().statusCode(200)
                .extract().response();
        jsonAsString = response.asString();
    }

    @Then("the response is the updated id {int}")
    public void the_pet_status_is(Integer id) {
        Assert.assertEquals(Integer.toString(id), response.path("message"));
    }

    /************** BEGIN DELETE SCENARIO ********************/
    @When("the user deletes the pet with id {int}")
    public void the_user_deletes_the_pet_with_id(Integer id) {
        response = given()
                .contentType(ContentType.JSON)
                .when().delete("https://petstore.swagger.io/v2/pet/" + id)
                .then().statusCode(200)
                .extract().response();
        jsonAsString = response.asString();
    }
    @Then("the pet with id {int} do not exists")
    public void the_pet_with_id_do_not_exists(Integer id) {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertEquals(Integer.toString(id), response.path("message"));

        response = getPetById(id);

        Assert.assertEquals(404, response.statusCode());
        Assert.assertEquals("Pet not found", response.path("message"));
    }

    /************** END DELETE SCENARIO **********************/

    @When("there are pets with {string}")
    public void there_are_pets_with(String status) {
        response = given().contentType(ContentType.JSON)
                .queryParam("status", status)
                .when().get("https://petstore.swagger.io/v2/pet/findByStatus")
                .then().statusCode(200)
                .extract().response();
        jsonAsString = response.asString();
    }

    @Then("the result is not empty")
    public void the_result_is_not_empty() {
        ArrayList<Map<String,?>> jsonAsArrayList = from(jsonAsString).get("");
        Assert.assertTrue(jsonAsArrayList.size() > 0);
    }

    @Then("all results have the same property value {string}")
    public void all_results_have_the_same_property_value(String status) {
        List<String> petStatuses = response.path("status");

        // check that they are all 'available'
        for (String petStatus : petStatuses) {
            Assert.assertEquals(status, petStatus);
        }
    }

    private String makePet(Integer id, String name, String category, String status) {
        String categoryId = String.valueOf(Math.abs(new Random().nextInt()));
        return String.format(
                "{\n" +
                        "  \"id\": %s,\n" +
                        "  \"category\": {\n" +
                        "    \"id\": %s,\n" +
                        "    \"name\": \"%s\"\n" +
                        "  },\n" +
                        "  \"name\": \"%s\",\n" +
                        "  \"photoUrls\": [],\n" +
                        "  \"tags\": [],\n" +
                        "  \"status\": \"%s\"\n" +
                        "}",
                id,
                categoryId,
                category,
                name,
                status
        );
    }

    private Response getPetById(Integer id) {
        return given()
                .contentType(ContentType.JSON)
                .when().get("https://petstore.swagger.io/v2/pet/" + id)
                .then()
                .extract().response();
    }
}
