package com.bookinggo.annotationbasedexperiments.services.impl;


import com.bookinggo.annotationbasedexperiments.annotations.ExperimentOf;
import com.bookinggo.annotationbasedexperiments.experiment.AnnotationExperiment;
import com.bookinggo.annotationbasedexperiments.experiment.ExperimentVariant;
import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExperimentService;
import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExperimentFrameworkService;
import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExperimentToggleService;
import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExpressionService;
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
public class AnnotationBasedExperimentFrameworkServiceImpl implements AnnotationBasedExperimentFrameworkService {
    private final AnnotationBasedExperimentService annotationBasedExperimentService;
    private final AnnotationBasedExpressionService annotationBasedExpressionService;
    private final AnnotationBasedExperimentToggleService annotationBasedExperimentToggleService;
    private ExpressionParser expressionParser;

    @Autowired
    public AnnotationBasedExperimentFrameworkServiceImpl(AnnotationBasedExperimentService annotationBasedExperimentService, AnnotationBasedExpressionService annotationBasedExpressionService, AnnotationBasedExperimentToggleService annotationBasedExperimentToggleService) {
        this.annotationBasedExperimentService = annotationBasedExperimentService;
        this.annotationBasedExpressionService = annotationBasedExpressionService;
        this.annotationBasedExperimentToggleService = annotationBasedExperimentToggleService;
    }

    @PostConstruct
    public void init() {
        expressionParser = new SpelExpressionParser();
    }

    @Pointcut("execution(@com.bookinggo.annotationbasedexperiments.annotations.ExperimentOf(variant = com.bookinggo.annotationbasedexperiments.experiment.ExperimentVariant.A) * * (..))")
    private void onlyMatchAVariantMethodsWithExperimentOf() {
    }

    @Around("@annotation(experimentOf) && onlyMatchAVariantMethodsWithExperimentOf()")
    @Override
    public Object redirectToExperiment(ProceedingJoinPoint proceedingJoinPoint, ExperimentOf experimentOf) throws Throwable {
        Class klass = proceedingJoinPoint.getSourceLocation().getWithinType();
        AnnotationExperiment annotationExperiment = getExperimentFromAnnotation(experimentOf);

        if (annotationExperiment != null &&
                isExperimentEnabled(experimentOf) &&
                isExpressionValid(experimentOf.expression())) {
            return runExperimentalMethod(proceedingJoinPoint, klass, annotationExperiment);
        }
        return proceedingJoinPoint.proceed();
    }

    private boolean isExperimentEnabled(ExperimentOf experiment) {
        String applicationPropertyKey = experiment.property();
        return annotationBasedExperimentToggleService.getExperimentToggle(applicationPropertyKey);
    }

    private Object runExperimentalMethod(ProceedingJoinPoint proceedingJoinPoint, Class klass, AnnotationExperiment annotationExperiment) throws Throwable {
        if (isAVariant(annotationExperiment)) {
            annotationBasedExperimentService.impact(annotationExperiment.getId());
            return proceedingJoinPoint.proceed();
        } else {
            Method method = findMatchingExperimentalMethod(annotationExperiment, klass.getDeclaredMethods());
            return runMatchingExperimentalMethod(proceedingJoinPoint, annotationExperiment, method);
        }
    }

    private AnnotationExperiment getExperimentFromAnnotation(ExperimentOf experimentOf) {
        return annotationBasedExperimentService.getAnnotationExperiments()
                .stream()
                .filter(experiment -> experiment.getId() == experimentOf.id())
                .findFirst()
                .orElse(null);
    }

    private boolean isAVariant(AnnotationExperiment annotationExperiment) {
        return annotationExperiment.getVariant().equals(ExperimentVariant.A.letter());
    }

    private Method findMatchingExperimentalMethod(AnnotationExperiment annotationExperiment, Method[] experimentMethods) {
        return Arrays.stream(experimentMethods)
                .filter(method -> method.isAnnotationPresent(ExperimentOf.class) &&
                        annotationMatchesExperimentIdAndVariant(annotationExperiment, method))
                .findFirst()
                .orElse(null);
    }

    private boolean annotationMatchesExperimentIdAndVariant(AnnotationExperiment annotationExperiment, Method method) {
        ExperimentOf annotation = method.getAnnotation(ExperimentOf.class);
        return annotation.id() == annotationExperiment.getId() && annotation.variant().letter().equals(annotationExperiment.getVariant());
    }

    private Object runMatchingExperimentalMethod(ProceedingJoinPoint proceedingJoinPoint, AnnotationExperiment annotationExperiment, Method method) throws Throwable {
        if (method != null) {
            try {
                return runMethod(proceedingJoinPoint, annotationExperiment, method);
            } catch (Exception e) {
                log.error("There was an exception when trying to execute annotationExperiment id: {}, variant {}, method: {}", annotationExperiment.getId(), annotationExperiment.getVariant(), method, e);
                return proceedingJoinPoint.proceed();
            }
        }
        return proceedingJoinPoint.proceed();
    }

    private Object runMethod(ProceedingJoinPoint proceedingJoinPoint, AnnotationExperiment annotationExperiment, Method method) throws Exception {
        Object result = runMethodAgainstProxiedInterface(proceedingJoinPoint.getThis(), method, proceedingJoinPoint.getArgs());
        annotationBasedExperimentService.impact(annotationExperiment.getId());
        return result;
    }

    private Object runMethodAgainstProxiedInterface(Object proxiedInstance, Method method, Object[] args) throws Exception {
        Method proxiedMethod = proxiedInstance.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        return proxiedMethod.invoke(proxiedInstance, args);
    }

    private boolean isExpressionValid(String expressionString) {
        if (expressionString.isEmpty()) {
            return true;
        }
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(annotationBasedExpressionService);
        Expression expression = expressionParser.parseExpression(expressionString);
        Object result = expression.getValue(evaluationContext);
        return result != null && (boolean) result;
    }
}


