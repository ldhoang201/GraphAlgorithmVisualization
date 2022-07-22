package com.example.oop.algorithm;

import java.util.ArrayList;


import com.example.oop.model.Graph;
import com.example.oop.model.Vertex;
import com.example.oop.step.PseudoStep;

public abstract class Algorithm {
    private Graph data = Graph.createGraph();
    ArrayList<PseudoStep> pseudoSteps = new ArrayList<>();

    public abstract void explore(Vertex vertex);

    public Graph getData() {
        return data;
    }

    public void setData(Graph data) {
        this.data = data;
    }
    
    public ArrayList<PseudoStep> getPseudoSteps() {
        return pseudoSteps;
    }
    
    public void setPseudoSteps(ArrayList<PseudoStep> pseudoSteps) {
        this.pseudoSteps = pseudoSteps;
    }
}