package com.example.oop.step;

import java.util.ArrayList;

public class PseudoStep extends Step {
	
	ArrayList<soloStep> detail = new ArrayList<>();
    public PseudoStep(String text) {
        super(text);
    }

    public PseudoStep(int i) {
        super(i + "");
    }

    public ArrayList<soloStep> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<soloStep> detail) {
        this.detail = detail;
    }

    public void addStep(soloStep detail) {
        getDetail().add(detail);
    }

}
