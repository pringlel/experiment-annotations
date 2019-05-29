package com.bookinggo.annotationbasedexperiments.integration;

import com.bookinggo.annotationbasedexperiments.config.TestConfig;
import com.bookinggo.annotationbasedexperiments.experiment.Experiment;
import com.bookinggo.annotationbasedexperiments.experiment.ExperimentVariant;
import com.bookinggo.annotationbasedexperiments.services.ExperimentService;
import com.bookinggo.annotationbasedexperiments.services.ExperimentToggleService;
import com.bookinggo.annotationbasedexperiments.services.impl.ExperimentServiceImpl;
import com.bookinggo.annotationbasedexperiments.services.impl.experimentframework.DummyExperimental;
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
    private ExperimentService experimentService;
    @Autowired
    private ExperimentToggleService experimentToggleService;

    @After
    public void tearDown(){
        experimentService.removeAllExperiments();
        experimentToggleService.removeAllApplicationProperties();
    }

    @Test
    public void shouldUseSubtractionWhenVariantIsB(){
        Experiment experiment = new Experiment(66666, "B",false);
        int randomNumber = (int)(Math.random() * 100);
        int otherRandomNumber = (int)(Math.random() * 100);

        experimentService.addExperiment(experiment);
        experimentToggleService.addApplicationProperty("number.of.beast.experiment",true);

        int result = dummyExperimental.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber - otherRandomNumber));

        experimentService.getExperiments().stream()
                .filter(currentExperiment -> currentExperiment.getId() == experiment.getId() )
                .findFirst()
                .ifPresent(currentExperiment ->  assertThat(currentExperiment.isImpacted(), is(true)));
    }

    @Test
    public void shouldUseAdditionWhenVariantIsA(){
        Experiment experiment = new Experiment(66666, "A",false);
        int randomNumber = (int)(Math.random() * 100);
        int otherRandomNumber = (int)(Math.random() * 100);

        experimentService.addExperiment(experiment);
        experimentToggleService.addApplicationProperty("number.of.beast.experiment",true);

        int result = dummyExperimental.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber + otherRandomNumber));

        experimentService.getExperiments().stream()
                .filter(currentExperiment -> currentExperiment.getId() == experiment.getId() )
                .findFirst()
                .ifPresent(currentExperiment ->  assertThat(currentExperiment.isImpacted(), is(true)));
    }

    @Test
    public void shouldUseAdditionWhenVariantIsBAndToggleIsOff(){
        Experiment experiment = new Experiment(66666, "B",false);
        int randomNumber = (int)(Math.random() * 100);
        int otherRandomNumber = (int)(Math.random() * 100);

        experimentService.addExperiment(experiment);
        experimentToggleService.addApplicationProperty("number.of.beast.experiment",false);

        int result = dummyExperimental.addition(randomNumber, otherRandomNumber);
        assertThat(result, is(randomNumber + otherRandomNumber));

        experimentService.getExperiments().stream()
                .filter(currentExperiment -> currentExperiment.getId() == experiment.getId() )
                .findFirst()
                .ifPresent(currentExperiment ->  assertThat(currentExperiment.isImpacted(), is(false)));
    }

}
