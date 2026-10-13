package com.dsa.dsa_backend.model;

import java.util.List;
import java.util.Map;

/**
 * It is one state of execution steps:
 * The content in step is not modifiable
 * For next execution step, new step object will be created, that's why all fields are final
 */
public class Step {
    private final StepType stepType;
    private final List<Object> snapshot;
    private final int highlightedIndices[];
    private final Map<String, Integer> helpers;
    private final String description;

    // Constructor
    public Step(
        StepType stepType,
        List<Object> snapshot,
        int highlightedIndices[],
        Map<String, Integer> helpers,
        String description
    ) {
        this.stepType = stepType;
        this.snapshot = snapshot;
        this.highlightedIndices = highlightedIndices;
        this.helpers = helpers;
        this.description = description;
    }

    // Getters
    public StepType getStepType() {return stepType;}
    public List<Object> getSnapshot() {return snapshot;}
    public int[] getHighlightedIndices() {return highlightedIndices;}
    public Map<String, Integer> getHelpers() {return helpers;}
    public String getDescription() {return description;}
}