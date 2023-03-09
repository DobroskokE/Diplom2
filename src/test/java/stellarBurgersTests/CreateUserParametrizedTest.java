package stellarBurgersTests;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import stellarBurgers.CreateUser;
import stellarBurgers.User;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)

public class CreateUserParametrizedTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    static Faker faker = new Faker();
    private final String randomEmail;
    private final String randomPassword;
    private final String randomName;

    public CreateUserParametrizedTest(String randomEmail, String randomPassword, String randomName) {
        this.randomEmail = randomEmail;
        this.randomPassword = randomPassword;
        this.randomName = randomName;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0},{1},{2}")
    public static Object[][] getData() {
        return new Object[][]{
                {"", faker.internet().password(), faker.name().username()},
                {faker.internet().emailAddress(), "", faker.name().username()},
                {faker.internet().emailAddress(), faker.internet().password(), "" },
        };
    }

    @Test
    @DisplayName("Check creation user without one field")
    public void createUserWithoutOneFieldReturn403AndErrorMessage() {
        User user = new User(randomEmail, randomPassword, randomName);

        CreateUser.createUser(user)
                .then()
                .assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
