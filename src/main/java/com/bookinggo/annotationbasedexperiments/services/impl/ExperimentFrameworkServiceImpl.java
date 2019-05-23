package com.bookinggo.annotationbasedexperiments.services.impl;


import com.bookinggo.annotationbasedexperiments.annotations.ExperimentOf;
import com.bookinggo.annotationbasedexperiments.experiment.Experiment;
import com.bookinggo.annotationbasedexperiments.experiment.ExperimentVariant;
import com.bookinggo.annotationbasedexperiments.services.ExperimentFrameworkService;
import com.bookinggo.annotationbasedexperiments.services.ExperimentService;
import com.bookinggo.annotationbasedexperiments.services.ExperimentToggleService;
import com.bookinggo.annotationbasedexperiments.services.ExpressionService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;


@Slf4j
@Aspect
@Component
public class ExperimentFrameworkServiceImpl implements ExperimentFrameworkService {
    private final ExperimentService experimentService;
    private final ExpressionService expressionService;
    private final ExperimentToggleService experimentToggleService;
    private ExpressionParser expressionParser;

    @Autowired
    public ExperimentFrameworkServiceImpl(ExperimentService experimentService, ExpressionService expressionService, ExperimentToggleService experimentToggleService) {
        this.experimentService = experimentService;
        this.expressionService = expressionService;
        this.experimentToggleService = experimentToggleService;
    }

    @PostConstruct
    public void init(){
        expressionParser = new SpelExpressionParser();
    }

    @Pointcut("execution(@com.bookinggo.annotationbasedexperiments.annotations.ExperimentOf(variant = com.bookinggo.annotationbasedexperiments.experiment.ExperimentVariant.A) * * (..))")
    private void onlyMatchAVariantMethodsWithExperimentOf() { }

    @Around("@annotation(experimentOf) && onlyMatchAVariantMethodsWithExperimentOf()")
    @Override
    public Object redirectToExperiment(ProceedingJoinPoint proceedingJoinPoint, ExperimentOf experimentOf) throws Throwable {
        Class klass = proceedingJoinPoint.getSourceLocation().getWithinType();
        Experiment experiment = getExperimentFromAnnotation(experimentOf);

        if (experiment != null &&
                isExperimentEnabled(experimentOf) &&
                isExpressionValid(experimentOf.expression())) {
            return runExperimentalMethod(proceedingJoinPoint, klass, experiment);
        }
        return proceedingJoinPoint.proceed();
    }

    private boolean isExperimentEnabled(ExperimentOf experiment) {
        String applicationPropertyKey = experiment.property();
        return experimentToggleService.getExperimentToggle(applicationPropertyKey);
    }

    private Object runExperimentalMethod(ProceedingJoinPoint proceedingJoinPoint, Class klass, Experiment experiment) throws Throwable {
        if (isAVariant(experiment)) {
            experimentService.impact(experiment.getId());
            return proceedingJoinPoint.proceed();
        } else {
            Method method = findMatchingExperimentalMethod(experiment, klass.getDeclaredMethods());
            return runMatchingExperimentalMethod(proceedingJoinPoint, experiment, method);
        }
    }

    private Experiment getExperimentFromAnnotation(ExperimentOf experimentOf) {
        return experimentService.getExperiments()
                .stream()
                .filter(experiment -> experiment.getId() == experimentOf.id())
                .findFirst()
                .orElse(null);
    }

    private boolean isAVariant(Experiment experiment) {
        return experiment.getVariant().equals(ExperimentVariant.A.letter());
    }

    private Method findMatchingExperimentalMethod(Experiment experiment, Method[] experimentMethods) {
        return Arrays.stream(experimentMethods)
                .filter(method -> method.isAnnotationPresent(ExperimentOf.class) &&
                        annotationMatchesExperimentIdAndVariant(experiment, method))
                .findFirst()
                .orElse(null);
    }

    private boolean annotationMatchesExperimentIdAndVariant(Experiment experiment, Method method) {
        ExperimentOf annotation = method.getAnnotation(ExperimentOf.class);
        return annotation.id() == experiment.getId() && annotation.variant().letter().equals(experiment.getVariant());
    }

    private Object runMatchingExperimentalMethod(ProceedingJoinPoint proceedingJoinPoint, Experiment experiment, Method method) throws Throwable {
        if (method != null) {
            try {
                return runMethod(proceedingJoinPoint, experiment, method);
            } catch (Exception e) {
                log.error("There was an exception when trying to execute experiment id: {}, variant {}, method: {}", experiment.getId(), experiment.getVariant(), method, e);
                return proceedingJoinPoint.proceed();
            }
        }
        return proceedingJoinPoint.proceed();
    }

    private Object runMethod(ProceedingJoinPoint proceedingJoinPoint, Experiment experiment, Method method) throws Exception {
        Object result = runMethodAgainstProxiedInterface(proceedingJoinPoint.getThis(), method, proceedingJoinPoint.getArgs());
        experimentService.impact(experiment.getId());
        return result;
    }

    private Object runMethodAgainstProxiedInterface(Object proxiedInstance, Method method, Object[] args) throws Exception {
        Method proxiedMethod = proxiedInstance.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        return proxiedMethod.invoke(proxiedInstance, args);
    }

    private boolean isExpressionValid(String expressionString){
        if(expressionString.isEmpty()) {
            return true;
        }
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(expressionService);
        Expression expression =  expressionParser.parseExpression(expressionString);
        Object result = expression.getValue(evaluationContext);
        return result != null && (boolean) result;
    }
}


