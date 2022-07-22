package com.example.oop.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import com.example.oop.model.Edge;
import com.example.oop.model.Vertex;
import com.example.oop.step.EdgeDetailStep;
import com.example.oop.step.PseudoStep;
import com.example.oop.step.VertexDetailStep;
import com.example.oop.step.soloStep;

public class DP extends Algorithm {
    private ArrayList<Edge> edges = new ArrayList<>();
    static int[] color = new int[0];
    static boolean[] visited = new boolean[0];
    static boolean[] visited2 = new boolean[200];
    static Stack<Vertex> topoStack = new Stack<>();
    static ArrayList<Vertex> adjList = new ArrayList<>();
    static Vertex tmp = new Vertex();
    ArrayList<String> pseudoCodes = new ArrayList<>();
    static ArrayList<Integer> getIdList = new ArrayList<>();
    boolean isSource = true;

    public ArrayList<String> getPseudoCodes() {
        return pseudoCodes;
    }


    @Override
    public void explore(Vertex v) {
        if (isSource) {
            PseudoStep step1 = new PseudoStep(0);
            pseudoSteps.add(step1);
            step1.addStep(new VertexDetailStep(v.getID() + " is the source vertex", false, v));

        }
        isSource = false;

        while (!topoStack.isEmpty()) {
            convertStack();
            Collections.reverse(adjList);
            PseudoStep step2 = new PseudoStep(1);
            pseudoSteps.add(step2);
            step2.addStep(new soloStep("current topological sort:[" + getIdList + "]"));

            v = topoStack.lastElement();
            traverser(v);
            topoStack.remove(topoStack.lastElement());
            getIdList = new ArrayList<>();
        }
        super.getData().clearHighlight();
        PseudoStep s5 = new PseudoStep(2);
        pseudoSteps.add(s5);
        s5.addStep(new VertexDetailStep("This is the SSSP spanning tree from source vertex " + v.getID(), true, v));
    }


    public boolean checkCyclic(Vertex u) {

        color[u.getID()] = 1;
        for (Vertex v : super.getData().getAdjList(u)) {
            if (color[v.getID()] == 0) {
                if (checkCyclic(v))
                    return true;
            } else if (color[v.getID()] == 1) return true;//exist (v,u)
        }

        color[u.getID()] = 2;
        return false;

    }

    public void traverser(Vertex vertex) {
        boolean isLight;
        if (visited2[vertex.getID()] != true) {
            visited2[vertex.getID()] = true;
            PseudoStep step3 = new PseudoStep(2);
            pseudoSteps.add(step3);
            step3.addStep(new VertexDetailStep("visit(" + vertex.getID() + ")", true, vertex));

            Iterator<Vertex> ite = super.getData().getAdjList(vertex).iterator();

            while (ite.hasNext()) {
                Vertex adj = ite.next();

                PseudoStep step4 = new PseudoStep(3);
                pseudoSteps.add(step4);
                step4.addStep(new EdgeDetailStep("relax(" + vertex.getID() + "," + adj.getID()
                        + "," + super.getData().getLabelEdge(vertex, adj) + ")", true, super.getData().getEdge(vertex, adj)));

                if (!visited2[adj.getID()]) {
                    traverser(adj);
                } else {
                    PseudoStep step5 = new PseudoStep(3);
                    pseudoSteps.add(step5);
                    step5.addStep(new EdgeDetailStep("visited u = " + adj.getID(), false, super.getData().getEdge(vertex, adj)));
                }
            }
        } else {
            PseudoStep step3 = new PseudoStep(2);
            pseudoSteps.add(step3);
            step3.addStep(new VertexDetailStep("backtrack(" + vertex.getID() + ")", true, vertex));
        }
    }

    public boolean isDAG() {
        int colorSize = super.getData().getVertices().size();
        color = new int[colorSize];

        for (int i = 0; i < colorSize; i++) {
            color[i] = 0;
        }

        for (Vertex v : super.getData().getVertices()) {
            if (checkCyclic(v))
                return false;

        }
        return true;
    }

    public void topoTraverser(Vertex v) {
        visited[v.getID()] = true;
        for (Vertex u : super.getData().getAdjList(v)) {
            if (!visited[u.getID()])
                topoTraverser(u);
        }

        topoStack.push(v);
    }

    public void topoSort() {

        int visitSize = super.getData().getVertices().size();
        visited = new boolean[visitSize];

        for (int i = 0; i < visitSize; i++) {
            visited[i] = false;
        }

        for (Vertex v : super.getData().getVertices()) {
            if (!visited[v.getID()])
                topoTraverser(v);
        }
    }

    public void falseAll() {
        for (int i = 0; i < 200; i++) {
            visited2[i] = false;
        }
    }

    public static void convertStack() {
        int i = 0;
        for (Vertex vertex : topoStack) {
            getIdList.add(vertex.getID());
            i++;
        }
    }

    public DP() {
        pseudoCodes.add("initSSSP\n");
        pseudoCodes.add("order = Topological Sort the input DAG\n");
        pseudoCodes.add("""
                while !order.empty()
                u = order.front()
                """);
        pseudoCodes.add("relax all outgoing edges of vertex u");
    }

    public Stack<Vertex> getTopoStack() {
        return topoStack;
    }


}
