package stellarburgerstests;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.CreateUser;
import stellarburgers.DeleteUser;
import stellarburgers.LoginUser;
import stellarburgers.User;

import static org.hamcrest.Matchers.equalTo;
import static property.Property.url;
import static stellarburgers.CreateUser.responseCreateUser;

public class LoginUserTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = url;
    }
    Faker faker = new Faker();
    String randomEmail = faker.internet().emailAddress();
    String randomPassword = faker.internet().password();
    String randomName = faker.name().username();
    String token;

    @Test
    @DisplayName("Check login user")
    public void loginUserReturn200AndSuccessTrue() {
        User user = new User(randomEmail, randomPassword, randomName);

        CreateUser.createUser(user);

        token = responseCreateUser.jsonPath().getString("accessToken");

        LoginUser.loginUser(user)
                .then()
                .assertThat().statusCode(200)
                .and()
                .body("success",  equalTo(true));
    }

    @Test
    @DisplayName("Check status and error message with wrong login")
    public void loginWithWrongLoginReturn401AndErrorMessage() {
        User user = new User(randomEmail, randomPassword, randomName);

        CreateUser.createUser(user);

        token = responseCreateUser.jsonPath().getString("accessToken");

        User wrongUser= new User("djdjklsgf", randomPassword, randomName);

        LoginUser.loginUser(wrongUser)
                .then()
                .assertThat().statusCode(401)
                .and()
                .body("message",  equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Check status and error message with wrong password")
    public void loginWithWrongPasswordReturn401AndErrorMessage() {
        User user = new User(randomEmail, randomPassword, randomName);

        CreateUser.createUser(user);

        token = responseCreateUser.jsonPath().getString("accessToken");

        User wrongUser= new User(randomEmail, "sdfsdfs", randomName);

        LoginUser.loginUser(wrongUser)
                .then()
                .assertThat().statusCode(401)
                .and()
                .body("message",  equalTo("email or password are incorrect"));
    }

    @After
    public void teardown() {
        DeleteUser.deleteUser(token);
    }
}
