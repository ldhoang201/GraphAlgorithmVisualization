package com.example.oop.step;

public class soloStep extends Step{
	
	boolean isHighlighted;
	
	public soloStep(String text) {
		super(text);
	}

	public soloStep(String text, boolean isHighlighted) {
		super(text);
		this.isHighlighted = isHighlighted;
	}

}
