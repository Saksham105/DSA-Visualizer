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
import com.dsa.dsa_backend.service.TracedJStack;


@RestController
@RequestMapping("/api/stack")
public class JStackController {

    @PostMapping("/execute") // POST localhost:8080/api/stack/execute
    public ExecutionResponse execute(@RequestBody ExecutionRequest request) {
        StepRecorder recorder = new StepRecorder();
        TracedJStack stack = new TracedJStack(recorder);

        for(Operation op : request.getOperations()) {
            try{
                switch (op.getName()) {
                    case "push" -> {
                        
                        if(!stack.push((int) op.getArguments().get(0)))
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }
                    }

        
                    case "pop" -> {

                        if(!stack.pop())
                        {
                            throw new IllegalCallerException("steps not recorded.!");
                        }

                    }

                    case "peek" -> {

                        if(!stack.peek())
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