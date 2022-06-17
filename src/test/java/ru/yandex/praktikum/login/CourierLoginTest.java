package ru.yandex.praktikum.login;

import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.api.CourierClient;
import ru.yandex.praktikum.entities.Courier;
import ru.yandex.praktikum.entities.CourierCredentials;
import ru.yandex.praktikum.utils.GenerateCourier;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    private static final String MESSAGE_NOT_FOUND = "Учетная запись не найдена";
    private static final String MESSAGE_BAD_REQUEST = "Недостаточно данных для входа";
    private CourierClient courierClient;
    private ValidatableResponse response;
    private Courier courier;
    private int id;

    @Before
    public void setUp() {

        courier = GenerateCourier.getRandomCourier();
        courierClient = new CourierClient();
        courierClient.createCourier(courier);
    }

    @After
    public void clearState() {

        courierClient.deleteCourier(id);
    }

    @Test
    @DisplayName("Courier is valid credential")
    public void courierLoginValidCredentials() {

        response = courierClient.loginCourier(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        int statusCode = response.extract().statusCode();
        id = response.extract().path("id");

        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("Courier id incorrect", id, notNullValue());
    }

    @Test
    @DisplayName("Courier with empty field login")
    public void courierLoginIsEmptyLogin() {

        response = courierClient.loginCourier(new CourierCredentials(null, courier.getPassword()));
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        assertThat("There is not enough login for the correct courier login", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Message not equal", message,  equalTo(MESSAGE_BAD_REQUEST));
    }

    @Test
    @DisplayName("Courier with empty field password")
    @Issue("Bug unavailable, code actual 504")
    public void courierLoginIsEmptyPassword() {

        response = courierClient.loginCourier(new CourierCredentials(courier.getLogin(), null));
        int statusCode = response.extract().statusCode();

        assertThat("There is not enough password for the correct courier login", statusCode, equalTo(SC_GATEWAY_TIMEOUT));
    }



    @Test
    @DisplayName("Courier invalid login")
    public void courierLoginInvalidLogin() {
        courier.setLogin("invalidLogin");
        response = courierClient.loginCourier(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        assertThat("Code not equal", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("Message not equal", message,  equalTo(MESSAGE_NOT_FOUND));
    }

    @Test
    @DisplayName("Courier invalid password")
    public void courierLoginInvalidPassword() {

        courier.setPassword("invalidPassword");
        response = courierClient.loginCourier(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        assertThat("Code not equal", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("Message not equal", message,  equalTo(MESSAGE_NOT_FOUND));
    }


}
