package com.bookinggo.annotationbasedexperiments.services.impl;

import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExpressionService;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class AnnotationBasedExpressionServiceImplTest {

    private AnnotationBasedExpressionService annotationBasedExpressionService;

    @Test
    public void expressionServiceMethodReturnsTrue(){
        annotationBasedExpressionService = new AnnotationBasedExpressionServiceImpl();
        assertThat(annotationBasedExpressionService.isLeadTimeLessThanTwentyFourHours(),is(true));
    }

}