package com.fluffy.universe.e2esteps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluffy.universe.controllers.UserController;
import com.fluffy.universe.controllers.UserControllerIntegrationTest;
import com.fluffy.universe.utils.Configuration;
import com.fluffy.universe.utils.DataSource;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.javalin.Javalin;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sql2o.Connection;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class e2eStepdefs {
    private static WebDriver driver;
    private static Map<String, String> selectors;
    String currentemail;

    void prepareInputs() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            selectors = objectMapper.readValue(new File("src/test/resources/CucumberTable/elementParser.json"), new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentemail="";
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
    public void iShouldSeeMessageAsAndAnd(String message1, String message2, String button) {
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(2L));
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
    static int port=7001;
    @After
    public void tearDown() {
        Connection connection;
        UserController userController;
        Javalin app;
        if (driver != null) {
            driver.quit();
            driver = null;
        }
        if(currentemail!=null) {
            Configuration.load(new File("application.properties"));

            // Create SQLite connection
            try {
                connection = DataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Initialize a minimal Javalin application for testing
            app = Javalin.create().start(port++); // or any port, won't actually be used

            // Create UserController instance with the app
            userController = new UserController(app);
            UserControllerIntegrationTest.deleteUserFromDatabase(currentemail);
        }
    }

    @And("I should not be able to submit the {string} form")
    public void iShouldNotBeAbleToSubmitTheForm(String arg0) {

    }

    @Then("I should see {string} message for {string} field on {string} page")
    public void iShouldSeeMessageForFieldOnPage(String arg0, String arg1, String arg2) {

    }

    @And("I should see {string} button disbaled")
    public void iShouldSeeButtonDisbaled(String arg0) {

    }

    @Then("I should be successfully logged in")
    public void iShouldBeSuccessfullyLoggedIn() {

    }

    @Then("I should be redirected on the {string} page")
    public void iShouldBeRedirectedOnThePage(String arg0) {

    }

    @And("I should see {string} message as {string}")
    public void iShouldSeeMessageAs(String arg0, String arg1, String arg2) {
    }
}
