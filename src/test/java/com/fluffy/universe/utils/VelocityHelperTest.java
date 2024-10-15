package com.fluffy.universe.utils;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VelocityHelperTest {

    @Order(1)
    @Test
    void calculateMaxPageNumberTest1(){
        assertEquals(1,VelocityHelper.calculateMaxPageNumber(2,4));
    }
    @Order(3)
    @Test
    void calculateMaxPageNumberTest3(){
        assertEquals(3,VelocityHelper.calculateMaxPageNumber(9,3));
    }
    @Order(2)
    @Test
    void generatePaginationLinkTest(){
        assertEquals("localhost/?page=2&size=4",VelocityHelper.generatePaginationLink("localhost/",2,4));
    }
    @Order(4)
    @Test
    void calculateMaxPageNumberTest4(){
        assertThrows(NumberFormatException.class, () -> {
            VelocityHelper.calculateMaxPageNumber(10, Integer.parseInt("a"));
        });
    }
}
