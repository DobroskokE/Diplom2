package stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ChangeUsersData {
    public static Response responseChangeUsersData;

    @Step("Change users data")
    public static Response changeUserData(User user, String token) {
        responseChangeUsersData =
                given()
                        .header("Content-type", "application/json")
                        .header("Authorization", token)
                        .and()
                        .body(user)
                        .when()
                        .patch("/api/auth/user");
        return responseChangeUsersData;
    }
}
