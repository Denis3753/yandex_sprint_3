package ru.yandex.praktikum.entities;

import io.qameta.allure.Allure;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;

@Builder
@Data
public class Order {

    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public void addAttachmentOrder() {
        Allure.addAttachment("First name : ", firstName);
        Allure.addAttachment("Last name : ", lastName);
        Allure.addAttachment("Address : ", address);
        Allure.addAttachment("Metro station: ", String.valueOf(metroStation));
        Allure.addAttachment("Phone : ", phone);
        Allure.addAttachment("Rent time : ", String.valueOf(rentTime));
        Allure.addAttachment("Delivery date : ", deliveryDate);
        Allure.addAttachment("Comment : ", comment);
        Allure.addAttachment("Color : ", Arrays.toString(color));
    }

    public static Order getFinalInstanceOrder() {
        final Order order = Order.builder()
                .firstName("Alfred")
                .lastName("Hitchcock")
                .address("London, WC47 5UL")
                .metroStation(1)
                .phone("+7 900 000 00 00")
                .rentTime(5)
                .deliveryDate("2022-05-28")
                .comment("our comment...")
                .color(new String[]{"BLACK"})
                .build();
        order.addAttachmentOrder();
        return order;
    }
}
