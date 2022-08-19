package backend.steps;


// TODO: Duda, no aparecía a en el pom.xml
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.Assert;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class AddPetSteps {

    /*
        API is not working properly, new pets seems to be added, but although that is ok,
        all pets are created with the same id, so it is impossible to search by its ID later.

        Because of that, testing uploading data have to be done with already known
        pets and work with that.

        Other way is searching unused IDs and use it to keep database safe with our interactions
    */

    private String petName;
    private String petStatus;
    private ExtractableResponse<Response> response;


    @When("the user adds a new pet with {string}, {string} and {string}")
    public void theUserAddsANewPetWithNamePetAndStatus(String name, String category, String status) {
        response = given().contentType(ContentType.JSON)
                .body(makePet(name, category, status))
                .when().post("https://petstore.swagger.io/v2/pet")
                .then().statusCode(200)
                .extract();

        petName = given().contentType(ContentType.JSON)
                    .body(makePet(name, category, status))
                    .when().post("https://petstore.swagger.io/v2/pet")
                    .then().statusCode(200)
                    .extract().path("name");

    }

    @Then("the pet {string} exist")
    public void thePetNameExist(String name) {

        Assert.assertTrue(petName.equals(name));
    }

    private String makePet(String name, String category, String status) {
        String petId = String.valueOf(Math.abs(new Random().nextInt()));
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
                petId,
                categoryId,
                category,
                name,
                status
        );
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
