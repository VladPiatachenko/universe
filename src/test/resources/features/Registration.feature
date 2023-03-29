@Registration
Feature: Registration
  As an Un-registered User of the application
  I want to validate the Registration functionality
  In order to check if it works as desired

  Background: User navigates to Registration page
    Given I navigate to the "Registration" page

  @SuccessfulRegistration
  Scenario Outline: Successful Registration using valid credentials
    When I fill in "Firstname" with "<first-name>"
    And I fill in "Lastname" with "<last-name>"
    And I fill in "email" with "<email>"
    And I fill in "password" with "<password>"
    And I fill in "confirm password" with "<confirm password>"
    And I click on the "Register Now" button
    Then I should be successfully registered
    And I should land on the "Home" page
    And I should see "success" message as "Congrats! Your registration has been successful."
    And I should see "Dashboard" and "Logout" links
    Examples:
      | first-name | last-name | email                 | password  | confirm password |
      | asdf.asdf  | abcd.efgh | asdf.asdf@example.com | Asdf@1234 | Asdf@1234        |


  @DisabledRegistration
  Scenario Outline: Disabled Registration when one of the required fields is left blank
    When I fill in "Firstname" with "<first-name>"
    And I fill in "Lastname" with "<last-name>"
    And I fill in "email" with "<email>"
    And I fill in "password" with "<password>"
    And I fill in "confirm password" with "<confirm password>"
    And I click on the "Register Now" button
    Then I should see "<form error>" message for "<input field>" field on "Registration" page
    And I should see "Register Now" buttton disbaled
    And I should not be able to submit the "Registration" form
    Examples:
      | first-name | last-name | email                 | password  | confirm password | form error                                           | input field      |
      |            | bcde      | asdf.asdf@example.com | Asdf@1234 | Asdf@1234        | The firstname is required and cannot be empty        | Firstname        |
      | asdf       | bcde      |                       | Asdf@1234 | Asdf@1234        | The email is required and cannot be empty            | email            |
      | asdf       | bcde      | asdf                  | Asdf@1234 | Asdf@1234        | The email address is not valid                       | email            |
      | asdf       | bcde      | asdf.asdf@example.com | Asdf@1234 |                  | The confirm password is required and cannot be empty | confirm password |
      | asdf       | bcde      | asdf.asdf@example.com |           | Asdf@1234        | The password and its confirm are not the same        | confirm password |
      | asdf       |           | asdf.asdf@example.com | Asdf@1234 | Asdf@1234        | The lastname is required and cannot be empty         | Lastname         |