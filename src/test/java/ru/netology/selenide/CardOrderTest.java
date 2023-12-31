package ru.netology.selenide;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardOrderTest {
    String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    @Test
    void shouldSendForm() {
        open("http://localhost:9999");

        $("[data-test-id=city] input").setValue("Киров");
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("form.form > fieldset").shouldHave(attribute("disabled"));

        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] > .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + date));
    }

    @Test
    void shouldSendFormWithCurrentDate() {
        open("http://localhost:9999");

        String dateNow = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        $("[data-test-id=city] input").setValue("Киров");
        $("[data-test-id=date] input").setValue(dateNow);
        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("form.form > fieldset").shouldHave(attribute("disabled"));

        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] > .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + date));
    }

    @Test
    void shouldFailOnInvalidCity() {
        open("http://localhost:9999");

        $("[data-test-id=city] input").setValue("-");
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldFailOnEmptyCity() {
        open("http://localhost:9999");

        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFailOnInvalidDate() {
        open("http://localhost:9999");

        $("[data-test-id=city] input").setValue("Киров");
        for (int i = 0; i < 10; i++) {
            $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        }
        $("[data-test-id=date] input").sendKeys("01.12");
        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("[data-test-id=date] span.input.input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void shouldFailOnDateInPast() {
        open("http://localhost:9999");

        String datePast = LocalDate.now().minusDays(200).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        $("[data-test-id=city] input").setValue("Киров");
        for (int i = 0; i < 10; i++) {
            $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        }
        $("[data-test-id=date] input").sendKeys(datePast);
        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("[data-test-id=date] span.input.input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldFailOnEmptyDate() {
        open("http://localhost:9999");

        $("[data-test-id=city] input").setValue("Киров");
        for (int i = 0; i < 10; i++) {
            $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        }
        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("[data-test-id=date] span.input.input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void shouldFailOnInvalidName() {
        open("http://localhost:9999");

        $("[data-test-id=city] input").setValue("Киров");
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("ddd");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldFailOnEmptyName() {
        open("http://localhost:9999");

        $("[data-test-id=city] input").setValue("Киров");
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFailOnInvalidPhone() {
        open("http://localhost:9999");

        $("[data-test-id=city] input").setValue("Киров");
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=phone] input").setValue("-");
        $("[data-test-id=agreement]").click();
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldFailOnEmptyPhone() {
        open("http://localhost:9999");

        $("[data-test-id=city] input").setValue("Киров");
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=agreement]").click();
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFailOnUncheckedAgreement() {
        open("http://localhost:9999");

        $("[data-test-id=city] input").setValue("Киров");
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("[data-test-id=agreement]").shouldHave(cssClass("input_invalid"));
    }

    @Test
    void shouldSendFormUsingComponents() {
        open("http://localhost:9999");

        $("[data-test-id=city] input").sendKeys("Ки");
        $$("body > .popup.input__popup .menu-item").get(3).click();

        $("[data-test-id=date]").click();

        LocalDate base = LocalDate.now().plusDays(3);
        LocalDate d = LocalDate.now().plusDays(7);

        if (base.getMonth() != d.getMonth()) {
            $("body > .popup .calendar__title .calendar__arrow.calendar__arrow_direction_right[data-step='1']").click();
        }

        LocalDate md = d.withDayOfMonth(1);
        int col = d.getDayOfWeek().getValue();
        int row = (d.getDayOfMonth() + md.getDayOfWeek().getValue() - 2) / 7 + 1;

        // System.out.println(d + " -> " + col + " " + row);

        SelenideElement r = $$("body > .popup .calendar__layout tr.calendar__row").get(row);
        r.$$("td.calendar__day").get(col - 1).click();

        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=phone] input").setValue("+79600000000");
        $("[data-test-id=agreement]").click();
        $$("form.form button.button").find(exactText("Забронировать")).click();

        $("form.form > fieldset").shouldHave(attribute("disabled"));

        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] > .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + d.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }
}
