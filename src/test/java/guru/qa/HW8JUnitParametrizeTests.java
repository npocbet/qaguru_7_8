package guru.qa;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Selenide.*;

public class HW8JUnitParametrizeTests {

    @ValueSource(strings = {"Lenovo", "HP", "Asus"})
    @DisplayName("Поиск на ситилинке")
    @ParameterizedTest(name = "бренда {0}")
    void searchForBrendsOnCitilink(String value){
        open("https://www.citilink.ru");
        $(".MainHeader__search input[type=\"search\"]").setValue(value).pressEnter();
        $(".BrandCategories__brand-header h1").shouldHave(Condition.text(value));
    }


    @CsvSource(value = {"Lenovo|Мониторы", "HP|Ноутбуки", "Asus|Смартфоны"}, delimiter = '|')
    @DisplayName("Поиск на ситилинке")
    @ParameterizedTest(name = "бренда {0} в категории товара {1}")
    void searchForBrendsAndCategoriesOnCitilink(String brend, String category){
        open("https://www.citilink.ru");
        $(".MainHeader__search input[type=\"search\"]").setValue(category + " " + brend).pressEnter();
        $(".MainLayout__main h1").shouldHave(Condition.text(brend));
    }

    static Stream<Arguments> searchForBrendsAndCategoriesWithSpecifiedQuantityOnCitilink(){
        return Stream.of(
                Arguments.of("asus", List.of("компьютер", "9")),
                Arguments.of("hp", List.of("принтер", "15")),
                Arguments.of("lenovo", List.of("монитор", "19"))
        );
    }

    @MethodSource
    @DisplayName("Поиск на ситилинке")
    @ParameterizedTest(name = "бренда {0} в категории товара и количестве {1}")
    void searchForBrendsAndCategoriesWithSpecifiedQuantityOnCitilink(String brend,List<String> category){
        open("https://www.citilink.ru");
        $(".MainHeader__search input[type=\"search\"]").setValue(category.get(0) + " " + brend).pressEnter();
        $(".MainLayout__main h1").shouldHave(Condition.text(brend));
        $("sup.css-1i8uu4g").shouldHave(Condition.text(category.get(1)));
    }

    @EnumSource(SearchQuery.class)
    @DisplayName("Поиск в ситилинке")
    @ParameterizedTest(name = "категории товара {0}")
    void searchForCategoriesOnCitilink(SearchQuery category){
        open("https://www.citilink.ru");
        $("button[data-label=\"Каталог товаров\"]").click();
        $("a[data-title=\"Ноутбуки и компьютеры\"]").hover();
        $$("a.CatalogMenu__subcategory-link").shouldHave(itemWithText(category.name()));
    }
}
