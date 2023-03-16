package com.fluffy.universe.services;

import com.fluffy.universe.utils.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServicesTest {
    @BeforeAll
    public static void setUpTestEnvironment(){
        Configuration.load(new File("application.properties"));
    }
    @Test
    public void encodePasswordTest(){
        String result=UserService.encodePassword("Password1!");
        assertTrue(result.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{60}$"));
    }
    @Test
    public void isCorrectPasswordTest(){
        assertTrue(UserService.isCorrectPassword("Password1!",
                        "$2a$10$UZm.EaDD8VgKVcyVrUGiguWF7ZYjsyzQlDy3cNcIiblDiHfhrUS7i"));
    }

}
