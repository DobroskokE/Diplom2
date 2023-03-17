package stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class LoginUser {
    public static Response responseLoginUser;

    @Step("Login user")
    public static Response loginUser(User user) {
        responseLoginUser =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user)
                        .when()
                        .post("/api/auth/login");
        return responseLoginUser;
    }
}
