package com.bookinggo.annotationbasedexperiments.services.impl.experimentframework;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SearchContext {
    private LocalDateTime pickTime;
}
