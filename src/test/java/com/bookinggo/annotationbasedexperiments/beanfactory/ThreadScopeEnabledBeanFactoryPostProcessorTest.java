package com.bookinggo.annotationbasedexperiments.beanfactory;

import com.bookinggo.annotationbasedexperiments.config.AnnotationBasedExperimentsConfig;
import com.bookinggo.annotationbasedexperiments.config.TestConfig;
import com.bookinggo.annotationbasedexperiments.experiment.AnnotationExperiment;
import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExperimentService;
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
        ctx.register(AnnotationBasedExperimentsConfig.class);
        ctx.register(TestConfig.class);
        ctx.refresh();

        AnnotationBasedExperimentService annotationBasedExperimentService = ctx.getBean("annotationBasedExperimentServiceImpl", AnnotationBasedExperimentService.class);
        annotationBasedExperimentService.addExperiment(new AnnotationExperiment(1, "A", true));

        AsynchTester asynchTester = new AsynchTester(() -> checkIfExperimentsExist(ctx));
        asynchTester.start();
        asynchTester.test();

        AnnotationBasedExperimentService otherAnnotationBasedExperimentService = ctx.getBean("annotationBasedExperimentServiceImpl", AnnotationBasedExperimentService.class);

        assertThat(otherAnnotationBasedExperimentService.getAnnotationExperiments().size(), is(1));

    }

    private void checkIfExperimentsExist(AnnotationConfigApplicationContext ctx) {
        AnnotationBasedExperimentService otherAnnotationBasedExperimentService = ctx.getBean("annotationBasedExperimentServiceImpl", AnnotationBasedExperimentService.class);
        assertThat(otherAnnotationBasedExperimentService.getAnnotationExperiments().size(), is(0));
    }
}