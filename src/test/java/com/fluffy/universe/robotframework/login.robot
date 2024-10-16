*** Settings ***
Library  SeleniumLibrary
Library  Screenshot  ${SCREENDIR}
Library  OperatingSystem

*** Variables ***
${HOST}             127.0.0.1:7000
${LOGIN URL}        http://${HOST}/sign-in
${WELCOME URL}      http://${HOST}/
${BROWSER}          Chrome
${VALID_EMAIL}      potter@hogwarts.uk
${VALID_PASS}       Password1!
${SCREENDIR}        src/test/resources/screenshots

# Locators
${EMAIL_INPUT}      xpath://*[@id="sign-in__email"]
${PASSWORD_INPUT}   xpath://*[@id="sign-in__password"]
${SIGN_IN_BUTTON}   xpath:/html/body/div[1]/main/div/form/div[5]/div/button
${PROFILE_ICON}     xpath:/html/body/header/nav/ul/li[2]/div/button/img
${SUCCESS_ELEMENT}  New post

# Error messages
${INVALID_EMAIL_ERROR}     Invalid email or password. Please check your input and try again.
${INVALID_PASSWORD_ERROR}   Invalid email or password. Please check your input and try again.

*** Test Cases ***
# Positive Test Case
Login
    [Documentation]  Test valid login
    Valid Login

# Negative Test Case 1: Invalid Email Format
Login With Invalid Email Format
    [Documentation]  Attempt login with an improperly formatted email address.
    Open Browser  ${LOGIN URL}  ${BROWSER}
    Maximize Browser Window
    Input Credentials Invalid Email
    Submit Form
    Verify Invalid Email Error
    Take Screenshot
    [Teardown]  Close All Browsers

# Negative Test Case 2: Incorrect Password
Login With Incorrect Password
    [Documentation]  Attempt login with the correct email but an incorrect password.
    Open Browser  ${LOGIN URL}  ${BROWSER}
    Maximize Browser Window
    Input Credentials Invalid Password
    Submit Form
    Verify Incorrect Password Error
    Take Screenshot
    [Teardown]  Close All Browsers


*** Keywords ***
# Valid Login
Valid Login
    Open Browser  ${LOGIN URL}  ${BROWSER}
    Maximize Browser Window
    Input Credentials
    Submit Form
    Verify Login Success
    Take Screenshot
    [Teardown]  Close All Browsers

# Input Credentials with valid email and password
Input Credentials
    Input Text  ${EMAIL_INPUT}  ${VALID_EMAIL}
    Input Text  ${PASSWORD_INPUT}  ${VALID_PASS}

# Input Credentials with invalid email format
Input Credentials Invalid Email
    Input Text  ${EMAIL_INPUT}  invalid@email.ml
    Input Text  ${PASSWORD_INPUT}  ${VALID_PASS}

# Input Credentials with valid email and incorrect password
Input Credentials Invalid Password
    Input Text  ${EMAIL_INPUT}  ${VALID_EMAIL}
    Input Text  ${PASSWORD_INPUT}  wrongpassword123

# Submit login form
Submit Form
    Click Element  ${SIGN_IN_BUTTON}

# Verification for valid login
Verify Login Success
    Wait Until Page Contains  ${SUCCESS_ELEMENT}
    Click Element  ${PROFILE_ICON}

# Verification for invalid email error
Verify Invalid Email Error
    Wait Until Page Contains  ${INVALID_EMAIL_ERROR}

# Verification for incorrect password error
Verify Incorrect Password Error
    Wait Until Page Contains  ${INVALID_PASSWORD_ERROR}