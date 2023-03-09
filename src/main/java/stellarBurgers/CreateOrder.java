package stellarBurgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreateOrder {
    public static Response responseCreateOrder;

    @Step("Create order")
    public static Response createOrder(Ingredients ingredients, String token) {
        responseCreateOrder =
                given()
                        .header("Content-type", "application/json")
                        .header("Authorization", token)
                        .and()
                        .body(ingredients)
                        .when()
                        .post("/api/orders");
        return responseCreateOrder;
    }
}