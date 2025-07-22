package com.framework.driver;

import com.framework.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);
    ConfigReader configReader = new ConfigReader();
    private DriverManager(){
        // Private constructor to prevent instantiation
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if(driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Please initialize the WebDriver before launching the browser.");
        }
        return driver;
    }

    public static WebDriver launchBrowser(){

        if(driverThreadLocal.get() != null) {
            log.warn("WebDriver already initialized for current thread. Re-using existing instance");
            return driverThreadLocal.get();
        }
        String browser = ConfigReader.getProperty("browser", "CHROME").toUpperCase();
        String platform = ConfigReader.getProperty("platform", "local").toUpperCase();
        WebDriver driver;
        if(platform.equalsIgnoreCase("local")) {
            driver = localWebdriver(browser);
        } else {
            driver = remoteWebDriver(browser);
        }

        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driverThreadLocal.set(driver);
        return driver;
    }

  public static void navigate(String url) {
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        getDriver().get(url);

    }
    public static WebDriver localWebdriver(String browser) {
        switch (browser) {

            case "CHROME":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (Boolean.parseBoolean((ConfigReader.getProperty("headless", "false")))) {
                    chromeOptions.addArguments("--headless=new", "--disable-gpu");
                }
                return new ChromeDriver(chromeOptions);
            case "FIREFOX":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (Boolean.parseBoolean((ConfigReader.getProperty("headless", "false")))) {
                    firefoxOptions.addArguments("--headless", "--disable-gpu");
                }
                return new FirefoxDriver(firefoxOptions);

            default:
                throw new RuntimeException("Invalid browser: " + browser);
        }
    }

    public  static WebDriver remoteWebDriver(String browser) {
            switch (browser){

                case "CHROME":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (Boolean.parseBoolean((ConfigReader.getProperty("headless", "false")))) {
                        chromeOptions.addArguments("--headless=new", "--disable-gpu");
                    }
                    return new ChromeDriver(chromeOptions);

                case "FIREFOX":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (Boolean.parseBoolean((ConfigReader.getProperty("headless", "false")))) {
                        firefoxOptions.addArguments("--headless", "--disable-gpu");
                    }
                    return new FirefoxDriver(firefoxOptions);

                default:
                    throw new RuntimeException("Invalid remote browser: " + browser);
            }
        }

    }

