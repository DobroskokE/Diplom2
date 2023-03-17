package stellarburgerstests;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static property.Property.url;
import static stellarburgers.CreateUser.responseCreateUser;

public class GetUsersOrdersTest {
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
    @DisplayName("Check users orders with auth")
    public void getUsersOrdersWithAuthReturn200AndOrdersIsNotNull() {
        User user = new User(randomEmail, randomPassword, randomName);

        CreateUser.createUser(user);

        token = responseCreateUser.jsonPath().getString("accessToken");

        Ingredients ingredients = new Ingredients(GetIngredients.getIngredients().jsonPath().getList("data._id"));

        CreateOrder.createOrder(ingredients, token);

        GetUsersOrders.getUsersOrders(token)
                .then()
                .assertThat().statusCode(200)
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Check users orders without auth")
    public void getUsersOrdersWithoutAuthReturn401andErrorMessage() {
        User user = new User(randomEmail, randomPassword, randomName);

        CreateUser.createUser(user);

        token = responseCreateUser.jsonPath().getString("accessToken");

        Ingredients ingredients = new Ingredients(GetIngredients.getIngredients().jsonPath().getList("data._id"));

        CreateOrder.createOrder(ingredients, token);

        GetUsersOrders.getUsersOrders("")
                .then()
                .assertThat().statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void teardown() {
        DeleteUser.deleteUser(token);
    }
}
