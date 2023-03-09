package stellarBurgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetIngredients {
    @Step("Get ingredients")
    public static Response getIngredients() {
        Response responseGetIngredients =
                given()
                        .get("api/ingredients");
        return responseGetIngredients;
    }
}

