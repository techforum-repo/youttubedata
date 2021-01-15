package com.coveragedemo.module2.ut;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.coveragedemo.module2.Sample2;


class Sample2Test {

    
 @Test
    void testReturnValue() throws Exception {
        assertEquals(10, new Sample2().getValue());
    }


}
