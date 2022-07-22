package com.example.oop.algorithm;

import java.util.ArrayList;
import java.util.Iterator;

import com.example.oop.model.Vertex;
import com.example.oop.step.EdgeDetailStep;
import com.example.oop.step.PseudoStep;
import com.example.oop.step.VertexDetailStep;

public class DFS extends Algorithm {
    static boolean[] visited = new boolean[200];

    ArrayList<String> pseudoCodes = new ArrayList<>();

    public ArrayList<String> getPseudoCodes() {
        return pseudoCodes;
    }

    private boolean source = true;

    public void falseAll() {
        for (int i = 0; i < 200; i++) {
            visited[i] = false;
        }
    }


    @Override
    public void explore(Vertex vertex) {
        visited[vertex.getID()] = true;
        if (source) {
            source = false;
            PseudoStep s1 = new PseudoStep(0);
            pseudoSteps.add(s1);

            s1.addStep(new VertexDetailStep(vertex.getID() + "is the source vertex", true, super.getData().getVertices().get(vertex.getID())));
        }
        System.out.print(vertex.getID() + "->");

//        PseudoStep s2 = new PseudoStep(1);
//        pseudoSteps.add(s2);
//        s2.addStep(new VertexDetailStep("DFS(" + vertex.getID() + ")", true, super.getData().getVertices().get(vertex.getID())));

        Iterator<Vertex> ite = super.getData().getAdjList(vertex).iterator();

        PseudoStep s3 = new PseudoStep(2);
        pseudoSteps.add(s3);

        while (ite.hasNext()) {
            Vertex adj = ite.next();
            Vertex temp1 = adj;
            System.out.print(adj.getID() + "=" + visited[adj.getID()]);
            if (!visited[adj.getID()]) {
                s3.addStep(new EdgeDetailStep("relax(" + vertex.getID() + "," + temp1.getID()
                        + "," + super.getData().getLabelEdge(vertex, temp1) + ")", true, super.getData().getEdge(vertex, temp1)));
                PseudoStep s2 = new PseudoStep(1);
                pseudoSteps.add(s2);
                s2.addStep(new VertexDetailStep("DFS(" + vertex.getID() + ")", true, super.getData().getVertices().get(temp1.getID())));
                explore(adj);
            }
            else {
                s3.addStep(new EdgeDetailStep("u = " + adj.getID() + " is visited!!", false,super.getData().getEdge(vertex, temp1)));
            }
            if (ite.hasNext()){
                PseudoStep s3s = new PseudoStep(2);
                pseudoSteps.add(s3s);
                s3s.addStep(new VertexDetailStep("Backtrack to DFS(" + vertex.getID() + ")", true, vertex));
            }

        }
    }


    public DFS() {
        pseudoCodes.add("""    			
                show warning if the graph is not a tree
                initSSSP, then DFS(sourceVertex))\n""");
        pseudoCodes.add("DFS(u)\n");
        pseudoCodes.add("""  			
                for each neighbor v of u
                    if !visited[v]
                        relax(u, v, w(u, v)), DFS(v)""");
    }


    public static boolean[] getVisited() {
        return visited;
    }

}

//    public void explore(Vertex vertex) {
//        visited[vertex.getId()] = true;
//        
//        int getIdVertex = vertex.getId();
//        System.out.print(vertex.getId() + "->");
//
//
//        Iterator<Vertex> ite = super.getData().getAdjList(vertex).iterator();
//        
//        while (ite.hasNext()){
//            Vertex adj = ite.next();
//            
//            int getAdjId = adj.getId();
//            if(!visited[adj.getId()])
//                explore(adj);
//        }
//    }
//}

//pseudocode:
//    visited[vertex] = true;
//    System.out.print(vertex + " ");
//
//    Iterator<Integer> ite = adjLists[vertex].listIterator();
//    while (ite.hasNext()) {
//      int adj = ite.next();
//      if (!visited[adj])
//        DFS(adj);
//	}
