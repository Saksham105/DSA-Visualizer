package com.dsa.dsa_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dsa.collections.exceptions.StackEmptyException;
import com.dsa.collections.linear.JStack;
import com.dsa.dsa_backend.model.StepRecorder;
import com.dsa.dsa_backend.model.StepType;

public class TracedJStack {
    private final JStack<Integer> stack;
    private final StepRecorder recorder;

    private List<Object> snapshot() {
        List<Object> updated_list = new ArrayList<>();

        for(int i=0; i<stack.size(); i++)
            updated_list.add(stack.get(i));

        return updated_list;
    }

    public TracedJStack(StepRecorder recorder) {
        this.stack = new JStack<>();
        this.recorder = recorder;
    }

    public boolean push(int element) {
        stack.push(element);

        recorder.addRecord(
            StepType.ADD,
            snapshot(),
            new int[] {stack.size()-1},
            Map.of(),
            "Push element in stack (LAST IN)"
        );

        return true;
    }

    public boolean pop() {
        try {
            stack.pop();

            recorder.addRecord(
                StepType.REMOVE,
                snapshot(),
                new int[] {stack.top()},
                Map.of(),
                "Pop element from stack (LAST OUT)"
            );
        } catch(StackEmptyException e) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: Stack is empty.!"
            );
        }
        return true;
    }

    public boolean peek() {
        try {
            int top_element = stack.peek();

            recorder.addRecord(
                StepType.GET,
                snapshot(),
                new int[] {stack.top()},
                Map.of(),
                "Element at top: " + top_element
            );
        } catch(StackEmptyException e) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: Stack is empty.!"
            );
        }
        return true;
    }
}
