package com.dsa.dsa_backend.model;

import java.util.Collections;
import java.util.List;

public class ExecutionTrace {
    private final List<Step> steps;

    public ExecutionTrace(List<Step> steps) {
        this.steps = Collections.unmodifiableList(steps);
    }

    public List<Step> getSteps() {return steps;}

    public int totalSteps() {return steps.size();}
}