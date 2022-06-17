package ru.yandex.praktikum.entities;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourierCredentials {

    private String login;
    private String password;

    public CourierCredentials() {}
}
