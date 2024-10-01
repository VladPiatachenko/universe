package com.fluffy.universe.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VelocityHelperTest {
    /*  public static int calculateMaxPageNumber(int recordCount, int pageSize) {
        return (int) Math.round(Math.ceil(recordCount / (double) pageSize));
    }
    */
    @Test
    void calculateMaxPageNumberTest(){
        assertEquals(1,VelocityHelper.calculateMaxPageNumber(2,4));
    }
    @Test
    void generatePaginationLinkTest(){
        assertEquals("localhost/?page=2&size=4",VelocityHelper.generatePaginationLink("localhost/",2,4));
    }
}
