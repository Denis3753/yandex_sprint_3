package ru.yandex.praktikum.login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.api.CourierClient;
import ru.yandex.praktikum.entities.Courier;
import ru.yandex.praktikum.entities.CourierCredentials;
import ru.yandex.praktikum.utils.GenerateCourier;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CourierNotExistLoginAndPasswordTest {

    private static final String MES_NOT_FOUND = "Учетная запись не найдена";
    private Courier courier;
    private CourierClient courierClient;


    @Before
    public void setUp() {
        courier = GenerateCourier.getRandomCourier();
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Logging courier with non existen login and password")
    public void courierLoginNonExistLoginAndPassword() {

        ValidatableResponse response = courierClient.loginCourier(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        assertThat("Code not equal", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("Message not equal", message, equalTo(MES_NOT_FOUND));
    }
}
