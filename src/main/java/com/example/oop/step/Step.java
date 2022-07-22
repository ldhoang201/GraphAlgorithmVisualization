package com.example.oop.step;

import com.example.oop.model.Edge;
import com.example.oop.model.Vertex;

public abstract class Step {
    String text;

    public String getText() {
        return text;
    }

    public Step(String text) {
        this.text = text;
    }

    public void run() {
    }
}
