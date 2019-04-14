package gmail;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public abstract class BaseTest {
    private static final int SLEEP_2000 = 2000;
    protected static WebDriverWait wait;


    @BeforeTest
    @Parameters({"browser"})
    protected void beforeTest(@Optional("firefox") String suiteBrowserName) {
        setLocalDriver(suiteBrowserName);
    }

    @AfterTest(alwaysRun = true)
    protected void destroy() {
        try {
            getWebDriver().quit();
            sleep(SLEEP_2000);
        } catch (WebDriverException e) {
            System.out.println("***** I can't close browser *****");
            e.printStackTrace();
        }
    }


    //configuration for local web driver
    private RemoteWebDriver setLocalDriver(String browserName) {
        RemoteWebDriver driver;
        BrowserName browser = BrowserName.fromName(browserName);
        switch (browser) {
            case CHROME: {
                driver = setChromeDriverConfiguration();
                break;
            }
            case FIREFOX: {
                driver = setFirefoxDriverConfiguration();
                break;
            }
            case EDGE: {
                driver = setEdgeDriverConfiguration();
                sleep(3000);
                break;
            }
            default: {
                driver = setChromeDriverConfiguration();
                break;
            }
        }
        driver.manage().window().maximize();
        WebDriverRunner.setWebDriver(driver);
        return driver;
    }

    private RemoteWebDriver setChromeDriverConfiguration() {
        WebDriverManager.chromedriver().version("73.0.3683.68").setup();
        RemoteWebDriver driver = new ChromeDriver();
        Configuration.browser = BrowserName.CHROME.getName();
        return driver;
    }

    private RemoteWebDriver setEdgeDriverConfiguration() {
        WebDriverManager.edgedriver().setup();
        RemoteWebDriver driver = new EdgeDriver();
        Configuration.browser = BrowserName.EDGE.getName();
        return driver;
    }

    private static RemoteWebDriver setFirefoxDriverConfiguration() {
        WebDriverManager.firefoxdriver().setup();
        RemoteWebDriver driver = new FirefoxDriver();
        Configuration.browser = BrowserName.FIREFOX.getName();
        return driver;
    }

    private enum BrowserName {

        CHROME("chrome"),
        FIREFOX("firefox"),
        EDGE("edge");

        private final String name;

        BrowserName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static BrowserName fromName(String name) {
            for(BrowserName browserName: BrowserName.values()) {
                if(browserName.getName().equalsIgnoreCase(name)) {
                    return browserName;
                }
            }
            return null;
        }
    }

}
