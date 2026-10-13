package com.dsa.dsa_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dsa.collections.associative.JTreeMap;
import com.dsa.dsa_backend.model.StepRecorder;
import com.dsa.dsa_backend.model.StepType;

public class TracedJTreeMap {
    private JTreeMap<Integer, String> map;
    private StepRecorder recorder;

    public TracedJTreeMap(StepRecorder recorder) {
        this.map = new JTreeMap<>();
        this.recorder = recorder;
    }

    private List<Object> snapshot() {
        List<Object> preorder = new ArrayList<>(map.getPreorder());
        return preorder;
    }

    private int[] getHighlightedIndices(int element) {
        List<Object> preorderList = snapshot();

        List<Object> path = new ArrayList<>(map.getPath(element));

        int []indices = new int[path.size()];
        for(int i=0; i<indices.length; i++) {
            indices[i] = preorderList.indexOf(path.get(i));
        }

        return indices;
    }

    public boolean insert(int iKey, String sValue) {
        if(map.containsKey(iKey)) {
            map.put(iKey, sValue);

            recorder.addRecord(
                    StepType.SET,
                    snapshot(),
                    getHighlightedIndices(iKey),
                    Map.of(),
                    "Item: {" + iKey + ":" + sValue + "} is UPDATED by the " + sValue + " in the tree."
            );

            return true;
        }

        map.put(iKey, sValue);

        recorder.addRecord(
                StepType.ADD,
                snapshot(),
                getHighlightedIndices(iKey),
                Map.of(),
                "Item: {" + iKey + ":" + sValue + "} INSERTED in the tree."
        );

        return true;
    }

    public boolean delete(int element) {
        if(!map.containsKey(element)) {
            recorder.addRecord(
                    StepType.FAILED,
                    snapshot(),
                    new int[] {},
                    Map.of(),
                    "Key: {" + element + "} NOT PRESENT in the tree."
            );

            return true;
        }

        int[] highlightedIndicesBeforeRemove = getHighlightedIndices(element);

        recorder.addRecord(
                StepType.REMOVE,
                snapshot(),
                highlightedIndicesBeforeRemove,
                Map.of(),
                "Item: {" + element + ":" + map.get(element) + "} DELETED in the tree."
        );

        String val = map.remove(element);

        int []highlightedIndicesAfterRemove = new int[highlightedIndicesBeforeRemove.length -1];
        System.arraycopy(
                highlightedIndicesBeforeRemove,
                0,
                highlightedIndicesAfterRemove,
                0,
                highlightedIndicesBeforeRemove.length-1
        );

        recorder.addRecord(
                StepType.REMOVE,
                snapshot(),
                highlightedIndicesAfterRemove,
                Map.of(),
                "Item: {" + element + ":" + val + "} DELETED in the tree."
        );

        return true;
    }

    public boolean search(int element) {
        if(!map.containsKey(element)) {
            recorder.addRecord(
                    StepType.FAILED,
                    snapshot(),
                    new int[] {},
                    Map.of(),
                    "Key: {" + element + "} NOT PRESENT in the tree."
            );

            return true;
        }

        recorder.addRecord(
                StepType.FOUND,
                snapshot(),
                getHighlightedIndices(element),
                Map.of(),
                "Item: {" + element + ":" + map.get(element) + "} FOUND in the tree."
        );

        return true;
    }
}
