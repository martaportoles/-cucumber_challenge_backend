package backend.steps;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
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

    private String petName;

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

    @When("the user updates the pet with id {string} status to {string}")
    public void theUserUpdatesThePetWithIdStatusTo(String id, String status) {
        response = given()
                .contentType(ContentType.URLENC)
                .body("status=" + status)
                .when().post("https://petstore.swagger.io/v2/pet/" + id)
                .then().statusCode(200)
                .extract().response();
        jsonAsString = response.asString();
    }

    @Then("the response is the updated id {string}")
    public void the_pet_status_is(String id) {
        Assert.assertEquals(id, response.path("message"));
    }

    /************** BEGIN DELETE SCENARIO ********************/
    @When("the user deletes the pet with id {string}")
    public void the_user_deletes_the_pet_with_id(String id) {
        response = given()
                .contentType(ContentType.JSON)
                .when().delete("https://petstore.swagger.io/v2/pet/" + id)
                .then().statusCode(200)
                .extract().response();
        jsonAsString = response.asString();
    }
    @Then("the pet with id {string} do not exists")
    public void the_pet_with_id_do_not_exists(String id) {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertEquals(id, response.path("message"));

        response = getPetById(id);

        Assert.assertEquals(404, response.statusCode());
        Assert.assertEquals("Pet not found", response.path("message"));
    }

    /************** END DELETE SCENARIO **********************/

/*
    @Given("there are pets with status <status>")
    public void there_are_pets_with_status_status() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
*/

//    @When("there are pets with status {string}")
//    public void there_are_pets_with_status(String status) {
//        response = given().contentType(ContentType.JSON)
//                .queryParam("status", status)
//                .when().get("https://petstore.swagger.io/v2/pet/findByStatus")
//                .then().statusCode(200)
//                .extract().response();
//        jsonAsString = response.asString();
//    }

//    @When("there are pets with <string>")
//    public void thereArePetsWithStatus(String status) {
//        response = given().contentType(ContentType.JSON)
//                .queryParam("status", status)
//                .when().get("https://petstore.swagger.io/v2/pet/findByStatus")
//                .then().statusCode(200)
//                .extract().response();
//        jsonAsString = response.asString();
//    }
//
//    @When("there are pets with <status>")
//    public void thereArePetsWithStatus() {
//        response = given().contentType(ContentType.JSON)
//                .queryParam("status", "available")
//                .when().get("https://petstore.swagger.io/v2/pet/findByStatus")
//                .then().statusCode(200)
//                .extract().response();
//        jsonAsString = response.asString();
//    }

    @When("there are pets with {string}")
    public void there_are_pets_with(String string) {
        response = given().contentType(ContentType.JSON)
                .queryParam("status", "available")
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

        // check that they are all 'open'
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

    private Response getPetById(String id) {
        return given()
                .contentType(ContentType.JSON)
                .when().get("https://petstore.swagger.io/v2/pet/" + id)
                .then()
                .extract().response();
    }

/*

    @When("the user gets all pets with {string} status")
    public void the_user_gets_all_pets_with_status(String petStatus) {
        Object petList = given().contentType(ContentType.JSON)
                .when().get("https://petstore.swagger.io/v2/pet/findByStatus?status="+petStatus)
                .then()
                .statusCode(200)

                .extract().path("name");
    }

    @Then("the pet status is {string}")
    public void the_pet_status_is(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


*/

}
