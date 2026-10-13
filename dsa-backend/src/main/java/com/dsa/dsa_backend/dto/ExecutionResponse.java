package com.dsa.dsa_backend.dto;

import java.util.List;

import com.dsa.dsa_backend.model.ExecutionTrace;
import com.dsa.dsa_backend.model.Step;

public class ExecutionResponse
{
    private final List<Step> steps;
    private final int totalSteps;

    public ExecutionResponse(ExecutionTrace trace)
    {
        this.steps = trace.getSteps();
        this.totalSteps = trace.totalSteps();
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getTotalSteps() {
        return totalSteps;
    }
}