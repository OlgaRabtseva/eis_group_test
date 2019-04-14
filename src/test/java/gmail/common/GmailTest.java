package gmail.common;

import com.codeborne.selenide.Condition;
import gmail.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static service.WebElementService.*;

public class GmailTest extends BaseTest {
    private final String MAIN_URL = "https://accounts.google.com/signin/v2/identifier?continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&service=mail&sacu=1&rip=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin";
    private final String TEST_EMAIL = "eisgroupmail34@gmail.com";
    private final String TEST_PASSWORD = "123456zq!";
    private final String subject = RandomStringUtils.randomAlphabetic(15);


    @BeforeClass
    protected void prepareData() {
        open(MAIN_URL);
        wait = new WebDriverWait(getWebDriver(), 20);
    }

    @Test
    public void loginTest() {
        loginAsTestUser();
        checkLoginSuccess();
    }

    @Test(dependsOnMethods = "loginTest")
    public void sendEmailTest() {
        createAndSendNewEmail(TEST_EMAIL, subject);
        checkEmailWasSent(subject);
        checkEmailWasReceived(subject);
    }

    @Test(dependsOnMethods = "sendEmailTest")
    public void removeReceivedEmailTest() {
        removeEmail();
        checkRemoveSuccess();
    }

    private void loginAsTestUser() {
        getById("identifierId").setValue(TEST_EMAIL);
        getById("identifierNext").click();
        wait.until(visibilityOf(getByAttribute("password", "type")));
        getByAttribute("password", "type").setValue(TEST_PASSWORD);
        getById("passwordNext").click();
        wait.until(visibilityOf(getByClassName("nM")));
    }

    private void checkLoginSuccess() {
        getByClassName("nM").shouldBe(visible);
    }

    private void createAndSendNewEmail(String email, String subject) {
        String body = "Test email";
        getByClassName("T-I-KE").click();
        wait.until(visibilityOf(getByAttribute("dialog", "role")));
        getByAttribute("to", "name").setValue(email);
        getByAttribute("subjectbox", "name").setValue(subject);
        getCollectionByClassName("editable").findBy(Condition.attribute("role", "textbox")).setValue(body);
        getCollectionByClassName("T-I-atl").filterBy(Condition.attribute("aria-label")).first().click();
    }

    private void checkEmailWasSent(String subject) {
        wait.until(visibilityOf(getCollectionByClassName("TO").findBy(Condition.text("Отправленные")))).click();
        getCollectionByTagName("td")
                .filterBy(Condition.text(subject))
                .shouldHaveSize(1);
    }

    private void checkEmailWasReceived(String subject) {
        wait.until(visibilityOf(getCollectionByClassName("TO").findBy(Condition.text("Входящие")))).click();
        getCollectionByTagName("td")
                .filterBy(Condition.text(subject))
                .shouldHaveSize(1);
    }

    private void removeEmail() {
        wait.until(visibilityOf(getCollectionByClassName("TO").findBy(Condition.text("Входящие")))).click();
        getCollectionByTagName("td")
                .filterBy(Condition.text(subject)).first().hover();
        getByAttribute("Удалить", "data-tooltip").click();
    }

    private void checkRemoveSuccess() {
        getCollectionByTagName("td")
                .filterBy(Condition.text(subject)).shouldHaveSize(0);
    }
}
