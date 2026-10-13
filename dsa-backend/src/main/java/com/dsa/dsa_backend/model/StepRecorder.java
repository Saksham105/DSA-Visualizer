package com.dsa.dsa_backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * It will store steps of execution for one operation
 * after each operation (user request) steps list will be exported
 * and after that it will be clear for another operarations.
 */
public class StepRecorder {
    private final List<Step> steps;

    public StepRecorder() {
        steps = new ArrayList<>();
    }

    public boolean addRecord(StepType stepType, List<Object> snapshot, int highlightedIndices[], Map<String, Integer> helpers, String description) {
        return steps.add(new Step(stepType, snapshot, highlightedIndices, helpers, description));
    }

    public ExecutionTrace buildTrace() {
        /**
         * Collections.unmodifiableList() changes the property of List to immutable.
         * to keep this.steps unchanged, we passes new ArrayList of same elements.
         */ 
        return new ExecutionTrace(new ArrayList<Step>(steps));
    }

    public void clearRecord() {
        steps.clear();
    }
}