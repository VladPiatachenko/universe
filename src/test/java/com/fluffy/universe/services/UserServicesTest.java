/**
 * test.java.com.fluffy.universe.services
 *  contains training unit tests for course.
 *
 * @since 1.1
 * @author Erlkonig
 * @version 1.1.1
 */
package com.fluffy.universe.services;

import com.fluffy.universe.utils.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
/*
*Tests for UserServices
*provide encoding tests for passwords
* */
public class UserServicesTest {
    /**
     *Loading configuartions
     *from application.properties.
     */
    @BeforeAll
    public static void setUpTestEnvironment() {
        Configuration.load(new File("application.properties"));
    }
    /**
     *Matching encoded password
     *to regular exprecion.
     */
    @Test
    public void encodePasswordTest() {
        String result = UserService.encodePassword("Password1!");
        assertTrue(result.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{60}$"));
    }
    /**
     *Test on correct encrypting.
     */
    @Test
    public void isCorrectPasswordTest() {
        assertTrue(UserService.isCorrectPassword("Password1!",
                        "$2a$10$"
                + "UZm.EaDD8VgKVcyVrUGiguWF"
                + "7ZYjsyzQlDy3cNcIiblDiHfhrUS7i"));
    }

}
