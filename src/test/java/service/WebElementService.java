package service;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class WebElementService {

    public static SelenideElement getById(String id) {
        return $(By.id(id));
    }

    public static SelenideElement getByAttribute(String value, String attribute) {
        return $(By.xpath(String.format("//*[@%s='%s']", attribute, value)));
    }

    public static SelenideElement getByClassName(String className) {
        return $(By.className(className));
    }

     public static ElementsCollection getCollectionByClassName(String value) {
        return $$(byClassName(value));
    }

    public static ElementsCollection getCollectionByTagName(String tagName) {
        return $$(By.tagName(tagName));
    }
}
