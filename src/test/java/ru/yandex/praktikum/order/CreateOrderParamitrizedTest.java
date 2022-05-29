package ru.yandex.praktikum.order;

import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.api.OrderClient;
import ru.yandex.praktikum.entities.Order;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Create order")
@RunWith(Parameterized.class)
public class CreateOrderParamitrizedTest {

    private OrderClient orderClient;
    private Order order;
    private final String[] color;

    public CreateOrderParamitrizedTest(String[] color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = Order.builder()
                .firstName("Alfred")
                .lastName("Hitchcock")
                .address("London, WC47 5UL")
                .metroStation(1)
                .phone("+7 900 000 00 00")
                .rentTime(5)
                .deliveryDate("2022-05-29")
                .comment("our comment...")
                .color(color)
                .build();
        order.addAttachmentOrder();
    }

    @Parameterized.Parameters
    public static Object[][] getOrderColor() {

        return new Object[][] {
                {new String[] {"GREY"}},
                {new String[] {"BLACK"}},
                {new String[] {"GREY", "BLACK"}},
                {new String[] {}}
        };
    }

    @Test()
    @DisplayName("Create order is valid credential")
    public void orderCreateValidCredentials() {
        ValidatableResponse response = orderClient.createOrder(order);
        int statusCode = response.extract().statusCode();
        int trackNumber = response.extract().path("track");

        assertThat("Code not equal", statusCode, equalTo(SC_CREATED));
        assertThat("Track number incorrect", trackNumber, notNullValue());
    }
}
