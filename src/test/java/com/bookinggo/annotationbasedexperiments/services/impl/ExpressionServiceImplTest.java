package com.bookinggo.annotationbasedexperiments.services.impl;

import com.bookinggo.annotationbasedexperiments.services.ExpressionService;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ExpressionServiceImplTest {

    private ExpressionService expressionService;

    @Test
    public void expressionServiceMethodReturnsTrue(){
        expressionService = new ExpressionServiceImpl();
        assertThat(expressionService.isLeadTimeLessThanTwentyFourHours(),is(true));
    }

}