package com.fluffy.universe.utils;

import com.fluffy.universe.exceptions.HttpException;
import io.javalin.http.HttpCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    void testIsValidPassword() {
        // Valid cases
        assertTrue(ValidationUtils.isValidPassword("Valid1@password"));
        assertTrue(ValidationUtils.isValidPassword("A1b2C3d$"));

        // Invalid cases
        assertFalse(ValidationUtils.isValidPassword("short1@"));         // Too short
        assertFalse(ValidationUtils.isValidPassword("nouppercase1@"));   // No uppercase
        assertFalse(ValidationUtils.isValidPassword("NOLOWERCASE1@"));   // No lowercase
        assertFalse(ValidationUtils.isValidPassword("NoNumber@"));       // No number
        assertFalse(ValidationUtils.isValidPassword("NoSpecial123"));    // No special character
        assertFalse(ValidationUtils.isValidPassword("TooLongPasswordWithSpecialChar1$aaaaaaaa"));  // Too long
    }

    @Test
    void testIsValidMail() {
        // Valid cases
        assertTrue(ValidationUtils.isValidMail("test@example.com"));
        assertTrue(ValidationUtils.isValidMail("user.name+tag+sorting@example.com"));
        assertTrue(ValidationUtils.isValidMail("email@subdomain.example.com"));

        // Invalid cases
        assertFalse(ValidationUtils.isValidMail("plainaddress"));                   // No domain
        assertFalse(ValidationUtils.isValidMail("@missingusername.com"));           // Missing username
        assertFalse(ValidationUtils.isValidMail("username@.com"));                  // Invalid domain
        assertFalse(ValidationUtils.isValidMail("user@localserver"));               // No top-level domain
        assertFalse(ValidationUtils.isValidMail("email@domain@domain.com"));        // Multiple '@'
        assertFalse(ValidationUtils.isValidMail("username@domain..com"));           // Double dot in domain
        assertFalse(ValidationUtils.isValidMail("toolong".repeat(40) + "@mail.com"));// Too long
    }

    @Test
    void testIsDateValid() {
        // Valid cases
        assertTrue(ValidationUtils.isDateValid("2024-02-29")); // Leap year
        assertTrue(ValidationUtils.isDateValid("2023-02-28")); // Non-leap year
        assertTrue(ValidationUtils.isDateValid("2023-12-31"));
        assertTrue(ValidationUtils.isDateValid("2023-01-01"));

        // Invalid cases
        assertFalse(ValidationUtils.isDateValid("2024-02-30")); // Invalid leap day
        assertFalse(ValidationUtils.isDateValid("2023-02-29")); // Invalid non-leap day
        assertFalse(ValidationUtils.isDateValid("2023-04-31")); // Invalid day in April
        assertFalse(ValidationUtils.isDateValid("2023-13-01")); // Invalid month
        assertFalse(ValidationUtils.isDateValid("abcd-12-31")); // Non-numeric year
        assertFalse(ValidationUtils.isDateValid("2023-12-32")); // Invalid day
        assertFalse(ValidationUtils.isDateValid("2023-00-10")); // Invalid zero month
    }

    @Test
    void testValidateParametersPresence() {
        // Valid case: all parameters are present
        assertDoesNotThrow(() -> ValidationUtils.validateParametersPresence("param1", "param2", 123, new Object()));

        // Invalid case: one parameter is null
        HttpException exception = assertThrows(HttpException.class, () -> {
            ValidationUtils.validateParametersPresence("param1", null, 123);
        });

        // Verify the exception code
        assertEquals(HttpCode.BAD_REQUEST, exception.getHttpCode());
    }
}
