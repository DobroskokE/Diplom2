package stellarBurgersTests;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarBurgers.CreateUser;
import stellarBurgers.DeleteUser;
import stellarBurgers.User;

import static org.hamcrest.Matchers.equalTo;
import static stellarBurgers.CreateUser.responseCreateUser;

public class CreateUserTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    Faker faker = new Faker();
    String randomEmail = faker.internet().emailAddress();
    String randomPassword = faker.internet().password();
    String randomName = faker.name().username();

    String token;

    @Test
    @DisplayName("Check creation unique user")
    public void createUniqueUserReturn200AndSuccessTrue() {
        User user = new User(randomEmail, randomPassword, randomName);

        CreateUser.createUser(user)
                .then()
                .assertThat().statusCode(200)
                .and()
                .body("success",  equalTo(true));

        token = responseCreateUser.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Check creation unique user")
    public void createTheSameUserReturn403AndErrorMessage() {
        User user = new User(randomEmail, randomPassword, randomName);

        CreateUser.createUser(user);

        token = responseCreateUser.jsonPath().getString("accessToken");

        CreateUser.createUser(user)
                .then()
                .assertThat().statusCode(403)
                .and()
                .body("message",  equalTo("User already exists"));


    }

    @After
    public void teardown() {
        DeleteUser.deleteUser(token);
    }
}
