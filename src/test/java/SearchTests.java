import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;

public class SearchTests {

    @Test
    void selenideGoogleTest() {
        open("https://www.google.com/");
        $(byName("q")).setValue("Selenide").pressEnter();
        $("#search").shouldHave(text("selenide.org"));
    }

    @Test
    void selenideYandexTest() {
        open("https://yandex.ru/");
        $("#text").val("Selenide").pressEnter();
        $("div #search-result").shouldHave(text("Selenide: concise UI tests in Java"));
    }
}