package com.codecoverage.module1.it;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.coveragedemo.module1.Sample1;

class Sample1IT {

    
	@Tag("integration")
	@Test
    void testReturnValue() throws Exception {
        assertEquals(20, new Sample1().getValue1());
    }


}