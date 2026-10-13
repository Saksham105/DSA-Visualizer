package com.dsa.dsa_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dsa.collections.exceptions.ListEmptyException;
import com.dsa.collections.linear.JLinkedQueue;
import com.dsa.dsa_backend.model.StepRecorder;
import com.dsa.dsa_backend.model.StepType;

public class TracedJQueue {

    private final JLinkedQueue<Integer> queue;
    private final StepRecorder recorder;

    private List<Object> snapshot() {
        List<Object> updated_list = new ArrayList<>();

        for(int i=0; i<queue.size(); i++)
            updated_list.add(queue.get(i));

        return updated_list;
    }

    public TracedJQueue(StepRecorder recorder) {
        this.queue = new JLinkedQueue<>();
        this.recorder = recorder;
    }

    //Enqueue, dequeue, peek, 

    public boolean enqueue(int element) {
        queue.enqueue(element);

        recorder.addRecord(
            StepType.ADD,
            snapshot(),
            new int[] {queue.size()-1},
            Map.of(),
            "Enqueue element in queue (FIRST IN)"
        );

        return true;
    }

    public boolean dequeue() {
        try {
            queue.dequeue();

            recorder.addRecord(
                StepType.REMOVE,
                snapshot(),
                new int[] {0},
                Map.of(),
                "Dequeue element from queue (FIRST OUT)"
            );
        } catch(ListEmptyException e) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "Dequeue failed, queue is empty"
            );
        }

        return true;
    }

    public boolean peek() {
        Integer element = null;

        try {
            element = queue.peek();

            recorder.addRecord(
                StepType.GET,
                snapshot(),
                new int[] {0},
                Map.of(),
                "Peek element from queue (FIRST): " + element
            );
        } catch(ListEmptyException e) {
            recorder.addRecord(
                StepType.FAILED,
                snapshot(),
                new int[] {},
                Map.of(),
                "Peek failed, queue is empty"
            );
        }

        return true;
    }


    
}
