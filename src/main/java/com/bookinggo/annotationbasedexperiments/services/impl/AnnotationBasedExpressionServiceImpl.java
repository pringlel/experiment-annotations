package com.bookinggo.annotationbasedexperiments.services.impl;

import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExpressionService;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Setter
@Service
public class AnnotationBasedExpressionServiceImpl implements AnnotationBasedExpressionService {

    @Override
    public boolean isLeadTimeLessThanTwentyFourHours() {
        return 23 < 24;
    }

}
