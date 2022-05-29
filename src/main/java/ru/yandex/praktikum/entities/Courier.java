package ru.yandex.praktikum.entities;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class Courier {

    private String login;
    private String password;
    private String firstName;

    private Courier() {}
}