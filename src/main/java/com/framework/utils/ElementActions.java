package com.framework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public class ElementActions  {

    private static final Logger log= LoggerFactory.getLogger(ElementActions.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final int maxRetries = 3;
    public ElementActions(WebDriver driver) {
        // Constructor can be used for initialization if needed
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    public WebElement getElement(By locator){
        for (int i = 0; i < maxRetries; i++) {
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                highlightElement(element);
                return element;
            } catch (StaleElementReferenceException e) {
                log.warn("Attempt {} - StaleElementReferenceException for locator: {}. Retrying...", i, locator);
                if (i == 2) {
                    throw new ElementNotVisibleException("Element remained stale after retries: " + locator, e);
                }
            } catch (TimeoutException | NoSuchElementException e) {
                log.error("Element not found: {}", locator, e);
                throw new ElementNotVisibleException("Element could not be found: " + locator, e);
            }catch (Exception e) {
                log.error("Unexpected error for locator: {}", locator, e);
                throw e;
            }
        }

        throw new RuntimeException("Unexpected error occurred while getting element: " + locator);
    }

    public void highlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style.border='2px solid red'", element);
    }
    public void click(By locator) {
            getElement(locator).click();;
            log.info("Successfully clicked on element: {}", locator);
    }

    public void sendKeys(By locator, CharSequence... keysToSend) {
            getElement(locator).clear();
            getElement(locator).sendKeys(keysToSend);
            log.info("Successfully sent keys '{}' to element: {}", keysToSend, locator);

    }

    public boolean isDisplayed(By locator) {

            log.debug("Checking if element is displayed: {}", locator);
            try {
                WebElement element = getElement(locator);
                return element.isDisplayed();
            } catch (TimeoutException | NoSuchElementException e) {
                log.warn("Element not displayed: {}", locator);
                return false;
            } catch (Exception e) {
                log.error("Unexpected error checking if element is displayed: {}", locator, e);
                return false;
            }

    }

    public String getText(By locator) {
        return getElement(locator).getText();
    }

    public void selectByValue(By locator, String value) {
        Select select = new Select(getElement(locator));
        select.selectByValue(value);
        log.info("Selected option with value '{}' from dropdown: {}", value, locator);
    }

    public void selectByVisibleText(By locator, String visibleText) {
        Select select = new Select(getElement(locator));
        select.selectByVisibleText(visibleText);
        log.info("Selected option with visible text '{}' from dropdown: {}", visibleText, locator);
    }

    public void selectByIndex(By locator, int index) {
        Select select = new Select(getElement(locator));
        select.selectByIndex(index);
        log.info("Selected option at index '{}' from dropdown: {}", index, locator);
    }


}
