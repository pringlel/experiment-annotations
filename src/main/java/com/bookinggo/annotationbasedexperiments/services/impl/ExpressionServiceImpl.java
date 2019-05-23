package com.bookinggo.annotationbasedexperiments.services.impl;

import com.bookinggo.annotationbasedexperiments.services.ExpressionService;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Setter
@Service
public class ExpressionServiceImpl implements ExpressionService {

    @Override
    public boolean isLeadTimeLessThanTwentyFourHours() {
        return 23 < 24;
    }

}
