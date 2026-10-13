package com.dsa.dsa_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dsa.collections.linear.JArrayList;
import com.dsa.dsa_backend.model.StepRecorder;
import com.dsa.dsa_backend.model.StepType;

public class TracedJArrayList {
    private final JArrayList<Integer> list;
    private final StepRecorder recorder;

    private List<Object> snapshot() {
        List<Object> updated_list = new ArrayList<>();

        for(int i=0; i<list.size(); i++)
            updated_list.add(list.get(i));

        return updated_list;
    }

    public TracedJArrayList(StepRecorder recorder) {
        this.list = new JArrayList<>();
        this.recorder = recorder;
    }

    /**
     * implementation of methods that will be recorded for visualizing.
     */
    /* CRUD operations */
    public boolean add(int element) {
        list.add(element); // actually adding element in JArrayList

        recorder.addRecord(
            StepType.ADD,
            snapshot(),
            new int[] {list.size()-1},
            Map.of(),
            "adding element at last index"
        );

        return true;
    }

    public boolean addFirst(int element) {
        list.addFirst(element);

        recorder.addRecord(
            StepType.SHIFT,
            snapshot(),
            new int[] {0},
            Map.of(),
            "shifting all elements to one index right"
        );
        return true;
    }

    public boolean remove(int index) {
        try {
            list.remove(index);

            recorder.addRecord(
                StepType.REMOVE,
                snapshot(),
                new int[] {index},
                Map.of(),
                "removing element on the index: " + index
            );
        } catch(IllegalArgumentException e) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: Cannot remove element from an empty array.!"
            );
        }
        return true;
    }

    public boolean set(int index, int element) {
        try {
            list.set(index, element);

            recorder.addRecord(
                StepType.SET,
                snapshot(),
                new int[] {index},
                Map.of(),
                "updating element on the index: " + index
            );
        } catch(IllegalArgumentException e) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: Cannot set (update) element in an empty array.!"
            );
        }
        return true;
    }

    public boolean get(int index) {
        try {
            int value = list.get(index);

            recorder.addRecord(
                StepType.GET,
                snapshot(),
                new int[] {index},
                Map.of(),
                "value on the index: " + index + " is: " + value
            );
        } catch(IllegalArgumentException e) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: Cannot fetch element from an empty array.!"
            );
        }
        return true;
    }

    /* searching operations */
    public boolean linear_search(int target) {
        for(int i = 0 ; i<list.size() ; i++)
        {
            recorder.addRecord(
                StepType.COMPARE,
                snapshot(),
                new int[] {i},
                Map.of(),
                "comparing element on the index: " + i + " with target: " + target
            );
            if(list.get(i) == target)
            {
                recorder.addRecord(
                    StepType.FOUND,
                    snapshot(),
                    new int[] {i},
                    Map.of(),
                    "target element: " + target + " found on the index: " + i
                );
                return true;
            }
        }

        recorder.addRecord(
            StepType.NOT_FOUND,
            snapshot(),
            new int[] {},
            Map.of(),
            "target element: " + target + " not found in the list"
        );
        
        return true;
    }

    public boolean binary_search(int target) {
        int start=0;
        int end=list.size()-1;
        int mid;

        while(start <= end) {
            mid = start + ((end-start)/2);

            recorder.addRecord(
                StepType.COMPARE,
                snapshot(),
                new int[] {start, mid, end},
                Map.of(),
                "comparing element on the index: " + mid + " with target: " + target
            );

            if(list.get(mid) == target) {
                recorder.addRecord(
                    StepType.FOUND,
                    snapshot(),
                    new int[] {mid},
                    Map.of(),
                    "target element: " + target + " found at index: " + mid
                );
                return true;

            } else if(target < list.get(mid)) {
                end = mid-1;
            } else {
                start = mid +1;
            }
        }

        recorder.addRecord(
            StepType.NOT_FOUND,
            snapshot(),
            new int[] {},
            Map.of(),
            "target element: " + target + " not found in the list"
        );
        return true;
    }

    /* sorting operations */
    public boolean bubble_sort() {
        if(list.isEmpty()) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: Cannot perform sorting on an empty array.!"
            );

            return true;
        }

        boolean swap;
        for(int i = 0 ; i<list.size()-1 ; i++)
        {
            swap = false;
            for(int j = 0 ; j<list.size()-i-1 ; j++)
            {
                recorder.addRecord(
                    StepType.COMPARE,
                    snapshot(),
                    new int[] {j, j+1},
                    Map.of(),
                    "comparing element on the index: " + j + " with " + (j+1)
                );

                if(list.get(j) > list.get(j+1))
                {
                    swap = true;
                    int temp = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, temp);
                    recorder.addRecord(
                        StepType.SWAP,
                        snapshot(),
                        new int[] {j, j+1},
                        Map.of(),
                        "swapping element on the index: " + j + " with " + (j+1)
                    );
                }
            }

            if(!swap)
                break;
        }

        recorder.addRecord(
            StepType.SORT,
            snapshot(),
            new int[] {},
            Map.of(),
            "array is now sorted"
        );
        return true;
    }

    public boolean insertion_sort() {
        if(list.isEmpty()) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: Cannot perform sorting on an empty array.!"
            );

            return true;
        }

        for(int i=1; i<list.size(); i++) {
            int j = i-1;
            int backup = list.get(i);

            recorder.addRecord(
                    StepType.COMPARE,
                    snapshot(),
                    new int[] {i, j},
                    Map.of("backup", backup),
                    "comparing backup with left array"
            );

            while(j >= 0 && list.get(j) > backup) {
                list.set(j+1, list.get(j));
                recorder.addRecord(
                        StepType.SHIFT,
                        snapshot(),
                        new int[] {j, j+1},
                        Map.of("backup", backup),
                        "shifting each element on index: " + j + " to " + j+1
                );

                j--;
            }

            list.set(j+1, backup);
            recorder.addRecord(
                    StepType.SET,
                    snapshot(),
                    new int[] {j+1},
                    Map.of("backup", backup),
                    "setting " + backup + " on index: " + j+1
            );
        }

        recorder.addRecord(
                StepType.SORT,
                snapshot(),
                new int[] {},
                Map.of(),
                "array is now sorted"
        );
        return true;
    }
}