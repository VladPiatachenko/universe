package com.fluffy.universe.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigTest {
    @BeforeAll
    static void setup(){
        Configuration.load(new File("application.properties"));
    }
    @Test
    void getHostTest(){
        Assertions.assertEquals("127.0.0.1",Configuration.get("application.host"));
        Assertions.assertEquals("7000",Configuration.get("application.port"));
    }

    @Test
    void errorReadFileText(){
        Assertions.assertThrows(RuntimeException.class , ()->Configuration.load(new File("not_A_file")));
    }
}
