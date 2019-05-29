package com.bookinggo.annotationbasedexperiments.beanfactory;

import com.bookinggo.annotationbasedexperiments.config.AppConfig;
import com.bookinggo.annotationbasedexperiments.config.TestConfig;
import com.bookinggo.annotationbasedexperiments.experiment.Experiment;
import com.bookinggo.annotationbasedexperiments.services.ExperimentService;
import com.bookinggo.annotationbasedexperiments.testutils.AsynchTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ThreadScopeEnabledBeanFactoryPostProcessorTest {

    @Test
    public final void whenExperimentsScopeIsThread_CreateNewBeanInEachThread() throws InterruptedException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.register(TestConfig.class);
        ctx.refresh();

        ExperimentService experimentService = ctx.getBean("experimentServiceImpl", ExperimentService.class);
        experimentService.addExperiment(new Experiment(1, "A", true));

        AsynchTester asynchTester = new AsynchTester(() -> checkIfExperimentsExist(ctx));
        asynchTester.start();
        asynchTester.test();

        ExperimentService otherExperimentService = ctx.getBean("experimentServiceImpl", ExperimentService.class);

        assertThat(otherExperimentService.getExperiments().size(), is(1));

    }

    private void checkIfExperimentsExist(AnnotationConfigApplicationContext ctx) {
        ExperimentService otherExperimentService = ctx.getBean("experimentServiceImpl", ExperimentService.class);
        assertThat(otherExperimentService.getExperiments().size(), is(0));
    }
}