package com.example.oop.model;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Vertex extends StackPane {
	 private int id;
	private final Label label = new Label();

	    public int getID() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
			label.setText(id + "");
	    }
		public Vertex(){
			this.setPrefSize(2, 14);
			Circle circle = new Circle();
			circle.setRadius(22);
			circle.setFill(Color.AQUA);
			circle.setStroke(Color.BLACK);
			circle.setStrokeType(StrokeType.INSIDE);
			this.getChildren().addAll(circle, label);
		}

		public Vertex(int id) {
			this.id = id;
		}
}
