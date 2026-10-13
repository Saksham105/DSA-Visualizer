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
import com.dsa.dsa_backend.service.TracedJHashSet;

@RestController
@RequestMapping("/api/hashset")
public class JHashSetController {

    @PostMapping("/execute") // POST localhost:8080/api/hashset/execute
    public ExecutionResponse execute(@RequestBody ExecutionRequest request) {
        StepRecorder recorder = new StepRecorder();
        TracedJHashSet hashSet = new TracedJHashSet(recorder);

        for(Operation op : request.getOperations()) {
            try{
                switch (op.getName()) {
                    case "insert" -> {

                        if(!hashSet.insert((int) op.getArguments().get(0)))
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }
                    }


                    case "delete" -> {

                        if(!hashSet.delete((int) op.getArguments().get(0)))
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }

                    }

                    case "search" -> {

                        if(!hashSet.search((int) op.getArguments().get(0)))
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
