package com.bookinggo.annotationbasedexperiments.services.impl;

import com.bookinggo.annotationbasedexperiments.experiment.AnnotationExperiment;
import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExperimentService;
import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExperimentToggleService;
import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExpressionService;
import com.bookinggo.annotationbasedexperiments.services.impl.experimentframework.DummyExperimental;
import com.bookinggo.annotationbasedexperiments.services.impl.experimentframework.DummyInterface;
import com.bookinggo.annotationbasedexperiments.services.impl.experimentframework.SearchContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.Silent.class)
public class AnnotationBasedAnnotationExperimentFrameworkServiceImplTest {
    private static final String NUMBER_OF_BEAST_EXPERIMENT = "number.of.beast.experiment";
    private static final String ALL_THE_ONES_EXPERIMENT = "all.the.ones.experiment";
    private static final String SINE_COSINE= "sine.cosine";

    @Mock
    private AnnotationBasedExperimentService annotationBasedExperimentService;
    @Mock
    private AnnotationBasedExperimentToggleService annotationBasedExperimentToggleService;

    @Mock
    private AnnotationBasedExpressionService annotationBasedExpressionService;

    private DummyInterface dummyExperimentalProxy;
    private SearchContext searchContext;

    @Before
    public void setUp() {
        searchContext = new SearchContext();
        AnnotationBasedExperimentFrameworkServiceImpl experimentFrameworkService = new AnnotationBasedExperimentFrameworkServiceImpl(annotationBasedExperimentService, annotationBasedExpressionService, annotationBasedExperimentToggleService);
        experimentFrameworkService.init();
        AspectJProxyFactory factory = new AspectJProxyFactory(new DummyExperimental());
        factory.addAspect(experimentFrameworkService);
        dummyExperimentalProxy = factory.getProxy();

    }

    @Test
    public void givenApplicationPropertyOffShouldNotDoExperiment(){
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(Collections.singletonList(new AnnotationExperiment(66666, "B", false)));
        when(annotationBasedExperimentToggleService.getExperimentToggle(NUMBER_OF_BEAST_EXPERIMENT)).thenReturn(false);
        int randomNumber = (int) (Math.random() * 100);
        int otherRandomNumber = (int) (Math.random() * 100);

        int result = dummyExperimentalProxy.addition(randomNumber, otherRandomNumber);
        verify(annotationBasedExperimentService,times(0)).impact(66666);
        assertThat(result, is(randomNumber + otherRandomNumber));
    }

