*** Settings ***
Library  SeleniumLibrary
Library  Screenshot  ${SCREENDIR}
*** Variables ***
${HOST}           127.0.0.1:7000
${LOGIN URL}      http://${HOST}/sign-in
${WELCOME URL}    http://${HOST}/
${BROWSER}        chrome
${VALID_EMAIL}    aassdff.asdf@example.com
${VALID_PASS}     Asdf@1234
${SCREENDIR}      src/test/resources/screenshots
***Test Cases***
Login
    Valid Login
***Keywords***
Valid Login
    Open Browser  ${LOGIN URL}  ${BROWSER}
    maximize browser window
    click element  xpath://*[@id="sign-in__email"]
    input text  id:sign-in__email  ${VALID_EMAIL}
    click element  xpath://*[@id="sign-in__password"]
    input text  id:sign-in__password  ${VALID_PASS}
    click element  xpath:/html/body/div[1]/main/div/form/div[5]/div/button
    Take Screenshot
    [Teardown]  Close Browser