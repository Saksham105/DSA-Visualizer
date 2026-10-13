package com.dsa.dsa_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dsa.collections.linear.JHashSet;
import com.dsa.dsa_backend.model.StepRecorder;
import com.dsa.dsa_backend.model.StepType;

public class TracedJHashSet {
    
    private JHashSet<Integer> hashSet;
    private StepRecorder recorder;

    public TracedJHashSet(StepRecorder recorder) {
        this.hashSet = new JHashSet<>();
        this.recorder = recorder;
    }

    private List<Object> snapshot() {
        Object[] hashTable = hashSet.getElements();

        List<Object> updatedList = new ArrayList<>();
        for(Object o : hashTable) {
            if(o == null)
                updatedList.add("null");
            else if(!(o instanceof Integer))
                updatedList.add("DEL");
            else
                updatedList.add(o);
        }

        return updatedList;
    }

    public boolean insert(int element) {
        if(!hashSet.add(element)) {
            int index = hashSet.indexOf(element);

            recorder.addRecord(
                    StepType.EXISTING,
                    snapshot(),
                    new int[] {index},
                    Map.of(),
                    "element " + element + " is ALREADY EXISTING in the JHashSet at the index: " + index
            );

            return true;
        }
        int index = hashSet.indexOf(element);

        recorder.addRecord(
                StepType.ADD,
                snapshot(),
                new int[] {index},
                Map.of(),
                "element " + element + " is INSERTED in the JHashSet at the index: " + index
        );

        return true;
    }

    public boolean delete(int element) {
        int index = hashSet.indexOf(element);

        if(!hashSet.remove(element)) {
            recorder.addRecord(
                    StepType.FAILED,
                    snapshot(),
                    new int[] {},
                    Map.of(),
                    "element " + element + " is NOT PRESENT the JHashSet"
            );

            return true;
        }

        recorder.addRecord(
                StepType.REMOVE,
                snapshot(),
                new int[] {index},
                Map.of(),
                "element " + element + " is DELETED from the JHashSet from the index: " + index
        );

        return true;
    }

    public boolean search(int element) {
        int index = hashSet.indexOf(element);

        if(!hashSet.contains(element)) {
            recorder.addRecord(
                    StepType.FAILED,
                    snapshot(),
                    new int[] {},
                    Map.of(),
                    "element " + element + " is NOT PRESENT the JHashSet"
            );

            return true;
        }

        recorder.addRecord(
                StepType.GET,
                snapshot(),
                new int[] {index},
                Map.of(),
                "element " + element + " is FOUND in the JHashSet at the index: " + index
        );

        return true;
    }
}
