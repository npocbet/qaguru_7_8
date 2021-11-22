package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class YaTest {

    private static List<String> arguments = List.of("лаконичные и стабильные UI тесты на Java");

    @DisplayName("Поиск в ya.ru слова Selenide")
    @Tag("blocker")
    @Test
    void selenideSearchTest() {
        open("https://ya.ru");
        $("#text").setValue("Selenide");
        $("button[type='submit']").click();
        $$("li.serp-item")
                .find(Condition.text("лаконичные и стабильные UI тесты на Java"))
                .shouldBe(Condition.visible);
    }


    static Stream<Arguments> commonYaSearchTest() {
        return Stream.of(
                Arguments.of("Selenide"),
                Arguments.of("")
        );
    }

//    @EnumSource(SearchQuery.class)
    @MethodSource
//    @CsvSource(value = {
//            "Selenide| лаконичные и стабильные UI тесты на Java]",
//            "Allure| Allure"
//    },
//    delimiter = '|')
    @Tag("blocker")
//    @ValueSource(strings = {"Allure", "Selenide"})
    @DisplayName("Поиск в яндексе")
    @ParameterizedTest(name = "Поиск в ya.ru слова {0} и проверка отображения текста {1}")
    void commonYaSearchTest(String searchQuery, List<String> expectedResult) {
        open("https://ya.ru");
        $("#text").setValue(searchQuery);
        $("button[type='submit']").click();
        $$("li.serp-item")
                .find(Condition.text(expectedResult.get(0)))
                .shouldBe(Condition.visible);
    }
}
