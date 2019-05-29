package com.bookinggo.annotationbasedexperiments.integration;

import com.bookinggo.annotationbasedexperiments.config.TestConfig;
import com.bookinggo.annotationbasedexperiments.experiment.AnnotationExperiment;
import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExperimentService;
import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExperimentToggleService;
import com.bookinggo.annotationbasedexperiments.services.impl.experimentframework.DummyInterface;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class AnnotationBasedExperimentsApplicationTest {
    @Autowired
    private DummyInterface dummyExperimental;
    @Autowired
    private AnnotationBasedExperimentService annotationBasedExperimentService;
    @Autowired
    private AnnotationBasedExperimentToggleService annotationBasedExperimentToggleService;

    @After
    public void tearDown() {
        annotationBasedExperimentService.removeAllExperiments();
        annotationBasedExperimentToggleService.removeAllApplicationProperties();
    }

    @Test
    public void shouldUseSubtractionWhenVariantIsB() {
        AnnotationExperiment annotationExperiment = new AnnotationExperiment(66666, "B", false);
        int randomNumber = (int) (Math.random() * 100);
        int otherRandomNumber = (int) (Math.random() * 100);

        annotationBasedExperimentService.addExperiment(annotationExperiment);
        annotationBasedExperimentToggleService.addApplicationProperty("number.of.beast.experiment", true);

        int result = dummyExperimental.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber - otherRandomNumber));

        annotationBasedExperimentService.getAnnotationExperiments().stream()
                .filter(currentExperiment -> currentExperiment.getId() == annotationExperiment.getId())
                .findFirst()
                .ifPresent(currentExperiment -> assertThat(currentExperiment.isImpacted(), is(true)));
    }

    @Test
    public void shouldUseAdditionWhenVariantIsA() {
        AnnotationExperiment annotationExperiment = new AnnotationExperiment(66666, "A", false);
        int randomNumber = (int) (Math.random() * 100);
        int otherRandomNumber = (int) (Math.random() * 100);

        annotationBasedExperimentService.addExperiment(annotationExperiment);
        annotationBasedExperimentToggleService.addApplicationProperty("number.of.beast.experiment", true);

        int result = dummyExperimental.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber + otherRandomNumber));

        annotationBasedExperimentService.getAnnotationExperiments().stream()
                .filter(currentExperiment -> currentExperiment.getId() == annotationExperiment.getId())
                .findFirst()
                .ifPresent(currentExperiment -> assertThat(currentExperiment.isImpacted(), is(true)));
    }

    @Test
    public void shouldUseAdditionWhenVariantIsBAndToggleIsOff() {
        AnnotationExperiment annotationExperiment = new AnnotationExperiment(66666, "B", false);
        int randomNumber = (int) (Math.random() * 100);
        int otherRandomNumber = (int) (Math.random() * 100);

        annotationBasedExperimentService.addExperiment(annotationExperiment);
        annotationBasedExperimentToggleService.addApplicationProperty("number.of.beast.experiment", false);

        int result = dummyExperimental.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber + otherRandomNumber));

        annotationBasedExperimentService.getAnnotationExperiments().stream()
                .filter(currentExperiment -> currentExperiment.getId() == annotationExperiment.getId())
                .findFirst()
                .ifPresent(currentExperiment -> assertThat(currentExperiment.isImpacted(), is(false)));
    }

}
