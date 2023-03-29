package com.fluffy.universe.e2esteps;

import io.cucumber.java8.En;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class e2eStepdefs implements En {

   WebDriver driver = new FirefoxDriver();
   Map<String,String> inputs=new HashMap<>();

    @io.cucumber.java.en.Given("^I navigate to the \"([^\"]*)\" page$")
    public void iNavigateToThePage(String arg0) throws Throwable {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        inputs.put("Registration","http://127.0.0.1:7000/sign-up");
        inputs.put("FirstName","sign-up__first-name");
        inputs.put("LastName","sign-up__last-name");
        inputs.put("email","sign-up__email");
        inputs.put("password","sign-up__password");
        inputs.put("confirm password","sign-up__confirm-password");
        inputs.put("Register Now","/html/body/div[1]/main/div/form/div[6]/button");
        inputs.put("Home","http://127.0.0.1:7000/");
        open(inputs.get(arg0));
        Thread.sleep(3000);
    }

    @io.cucumber.java.en.When("^I fill in \"([^\"]*)\" with \"([^\"]*)\"$")
    public void iFillInWith(String arg0, String arg1) throws Throwable {
        $(byName(inputs.get(arg0))).setValue(arg1);
    }

    @io.cucumber.java.en.And("^I click on the \"([^\"]*)\" button$")
    public void iClickOnTheButton(String arg0) throws Throwable {
        $(byXpath(inputs.get(arg0))).click();
    }

    @io.cucumber.java.en.Then("^I should be successfully registered$")
    public void iShouldBeSuccessfullyRegistered() {
    }

    @io.cucumber.java.en.And("^I should land on the \"([^\"]*)\" page$")
    public void iShouldLandOnThePage(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions

    }

    @io.cucumber.java.en.And("^I should see \"([^\"]*)\" message as \"([^\"]*)\"$")
    public void iShouldSeeMessageAs(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions

    }

    @io.cucumber.java.en.And("^I should see \"([^\"]*)\" and \"([^\"]*)\" links$")
    public void iShouldSeeAndLinks(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions

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
