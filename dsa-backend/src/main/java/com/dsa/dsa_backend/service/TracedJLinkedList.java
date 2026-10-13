package com.dsa.dsa_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dsa.collections.exceptions.ListEmptyException;
import com.dsa.collections.linear.JLinkedList;
import com.dsa.dsa_backend.model.StepRecorder;
import com.dsa.dsa_backend.model.StepType;


public class TracedJLinkedList {

    private final JLinkedList<Integer> list;
    private final StepRecorder recorder;

    private List<Object> snapshot() {
        List<Object> updated_list = new ArrayList<>();

        for(int i=0; i<list.size(); i++)
            updated_list.add(list.get(i));

        return updated_list;
    }

    public TracedJLinkedList(StepRecorder recorder) {

        this.list = new JLinkedList<>();
        this.recorder = recorder;

    }

    //CRUD operations 

    public boolean add(int element) {

        list.add(element); 
        recorder.addRecord(
            StepType.ADD,
            snapshot(),
            new int[] {list.size()-1},
            Map.of(),
            "adding element: " + element + " at the end of the list"
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
            "adding element: " + element + " at the beginning of the list"
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
                "removing node on the index of the list : " + index
            );
        } catch(IllegalArgumentException | ListEmptyException e) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: Cannot remove element from an empty List.!"
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
                "updating element on the node : " + index
            );
        } catch(IllegalArgumentException | ListEmptyException e) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: Cannot set (update) element in an empty List.!"
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
                "value on the node : " + index + " is: " + value
            );
        } catch(ListEmptyException | IndexOutOfBoundsException e) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: Cannot fetch element from an empty List.!"
            );
        }
        return true;
    }

    //Linear search only..as binary search is not efficient on linked list

    public boolean linear_search(int target) {
        for(int i = 0 ; i<list.size() ; i++)
        {
            recorder.addRecord(
                StepType.COMPARE,
                snapshot(),
                new int[] {i},
                Map.of(),
                "comparing element on the node : " + i + " with target: " + target
            );
            if(list.get(i) == target)
            {
                recorder.addRecord(
                    StepType.FOUND,
                    snapshot(),
                    new int[] {i},
                    Map.of(),
                    "target element: " + target + " found on the node : " + i
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
    

    //SORTING OPERATIONS

    public boolean insertion_sort() {
        if(list.isEmpty()) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: Cannot perform sorting on an empty list.!"
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
                    "comparing backup with left list"
            );

            while(j >= 0 && list.get(j) > backup) {
                list.set(j+1, list.get(j));
                recorder.addRecord(
                        StepType.SHIFT,
                        snapshot(),
                        new int[] {j, j+1},
                        Map.of("backup", backup),
                        "shifting each element on node : " + j + " to " + j+1
                );

                j--;
            }

            list.set(j+1, backup);
            recorder.addRecord(
                    StepType.SET,
                    snapshot(),
                    new int[] {j+1},
                    Map.of("backup", backup),
                    "setting " + backup + " on node : " + j+1
            );
        }

        recorder.addRecord(
                StepType.SORT,
                snapshot(),
                new int[] {},
                Map.of(),
                "list is now sorted"
        );
        return true;
    }

    //REVERSE OPERATION

    public boolean reverse()
    {
        if(list.isEmpty()) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: Cannot perform reverse on an empty list.!"
            );
            return true;
        }

        int start = 0;
        int end = list.size()-1;

        while(start < end)
        {
            int temp = list.get(start);
            list.set(start, list.get(end));
            list.set(end , temp);

            recorder.addRecord(
                StepType.SWAP,
                snapshot(),
                new int[] {start, end},
                Map.of(),
                "Swapping of node data done"
            );
            start++;
            end--;

        }

        recorder.addRecord(
            StepType.REVERSED,
            snapshot(),
            new int[] {},
            Map.of(),
            "List is reversed"
        );
        return true;

    }

    //MIDDLE NODE for linked list

    public boolean middleNode()
    {
        if(list.isEmpty()) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "ERROR: No middle node as list is empty!"
            );
            return true;
        }

        int slow = 0;
        int fast = 0;

        while(fast <list.size() && fast+1 <list.size() ) 
        {
            slow++;
            fast+=2;

            recorder.addRecord(
                StepType.RUN,
                snapshot(),
                new int[] {slow, fast},
                Map.of(),
                "Finding middle node using slow and fast pointer technique"
            );
        }

        recorder.addRecord(
            StepType.MIDDLE,
            snapshot(),
            new int[] {slow},
            Map.of(),
            "Middle node found at index: " + slow
        );

        return true;
    }

  
    
}
