package com.codecoverage.module1.ut;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.coveragedemo.module1.Sample1;


class Sample1Test {

    
 @Test
    void testReturnValue() throws Exception {
        assertEquals(10, new Sample1().getValue());
    }


}
