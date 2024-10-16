package com.fluffy.universe.e2esteps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluffy.universe.controllers.UserControllerIntegrationTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class e2eStepdefs {
    WebDriver driver;
    private static Map<String, String> selectors;
    String currentemail="";

    void prepareInputs() {
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver(options);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            selectors = objectMapper.readValue(new File("src/test/resources/CucumberTable/elementParser.json"), new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Before
    public void setUp() {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
    }

    @Given("I navigate to the {string} page")
    public void iNavigateToThePage(String page) {
        prepareInputs();
        driver.get(selectors.get("host") + page);
    }

    @When("I fill in {string} with {string}")
    public void iFillInWith(String field, String value) {
        driver.findElement(By.name(field)).sendKeys(value);
        if(field.equals("email")){currentemail=value;}
    }

    @And("I click on the {string} button")
    public void iClickOnTheButton(String button) {
        WebElement btn = driver.findElement(By.xpath(selectors.get(button)));
        btn.click();
    }

    @Then("I should be successfully registered")
    public void iShouldBeSuccessfullyRegistered() throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("src/test/resources/screenshots/SuccessfullyRegistered.png"));
    }

    @And("I should land on the {string} page")
    public void iShouldLandOnThePage(String page) {
        String currentPage = driver.getCurrentUrl();
        assertEquals(selectors.get("host") + selectors.get(page), currentPage);
    }

    @And("I should see message {string} and {string} and {string}")
    public void iShouldSeeMessageAs(String message1, String message2, String button) {
        WebDriverWait wait = new WebDriverWait(driver, 3L);
        WebElement alertElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert")));

        WebElement titleElement = driver.findElement(By.cssSelector(".alert__heading"));
        WebElement descriptionElement = driver.findElement(By.cssSelector(".alert__description"));

        String actualTitle = titleElement.getText();
        String actualDescription = descriptionElement.getText();
        assertEquals(message1,actualTitle);
        assertEquals(message2,actualDescription);

        driver.findElement(By.xpath(selectors.get(button))).click();
    }

    @And("I should see {string} and {string} links")
    public void iShouldSeeAndLinks(String link1, String link2) throws IOException {
        driver.findElement(By.xpath(selectors.get("UserPic"))).click();
        driver.findElement(By.xpath(selectors.get("UserDroplist"))).click();
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("src/test/resources/screenshots/See" + link1 + "and" + link2 + ".png"));
        WebElement linkElement1 = driver.findElement(By.xpath(selectors.get(link1)));
        WebElement linkElement2 = driver.findElement(By.xpath(selectors.get(link2)));
        assertEquals(link1, linkElement1.getText());
        assertEquals(link2, linkElement2.getText());
        linkElement2.click();
        driver.close();
    }
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
        UserControllerIntegrationTest.setup();
        UserControllerIntegrationTest.deleteUserFromDatabase(currentemail);

    }

}
