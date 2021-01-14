package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    @Override
    public String toString() {
        return "Graph{" +
                "adjVertices=" + adjVertices +
                '}';
    }

    private Map<Vertex, List<Vertex>> adjVertices = new HashMap<Vertex, List<Vertex>>();

    public Map<Vertex, List<Vertex>> getAdjVertices() {
        return adjVertices;
    }

    public void setAdjVertices(Map<Vertex, List<Vertex>> adjVertices) {
        this.adjVertices = adjVertices;
    }

    public void addVertex(Integer label) {
        this.adjVertices.putIfAbsent(new Vertex(label), new ArrayList<>());
    }

    public void removeVertex(Integer label) {
        Vertex v = new Vertex(label);
        this.adjVertices.values().stream().forEach(e -> e.remove(v));
        this.adjVertices.remove(new Vertex(label));
    }

    public void addEdge(Integer label1, Integer label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        this.adjVertices.get(v1).add(v2);
        this.adjVertices.get(v2).add(v1);
    }

    public void removeEdge(Integer label1, Integer label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        List<Vertex> eV1 = this.adjVertices.get(v1);
        List<Vertex> eV2 = this.adjVertices.get(v2);
        if (eV1 != null) {
            eV1.remove(v2);
        }
        if (eV2 != null) {
            eV2.remove(v1);
        }
    }

    List<Vertex> getAdjVertices(Integer label) {
        return this.adjVertices.get(new Vertex(label));
    }
}
