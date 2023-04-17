package com.fluffy.universe.e2esteps;
import io.cucumber.java8.En;import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;import org.openqa.selenium.firefox.FirefoxProfile;
import java.io.File;
import java.io.IOException;import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class e2eStepdefs implements En {
    WebDriver driver;
    Map<String,String> inputs=new HashMap<>();
    @BeforeAll
    void prepareInputs(){
        //System.setProperty("webdriver.gecko.driver","E:\\erlkonig\\geckodriver\\geckodriver.exe" );
        ChromeOptions options = new ChromeOptions();
        //options.setProfile(new ChromeProfile());
        driver = new ChromeDriver(options);
        inputs.put("Registration","http://127.0.0.1:7000/sign-up");
        inputs.put("Firstname","first-name");
        inputs.put("Lastname","last-name");
        inputs.put("email","email");
        inputs.put("password","password");
        inputs.put("confirm password","confirm-password");
        inputs.put("Register Now","/html/body/div[1]/main/div/form/div[6]/button");
        inputs.put("Home","http://127.0.0.1:7000/");

    }
    @BeforeEach    @io.cucumber.java.en.Given("^I navigate to the \"([^\"]*)\" page$")
    public void iNavigateToThePage(String arg0) throws Throwable {
        prepareInputs();
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        driver.get(inputs.get(arg0));
    }
    @io.cucumber.java.en.When("^I fill in \"([^\"]*)\" with \"([^\"]*)\"$")
    public void iFillInWith(String arg0, String arg1) throws Throwable {
        WebElement field=driver.findElement(By.name(inputs.get(arg0)));
        field.sendKeys(arg1);
    }
    @io.cucumber.java.en.And("^I click on the \"([^\"]*)\" button$")
    public void iClickOnTheButton(String arg0) throws Throwable {
        WebElement button = driver.findElement(By.xpath(inputs.get(arg0)));
        button.click();
    }
    @io.cucumber.java.en.Then("^I should be successfully registered$")
    public void iShouldBeSuccessfullyRegistered() throws IOException {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("src\\test\\resources\\screenshots\\SuccessfullyRegistered.png"));
    }
    @io.cucumber.java.en.And("^I should land on the \"([^\"]*)\" page$")
    public void iShouldLandOnThePage(String arg0) throws Throwable {
        String currentPage=driver.getCurrentUrl();
        assertEquals(currentPage,inputs.get(arg0));
    }
    @io.cucumber.java.en.And("^I should see \"([^\"]*)\" message as \"([^\"]*)\"$")
    public void iShouldSeeMessageAs(String arg0, String arg1) throws Throwable {
        WebElement alert=driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/h2"));
        assertEquals(arg0,alert.getText());
        alert=driver.findElement(By.xpath("/html/body/div[2]/div/div[2]"));
        assertEquals(arg1,alert.getText());
        driver.findElement(By.xpath("/html/body/div[2]/div/div[3]/button")).click();
    }
    @io.cucumber.java.en.And("^I should see \"([^\"]*)\" and \"([^\"]*)\" links$")
    public void iShouldSeeAndLinks(String arg0, String arg1) throws Throwable {
        driver.findElement(By.xpath("/html/body/header/nav/ul/li[2]")).click();
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("src\\test\\resources\\screenshots\\See"+arg0+"and"+arg1+".png"));        WebElement link1=driver.findElement(By.xpath("/html/body/header/nav/ul/li[2]/div/ul/li[2]/a"));
        WebElement link2=driver.findElement(By.xpath("/html/body/header/nav/ul/li[2]/div/ul/li[3]/form/button"));        assertEquals(arg0,link1.getText());
        assertEquals(arg1,link2.getText());
        link2.click();
        driver.close();
    }

    @io.cucumber.java.en.Then("^I should see \"([^\"]*)\" message for \"([^\"]*)\" field on \"([^\"]*)\" page$")
    public void iShouldSeeMessageForFieldOnPage(String arg0, String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }
    @io.cucumber.java.en.And("^I should see \"([^\"]*)\" buttton disbaled$")
    public void iShouldSeeButttonDisbaled(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }
    @io.cucumber.java.en.And("^I should not be able to submit the \"([^\"]*)\" form$")
    public void iShouldNotBeAbleToSubmitTheForm(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }
}