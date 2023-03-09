package stellarBurgersTests;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarBurgers.*;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static stellarBurgers.CreateUser.responseCreateUser;

public class CreateOrderTest {
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
    @DisplayName("Check creation order with auth")
    public void createOrderWithAuthReturn200AndSuccessTrue() {
        User user = new User(randomEmail, randomPassword, randomName);

        CreateUser.createUser(user);

        token = responseCreateUser.jsonPath().getString("accessToken");

        Ingredients ingredients = new Ingredients(GetIngredients.getIngredients().jsonPath().getList("data._id"));

        CreateOrder.createOrder(ingredients, token)
                .then()
                .assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check creation order without auth")
    public void createOrderWithoutAuthReturn200AndSuccessTrue() {
        Ingredients ingredients = new Ingredients(GetIngredients.getIngredients().jsonPath().getList("data._id"));

        CreateOrder.createOrder(ingredients, "")
                .then()
                .assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check creation order without ingredients")
    public void createOrderWithoutIngredientsReturn400AndErrorMessage() {
        User user = new User(randomEmail, randomPassword, randomName);

        CreateUser.createUser(user);

        token = responseCreateUser.jsonPath().getString("accessToken");

        Ingredients ingredients = new Ingredients();

        CreateOrder.createOrder(ingredients, token)
                .then()
                .assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Check creation order with wrong ingredients")
    public void createOrderWithWrongIngredientsReturn500() {
        User user = new User(randomEmail, randomPassword, randomName);

        CreateUser.createUser(user);

        token = responseCreateUser.jsonPath().getString("accessToken");

        Ingredients ingredients = new Ingredients(List.of(RandomStringUtils.randomAlphabetic(10)));

        CreateOrder.createOrder(ingredients, token)
                .then()
                .assertThat().statusCode(500);
    }

    @After
    public void teardown() {
        if (token != null) {
            DeleteUser.deleteUser(token);
        }
    }
}
