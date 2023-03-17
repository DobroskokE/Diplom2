package stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetUsersOrders {
    @Step("Get users orders")
    public static Response getUsersOrders(String token) {
        Response responseGetUsersOrders =
                given()
                        .header("Authorization", token)
                        .get("/api/orders");
        return responseGetUsersOrders;
    }
}
