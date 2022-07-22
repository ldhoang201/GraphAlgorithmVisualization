package com.example.oop.step;

import com.example.oop.model.Graph;
import com.example.oop.model.Vertex;

public class VertexDetailStep extends soloStep {
    Vertex vertex = new Vertex();

    public VertexDetailStep(String text) {
        super(text);
    }

    public VertexDetailStep(String text, boolean isHighlighted, Vertex vertex) {
        super(text, isHighlighted);
        this.vertex = vertex;
    }

    @Override
    public void run() {
        Graph.highlight(vertex, isHighlighted);
    }

}
