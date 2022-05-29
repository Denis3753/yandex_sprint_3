package ru.yandex.praktikum.order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.api.CourierClient;
import ru.yandex.praktikum.api.OrderClient;
import ru.yandex.praktikum.entities.Courier;
import ru.yandex.praktikum.entities.CourierCredentials;
import ru.yandex.praktikum.entities.Order;
import ru.yandex.praktikum.utils.GenerateCourier;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetAllOrdersTest {

    private ValidatableResponse res;
    private CourierClient courierClient;
    private OrderClient orderClient;
    private int courierId;
    private int trackNumb;


    @Before
    public void setUp() {
        Courier courier = GenerateCourier.getRandomCourier();
        courierClient = new CourierClient();
        courierClient.createCourier(courier);
        res = courierClient.loginCourier(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        courierId = res.extract().path("id");

        orderClient = new OrderClient();
        res = orderClient.createOrder(Order.getFinalInstanceOrder());
        trackNumb = res.extract().path("track");
    }

    @Test
    @DisplayName("Get all orders courier")
    public void getAllOrders() {

        res = orderClient.getOrderByTrackNumber(trackNumb);
        int orderId = res.extract().path("order.id");
        orderClient.acceptOrder(orderId, courierId);
        res = orderClient.getAllOrders(courierId);
        int statusCode = res.extract().statusCode();

        assertThat("Code not equal", statusCode, equalTo(SC_OK));
    }
}
