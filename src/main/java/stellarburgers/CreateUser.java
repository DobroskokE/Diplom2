package stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreateUser {
    public static Response responseCreateUser;

    @Step("Create user")
    public static Response createUser(User user) {
        responseCreateUser =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user)
                        .when()
                        .post("/api/auth/register");
        return responseCreateUser;
    }
}