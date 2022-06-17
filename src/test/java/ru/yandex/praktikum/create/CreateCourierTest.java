package ru.yandex.praktikum.create;

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

public class CreateCourierTest {

    private static final String MESSAGE_CONFLICT = "Этот логин уже используется. Попробуйте другой.";
    private static final String MESSAGE_BAD_REQ = "Недостаточно данных для создания учетной записи";
    private CourierClient courierClient;
    private ValidatableResponse res;
    private Courier courier;
    private int id;

    @Before
    public void setUp() {
        courier = GenerateCourier.getRandomCourier();
        courierClient = new CourierClient();
    }

    @After
    public void clearState() {
        courierClient.deleteCourier(id);
    }

    @Test
    @DisplayName("Create courier is valid credentials")
    public void courierCreateValidCredentials() {

        res = courierClient.createCourier(courier);
        int statusCode = res.extract().statusCode();
        boolean isCreate = res.extract().path("ok");
        res = courierClient.loginCourier(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        id = res.extract().path("id");

        assertThat("Courier create incorrect", statusCode, equalTo(SC_CREATED));
        assertThat("Courier create incorrect", isCreate, equalTo(true));
    }

    @Test
    @DisplayName("Create courier is empty login")
    public void courierCreateIsEmptyLogin() {
        courier.setLogin(null);
        res = courierClient.createCourier(courier);
        int statusCode = res.extract().statusCode();
        String message = res.extract().path("message");

        assertThat("Not login data to create an courier", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Message not equal", message,  equalTo(MESSAGE_BAD_REQ));
    }

    @Test
    @DisplayName("Create courier is empty password")
    public void courierCreateIsEmptyPassword() {

        courier.setPassword(null);
        res = courierClient.createCourier(courier);
        int statusCode = res.extract().statusCode();
        String message = res.extract().path("message");

        assertThat("Not password data to create an courier", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Message not equal", message,  equalTo(MESSAGE_BAD_REQ));
    }


    @Test
    @DisplayName("Repeat request by duplicate data - this login is already use")
    @Issue("Bug message not equal")
    public void courierCreateRepeatedRequestByDuplicateData() {

        courierClient.createCourier(courier);
        res = courierClient.createCourier(courier);
        int statusCode = res.extract().statusCode();
        String message = res.extract().path("message");

        assertThat("Code not equal", statusCode, equalTo(SC_CONFLICT));
        assertThat("Message not equal", message,  equalTo(MESSAGE_CONFLICT));
    }
}