    @Test
    public void shouldRedirectDueToSubtractExperimentVariantB_beingPresent() {
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(Collections.singletonList(new AnnotationExperiment(66666, "B", false)));
        when(annotationBasedExperimentToggleService.getExperimentToggle(NUMBER_OF_BEAST_EXPERIMENT)).thenReturn(true);

        int randomNumber = 100;
        int otherRandomNumber = 100;

        int result = dummyExperimentalProxy.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber - otherRandomNumber));

    }

    @Test
    public void shouldRedirectDueToMultiplicationExperimentVariantC_beingPresent() {
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(Collections.singletonList(new AnnotationExperiment(66666, "C", false)));
        when(annotationBasedExperimentToggleService.getExperimentToggle(NUMBER_OF_BEAST_EXPERIMENT)).thenReturn(true);

        int randomNumber = (int) (Math.random() * 100);
        int otherRandomNumber = (int) (Math.random() * 100);

        int result = dummyExperimentalProxy.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber * otherRandomNumber));

    }

    @Test
    public void shouldRedirectDueToDivisionExperimentVariantD_beingPresent() {
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(Collections.singletonList(new AnnotationExperiment(66666, "D", false)));
        when(annotationBasedExperimentToggleService.getExperimentToggle(NUMBER_OF_BEAST_EXPERIMENT)).thenReturn(true);

        int randomNumber = (int) (Math.random() * 100);
        int otherRandomNumber = (int) (Math.random() * 100);

        int result = dummyExperimentalProxy.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber / otherRandomNumber));
    }

    @Test
    public void shouldRunAVariantWhenExperimentsAreMissing() {
        int randomNumber = (int) (Math.random() * 100);
        int otherRandomNumber = (int) (Math.random() * 100);

        int result = dummyExperimentalProxy.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber + otherRandomNumber));
    }

    @Test
    public void shouldMarkTestAsImpactedWhenExperimentHasBeenUsed() {
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(Collections.singletonList(new AnnotationExperiment(66666, "D", false)));
        when(annotationBasedExperimentToggleService.getExperimentToggle(NUMBER_OF_BEAST_EXPERIMENT)).thenReturn(true);

        int randomNumber = (int) (Math.random() * 100);
        int otherRandomNumber = (int) (Math.random() * 100);

        int result = dummyExperimentalProxy.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber / otherRandomNumber));
        verify(annotationBasedExperimentService, times(1)).impact(66666);
    }

    @Test
    public void shouldMarkTestAsImpactedWhenExperimentAHasBeenUsed() {
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(Collections.singletonList(new AnnotationExperiment(66666, "A", false)));
        when(annotationBasedExperimentToggleService.getExperimentToggle(NUMBER_OF_BEAST_EXPERIMENT)).thenReturn(true);

        int randomNumber = (int) (Math.random() * 100);
        int otherRandomNumber = (int) (Math.random() * 100);

        int result = dummyExperimentalProxy.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber + otherRandomNumber));
        verify(annotationBasedExperimentService, times(1)).impact(66666);
    }

    @Test
    public void shouldNotMarkTestAsImpactedWhenNoExperimentsArePresent() {
        int randomNumber = (int) (Math.random() * 100);
        int otherRandomNumber = (int) (Math.random() * 100);

        int result = dummyExperimentalProxy.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber + otherRandomNumber));
        verify(annotationBasedExperimentService, times(0)).impact(66666);
    }

    @Test
    public void usesCorrectMethodWhenMultipleExperimentsAreOnTheSameClass(){
        List<AnnotationExperiment> annotationExperiments = new ArrayList<>();
        annotationExperiments.add(new AnnotationExperiment(66666, "C",false));
        annotationExperiments.add(new AnnotationExperiment(11111,"B",false));
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(annotationExperiments);
        when(annotationBasedExperimentToggleService.getExperimentToggle(NUMBER_OF_BEAST_EXPERIMENT)).thenReturn(true);
        when(annotationBasedExperimentToggleService.getExperimentToggle(ALL_THE_ONES_EXPERIMENT)).thenReturn(true);


        double randomNumber =  (Math.random() * 100);
        int otherRandomNumber = (int) (Math.random() * 100);

        double result = dummyExperimentalProxy.pow(randomNumber,otherRandomNumber);

        assertThat(result, is(Math.max(randomNumber,otherRandomNumber)));


    }

    @Test
    public void withMultipleExperimentsOnlyTheCorrectExperimentIsImpacted(){
        List<AnnotationExperiment> annotationExperiments = new ArrayList<>();
        annotationExperiments.add(new AnnotationExperiment(66666, "C",false));
        annotationExperiments.add(new AnnotationExperiment(11111,"B",false));
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(annotationExperiments);
        when(annotationBasedExperimentToggleService.getExperimentToggle(NUMBER_OF_BEAST_EXPERIMENT)).thenReturn(true);
        when(annotationBasedExperimentToggleService.getExperimentToggle(ALL_THE_ONES_EXPERIMENT)).thenReturn(true);

        double randomNumber =  (Math.random() * 100);
        int otherRandomNumber = (int) (Math.random() * 100);

        double result = dummyExperimentalProxy.pow(randomNumber,otherRandomNumber);

        assertThat(result, is(Math.max(randomNumber,otherRandomNumber)));
        verify(annotationBasedExperimentService,times(1)).impact(11111);
    }

    @Test
    public void whenExpressionIsFalseUseAVariant(){
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(2);
        searchContext.setPickTime(localDateTime);
        List<AnnotationExperiment> annotationExperiments = new ArrayList<>();
        annotationExperiments.add(new AnnotationExperiment(3142, "B",false));
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(annotationExperiments);
        when(annotationBasedExperimentToggleService.getExperimentToggle(SINE_COSINE)).thenReturn(true);
        when(annotationBasedExpressionService.isLeadTimeLessThanTwentyFourHours()).thenReturn(false);


        double randomNum = Math.random() * 100.0;

        double result = dummyExperimentalProxy.sine(randomNum);

        assertThat(result, is(Math.sin(randomNum)));

    }

    @Test
    public void whenExpressionIsTrueUseBVariant(){
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        searchContext.setPickTime(localDateTime);
        List<AnnotationExperiment> annotationExperiments = new ArrayList<>();
        annotationExperiments.add(new AnnotationExperiment(3142, "B",false));
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(annotationExperiments);
        when(annotationBasedExperimentToggleService.getExperimentToggle(SINE_COSINE)).thenReturn(true);
        when(annotationBasedExpressionService.isLeadTimeLessThanTwentyFourHours()).thenReturn(true);


        double randomNum = Math.random() * 100.0;

        double result = dummyExperimentalProxy.sine(randomNum);

        assertThat(result, is(Math.cos(randomNum)));

    }

    @Test
    public void whenExpressionIsTrueUseBVariantAndImpactExperiment(){
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        searchContext.setPickTime(localDateTime);
        List<AnnotationExperiment> annotationExperiments = new ArrayList<>();
        annotationExperiments.add(new AnnotationExperiment(3142, "B",false));
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(annotationExperiments);
        when(annotationBasedExperimentToggleService.getExperimentToggle(SINE_COSINE)).thenReturn(true);
        when(annotationBasedExpressionService.isLeadTimeLessThanTwentyFourHours()).thenReturn(true);

        double randomNum = Math.random() * 100.0;

        dummyExperimentalProxy.sine(randomNum);

        verify(annotationBasedExperimentService,times(1)).impact(3142);

    }

    @Test
    public void whenExpressionIsTrueUseAVariantAndImpactExperiment(){
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        searchContext.setPickTime(localDateTime);
        List<AnnotationExperiment> annotationExperiments = new ArrayList<>();
        annotationExperiments.add(new AnnotationExperiment(3142, "A",false));
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(annotationExperiments);
        when(annotationBasedExperimentToggleService.getExperimentToggle(SINE_COSINE)).thenReturn(true);
        when(annotationBasedExpressionService.isLeadTimeLessThanTwentyFourHours()).thenReturn(true);


        double randomNum = Math.random() * 100.0;

        dummyExperimentalProxy.sine(randomNum);

        verify(annotationBasedExperimentService,times(1)).impact(3142);

    }

    @Test
    public void whenExpressionIsFalseUseAVariantAndNotImpact(){
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(2);
        searchContext.setPickTime(localDateTime);
        List<AnnotationExperiment> annotationExperiments = new ArrayList<>();
        annotationExperiments.add(new AnnotationExperiment(3142, "B",false));
        when(annotationBasedExperimentService.getAnnotationExperiments()).thenReturn(annotationExperiments);
        when(annotationBasedExperimentToggleService.getExperimentToggle(SINE_COSINE)).thenReturn(true);

        double randomNum = Math.random() * 100.0;

        dummyExperimentalProxy.sine(randomNum);

        verify(annotationBasedExperimentService,times(0)).impact(3142);

    }

}





