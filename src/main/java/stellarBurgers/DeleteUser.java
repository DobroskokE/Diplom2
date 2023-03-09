package stellarBurgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteUser {
    @Step("Delete user")
    public static Response deleteUser(String token) {
        Response responseDeleteUser =
                given()
                        .header("Authorization", token)
                        .delete("/api/auth/user");
        return responseDeleteUser;
    }
}

