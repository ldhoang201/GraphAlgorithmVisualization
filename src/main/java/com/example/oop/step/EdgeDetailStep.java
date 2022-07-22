package com.example.oop.step;

import com.example.oop.model.Edge;
import com.example.oop.model.Graph;

public class EdgeDetailStep extends soloStep {
    Edge edge;

    public EdgeDetailStep(String text, boolean isHighlighted) {
        super(text, isHighlighted);
    }

    public EdgeDetailStep(String text, boolean isHighlighted, Edge edge) {
        super(text, isHighlighted);
        this.edge = edge;
    }

    @Override
    public void run() {
        Graph.highlight(edge, isHighlighted);
    }
}
