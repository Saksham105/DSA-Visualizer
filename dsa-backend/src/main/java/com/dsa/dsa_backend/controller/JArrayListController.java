package com.dsa.dsa_backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsa.dsa_backend.dto.ExecutionRequest;
import com.dsa.dsa_backend.dto.ExecutionResponse;
import com.dsa.dsa_backend.dto.Operation;
import com.dsa.dsa_backend.model.ExecutionTrace;
import com.dsa.dsa_backend.model.StepRecorder;
import com.dsa.dsa_backend.service.TracedJArrayList;

@RestController
@RequestMapping("/api/arraylist")
public class JArrayListController {

    @PostMapping("/execute") // POST localhost:8080/api/arraylist/execute
    public ExecutionResponse execute(@RequestBody ExecutionRequest request) {
        StepRecorder recorder = new StepRecorder();
        TracedJArrayList arrayList = new TracedJArrayList(recorder);

        for(Operation op : request.getOperations()) {
            try{
                switch (op.getName()) {
                    case "add" -> {
                        
                        if(!arrayList.add((int) op.getArguments().get(0)))
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }
                    }

                    case "addFirst" -> {

                        if(!arrayList.addFirst((int) op.getArguments().get(0)))
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }

                    }

                    case "remove" -> {

                        if(!arrayList.remove((int) op.getArguments().get(0)))
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }

                    }

                    case "set" -> {

                        if(!arrayList.set((int) op.getArguments().get(0), (int) op.getArguments().get(1)))
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }

                    }

                    case "get" -> {

                        if(!arrayList.get((int) op.getArguments().get(0)))
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }

                    }

                    case "linearSearch" -> {

                        if(!arrayList.linear_search((int) op.getArguments().get(0)))
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }

                    }

                    case "binarySearch" -> {

                        if(!arrayList.binary_search((int) op.getArguments().get(0)))
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }

                    }

                    case "bubbleSort" -> {

                        if(!arrayList.bubble_sort())
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }

                    }

                    case "insertionSort" -> {

                        if(!arrayList.insertion_sort())
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }

                    }

                    default -> throw new IllegalArgumentException("invalid operation.!");

                }
            } catch(IllegalCallerException e )
            {
                System.out.println("ERROR: " + e);
            }
        }
        

        ExecutionTrace holder = recorder.buildTrace();
        return new ExecutionResponse(holder);
    }
}