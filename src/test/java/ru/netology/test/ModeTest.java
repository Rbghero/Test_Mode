package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

public class ModeTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfullyLoginIfRegisteredActiveUser() {
        Configuration.holdBrowserOpen = true;
        var registeredUser = getRegisteredUser("active");
        $("div [data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("div [data-test-id='password'] input").setValue(registeredUser.getPassword());
        $(byText("Продолжить")).click();
        $("h2").shouldHave(exactText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("div [data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("div [data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $(byText("Продолжить")).click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("div [data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("div [data-test-id='password'] input").setValue(blockedUser.getPassword());
        $(byText("Продолжить")).click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Пользователь заблокирован"))
                .shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("div [data-test-id='login'] input").setValue(wrongLogin);
        $("div [data-test-id='password'] input").setValue(registeredUser.getPassword());
        $(byText("Продолжить")).click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("div [data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("div [data-test-id='password'] input").setValue(wrongPassword);
        $(byText("Продолжить")).click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible);
    }

    @Test
    void shouldNotSendFormIfEmptyLogin() {
        $("div [data-test-id='login'] input").setValue("");
        $("div [data-test-id='password'] input").setValue(getRandomPassword());
        $(byText("Продолжить")).click();
        $("[data-test-id='login'] span.input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendFormIfEmptyPassword() {
        $("div [data-test-id='login'] input").setValue(getRandomLogin());
        $("div [data-test-id='password'] input").setValue("");
        $(byText("Продолжить")).click();
        $("[data-test-id='password'] span.input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }
}