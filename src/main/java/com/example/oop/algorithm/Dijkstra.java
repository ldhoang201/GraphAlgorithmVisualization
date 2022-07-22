package com.example.oop.algorithm;

import com.example.oop.model.Vertex;
import com.example.oop.step.EdgeDetailStep;
import com.example.oop.step.PseudoStep;
import com.example.oop.step.VertexDetailStep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Dijkstra extends Algorithm {
    static ArrayList<String> steps = new ArrayList<>();
    Queue<Vertex> vertexQueue = new LinkedList<>();
    //    final int cnst = super.getData().getVertices().size();
    private String str = new String();
    private StringBuffer c = new StringBuffer();
    private int min = Integer.MAX_VALUE;
    int w;
    ArrayList<String> pseudoCodes = new ArrayList<>();

    public ArrayList<String> getPseudoCodes() {
        return pseudoCodes;
    }

    Vertex temp = new Vertex();
    ArrayList<Vertex> list = new ArrayList<>();

    @Override


    public void explore(Vertex vertex) {
        int[] dist = new int[super.getData().getVertices().size()];
        int[] previous = new int[super.getData().getVertices().size()];
        boolean[] visited = new boolean[super.getData().getVertices().size()];
        boolean isLight;

//        System.out.println(super.getData().getVertices().size());
        for (int k = 0; k < super.getData().getVertices().size(); k++) {
            dist[k] = Integer.MAX_VALUE;
            previous[k] = -1;
        }
        dist[vertex.getID()] = 0;
        visited[vertex.getID()] = true;
        PseudoStep s1 = new PseudoStep(0);
        pseudoSteps.add(s1);
        String text = new String();
        text = vertex.getId() + " is the source vertex.\n";
        text = text + "p[v] = -1, d[v] = Inf; but d[" + vertex.getID() + "] = 0, PQ add (0," + vertex.getID() + "),...}.";
        s1.addStep(new VertexDetailStep(text, true, super.getData().getVertices().get(vertex.getID())));

        vertexQueue.add(vertex);

        while (!vertexQueue.isEmpty()) {
            min = Integer.MAX_VALUE;
            for (Vertex i : vertexQueue) {
                if (min > dist[i.getID()]) {
                    min = dist[i.getID()];
                    temp = i;
                }
            }
            vertexQueue.remove(temp);
            visited[temp.getID()] = true;
            PseudoStep s2 = new PseudoStep(1);
            pseudoSteps.add(s2);
            s2.addStep(new VertexDetailStep("The first of PQ: (" + dist[temp.getID()] + "," + temp.getID() + ")\n" +
                    "Exploring neighbors of vertex u = " + temp.getID() + ",d[u] = " + dist[temp.getID()] + ".", true, temp));
            list = super.getData().getAdjList(temp);
            for (Vertex j : list) {
                w = super.getData().getLabelEdge(temp, j);
                PseudoStep s3 = new PseudoStep(2);
                pseudoSteps.add(s3);
                s3.addStep(new EdgeDetailStep("relax(" + temp.getID() + "," + j.getID() + "," + w + ").", true,
                        super.getData().getEdge(temp, j)));
                PseudoStep s4 = new PseudoStep(2);
                pseudoSteps.add(s4);
                if (dist[j.getID()] >= dist[temp.getID()] + w) {
                    dist[j.getID()] = dist[temp.getID()] + w;
                    previous[j.getID()] = temp.getID();
                    s4.addStep(new EdgeDetailStep("d[" + j.getID() + "] = d[" + temp.getID() + "] + w("
                            + temp.getID() + "," + j.getID() + ") = " + dist[temp.getID()] + " + " + w + " = " + dist[j.getID()]
                            + "p[" + j.getID() + "] = " + temp.getID() + ", PQ add (" + dist[j.getID()] + "," + j.getID() + ").", true,
                            super.getData().getEdge(temp, j)));
                } else {
                    isLight = previous[j.getID()] == -1 ? true : false;
                    text = "d[" + j.getID() + "] < d[" + temp.getID() + "] + w("
                            + temp.getID() + "," + j.getID() + ").so there is no change.";
                    s4.addStep(new EdgeDetailStep(text, isLight, super.getData().getEdge(temp, j)));
                    System.out.println(text);
                }
                if (!visited[j.getID()]) {
                    vertexQueue.add(j);
                }
            }

//            System.out.println(temp.getId());
        }
        PseudoStep s5 = new PseudoStep(2);
        pseudoSteps.add(s5);
        s5.addStep(new VertexDetailStep("This is the SSSP spanning tree from source vertex " + vertex.getID(), true, vertex));

        for (int k = 0; k < super.getData().getVertices().size(); k++) {
            str = "";
            System.out.print(k + ":");
            if (previous[k] == -1) {
                System.out.println("No path");
                continue;
            }
            w = k;
            while (previous[w] != -1) {
                str = str + String.valueOf(previous[w]);
                w = previous[w];
                if (previous[w] != -1)
                    str = str + ">";
            }
//            System.out.println(str);
            c.append(str);
            System.out.println(c.reverse() + ">" + k);
            c.delete(0, c.length());
        }

    }

    public Dijkstra() {
        pseudoCodes.add("show warning if the graph has -ve weight edge" +
                "\n" +
                "initSSSP, pre-populate PQ\n");
        pseudoCodes.add("while !PQ.empty() // PQ is a Priority Queue");
        pseudoCodes.add("\n\tfor each neighbor v of u = PQ.front(), PQ.pop()" +
                "\n" +
                "    relax(u, v, w(u, v)) + update PQ");
    }


}
