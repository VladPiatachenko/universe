package com.fluffy.universe.e2e;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @Profile
 * Feature: View/Edit Profile
 *   As a Registered User of the application
 *   I want to view and update my profile
 */
public class Profilee2eTest {
    static WebDriver driver;
    static String loginUrl="http://127.0.0.1:7000/sign-in";
    static String testEmail="potter@hogwarts.uk";
    static String testpass="Password1!";
    static String homepage="http://127.0.0.1:7000/";
    static String profilePage="http://127.0.0.1:7000/account";
    /**
     * Background: User is logged in and is on Homepage
     * Given I am a logged in user
     * And I navigate to the "Homepage" page
     */
    @BeforeEach
    void BackgroundPrep(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        //options.addArguments("--headless");
        //options.addArguments("--no-sandbox");
        //options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.get(loginUrl);
        driver.findElement(By.id("sign-in__email")).sendKeys(testEmail);
        driver.findElement(By.id("sign-in__password")).sendKeys(testpass);
        driver.findElement(By.xpath("/html/body/div[1]/main/div/form/div[5]/div/button")).click();
        assertEquals(driver.getCurrentUrl(),homepage);
    }
    /**
     * Scenario: Successful View Profile
     * When I click on "My Profile" link on the "Home" page
     * Then I should land on the "My Profile" page
     * And I should see "Your Profile" heading on the Profile page
     * And "My Profile" link should be active on the Profile page
     * And "User Name" field should be prepopulated and set as "readonly" on the Profile page
     * And "email" field should be prepopulated on the Profile page
     */
    @Test
    void SuccessfulViewProfileTest(){
        Actions action = new Actions(driver);
        WebElement usr = driver.findElement(By.xpath("/html/body/header/nav/ul/li[2]/div/button/img"));
        action.moveToElement(usr).moveToElement(driver.findElement(By.xpath("/html/body/header/nav/ul/li[2]/div/ul/li[1]/a"))).click().build().perform();
        assertEquals(driver.getCurrentUrl(),profilePage);
        assertEquals(driver.findElement(By.xpath("/html/body/main/div/form/div[1]/h1")).getText(),"Account");
        assertNotEquals(driver.findElement(By.id("account__first-name")).getAttribute("value"),"");
        assertNotEquals(driver.findElement(By.id("account__last-name")).getAttribute("value"),"");
        assertNotEquals(driver.findElement(By.id("account__email")).getAttribute("value"),"");//negative test on account_address
        driver.close();
    }
    /**
     *    Scenario Outline: Successful Edit Profile
     * 	  When I click on "My Profile" link on the "Home" page
     * 	  And I fill in First Name as "<firstname>"
     * 	  And I fill in Last Name as "<lastname>"
     * 	  And I fill in Age as "<age>"//account__birthday
     * 	  And I fill in Gender as "<gender>"
     * 	  And I fill in Address as "<address>"
     * 	  And I fill in Website as "<website>"
     * 	  And I click on the "Update Profile" button
     * 	  Then I should land on the "Home" page//not in this project
     * 	  And I should see "success" message as "Profile updated successfully!"
     */
    @Test
    void SuccessfulEditProfileTest() throws IOException {
        Actions action = new Actions(driver);
        WebElement usr = driver.findElement(By.xpath("/html/body/header/nav/ul/li[2]/div/button/img"));
        action.moveToElement(usr).moveToElement(driver.findElement(By.xpath("/html/body/header/nav/ul/li[2]/div/ul/li[1]/a"))).click().build().perform();
        driver.findElement(By.id("account__first-name")).sendKeys("ashis");
        driver.findElement(By.id("account__last-name")).sendKeys("raj");
        driver.findElement(By.id("account__birthday")).sendKeys("25.07.1994");
        driver.findElement(By.id("account__gender")).sendKeys("Male");
        driver.findElement(By.id("account__address")).sendKeys("E-605");
        driver.findElement(By.id("account__website")).sendKeys("https://example.com");
        driver.findElement(By.xpath("/html/body/main/div/form/div[8]/button")).click();
        assertEquals(driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/h2")).getText(),"Congratulations!");
        assertEquals(driver.findElement(By.xpath("/html/body/div[2]/div/div[2]")).getText(),"User account data updated successfully.");
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("src\\test\\resources\\screenshots\\SuccessfulEditProfile.png"));
        driver.close();
    }
}
