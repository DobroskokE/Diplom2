package stellarburgerstests;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.*;

import static org.hamcrest.Matchers.equalTo;
import static property.Property.url;
import static stellarburgers.CreateUser.responseCreateUser;

public class ChangeUsersDataTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = url;
    }
    Faker faker = new Faker();
    String randomEmail = faker.internet().emailAddress();
    String randomPassword = faker.internet().password();
    String randomName = faker.name().username();
    String newRandomEmail = faker.internet().emailAddress();
    String newRandomPassword = faker.internet().password();
    String newRandomName = faker.name().username();
    String token;

    @Test
    @DisplayName("Check change users data with token")
    public void changeUsersDataWithTokenReturn200AndNewUsersData() {
        User user = new User(randomEmail, randomPassword, randomName);
        User newDataUser = new User(newRandomEmail, newRandomPassword, newRandomName);

        CreateUser.createUser(user);

        token = responseCreateUser.jsonPath().getString("accessToken");

        ChangeUsersData.changeUserData(newDataUser, token)
                .then()
                .assertThat().statusCode(200)
                .and()
                .body("user.email",  equalTo(newDataUser.getEmail()))
                .and()
                .body("user.name",  equalTo(newDataUser.getName()));
    }

    @Test
    @DisplayName("Check change users data without token")
    public void changeUsersDataWithoutTokenReturn401() {
        User user = new User(randomEmail, randomPassword, randomName);
        User newDataUser = new User(newRandomEmail, newRandomPassword, newRandomName);

        CreateUser.createUser(user);

        token = responseCreateUser.jsonPath().getString("accessToken");

        ChangeUsersData.changeUserData(newDataUser, "")
                .then()
                .assertThat().statusCode(401);
    }

    @After
    public void teardown() {
        DeleteUser.deleteUser(token);
    }
}
