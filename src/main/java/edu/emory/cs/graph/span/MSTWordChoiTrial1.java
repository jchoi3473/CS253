
package edu.emory.cs.graph.span;

import edu.emory.cs.graph.Edge;
import edu.emory.cs.graph.Graph;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

public class MSTWordChoiTrial1 extends MSTWord {
    List<Edge> edges;
    public MSTWordChoiTrial1(InputStream in) {
        super(in);
    }

    @Override
    protected Graph createGraph() {
        Graph graph = new Graph(vertices.size());
        //Cosine Similarity
        //vertices get(0) word, get(1) vector
        //index from the list functions as int source/target
        // System.out.println("vertices size "+vertices.size());
        for(int i=0;i<vertices.size();i++){
            for(int j=0;j<vertices.size();j++){
                double a = 0, b = 0, ab = 0;

                for(int count =0;count<vertices.get(i).getVector().length;count++) {
                    double[] temp1 = vertices.get(i).getVector();
                    double[] temp2 = vertices.get(j).getVector();
                    ab += temp1[count]*temp2[count];
                    a += temp1[count]*temp1[count];
                    b += temp2[count]*temp2[count];
                }

                double weight =1-( ab / (Math.sqrt(a)*Math.sqrt(b)));
                //System.out.println("weight "+weight);
                graph.setDirectedEdge(i,j,weight);
            }
        }
        //System.out.println("elements in graph  "+graph.getAllEdges().size());
        return graph;
    }

    public SpanningTree getMinimumSpanningTree() {
        //System.out.println("Graph "+graph.getAllEdges().size());
        PriorityQueue<Edge> queue = new PriorityQueue<>();
        SpanningTree tree = new SpanningTree();
        Set<Integer> visited = new HashSet<>();
        Edge edge;
        //Add all connecting vertices from start vertex to the queue
        add(queue, visited, graph, 0);
        while (!queue.isEmpty()) {
            edge = queue.poll();
            if (!visited.contains(edge.getSource())) {
                tree.addEdge(edge);
                //If a spanning tree is found, break.
                if (tree.size() + 1 == graph.size()) break;
                //Add all connecting vertices from current vertex to the queue
                add(queue, visited, graph, edge.getSource());
            }
        }
        // System.out.println("tree size "+tree.size());
        return tree;
    }

    private void add(PriorityQueue<Edge> queue, Set<Integer> visited, Graph graph, int target) {
        visited.add(target);
        for (Edge edge : graph.getIncomingEdges(target)) {
            if (!visited.contains(edge.getSource()))
                queue.add(edge);
        }
    }

    public List<String> getShortestPath(int source, int target) {
        Comparator<Edge> edgeComparator = new Comparator<>() {
            @Override
            public int compare(Edge e1, Edge e2) {
                if(e1.getWeight()>e2.getWeight())
                    return 1;
                else if(e1.getWeight()<e2.getWeight())
                    return -1;
                else
                    return 0;
            }
        };

        PriorityQueue<Edge> queue = new PriorityQueue<>(edgeComparator);
        Integer[] previous = new Integer[graph.size()];
        double[] distances = new double[graph.size()];
        List<String> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        result.add("d");
        init(distances, previous, target);
        queue.add(new Edge(target,target, 0));
        double accWeight = 0;
        while (!queue.isEmpty()) {
            Edge u = queue.poll();
            visited.add(u.getTarget()); //logV

            for (Edge edge : graph.getIncomingEdges(u.getTarget())) {
                //Vertex that can be reached through current vertex
                int v = edge.getSource();

                //If the vertex has yet been visited
                if (!visited.contains(v)) {
                    //Calculated distance from target to v
                    double dist = distances[u.getTarget()] + edge.getWeight();

                    if (dist < distances[v]) {
                        accWeight+=dist;
                        System.out.println("distance "+accWeight);
                        distances[v] = dist;
                        previous[v] = u.getTarget();
                        queue.add(new Edge(v,v, dist));
                    }
                }
            }
        }

        return result;
    }



    private void init(double[] distances, Integer[] previous, int target) {
        for (int i = 0; i < distances.length; i++) {
            //Set distance from target to target as the heuristic value
            if (i == target)
                distances[i] = 0;
            else {
                //Initialize all distance to infinity
                distances[i] = Double.MAX_VALUE;
                //Initialize all previous vertices to null
                previous[i] = null;
            }
        }
    }

    static public void main(String[] args) throws Exception {
        final String INPUT_FILE = "src/main/resources/word_vectors.txt";
        final String OUTPUT_FILE = "src/main/resources/word_vectors.dot";

        MSTWord mst = new MSTWordChoiTrial1(new FileInputStream(INPUT_FILE));
        SpanningTree tree = mst.getMinimumSpanningTree();
        mst.printSpanningTree(new FileOutputStream(OUTPUT_FILE), tree);
        List<String> test = mst.getShortestPath(1,55);
        System.out.println(tree.getTotalWeight());
    }
}

//    public SpanningTree getAllTree() {
//        //System.out.println("Graph "+graph.getAllEdges().size());
//        PriorityQueue<Edge> queue = new PriorityQueue<>();
//        SpanningTree tree = new SpanningTree();
//        Set<Integer> visited = new HashSet<>();
//        Edge edge;
//        //Add all connecting vertices from start vertex to the queue
//
//        List<Edge> edges = graph.getAllEdges();
//        for(Edge e : edges){
//            tree.addEdge(e);
//        }
//        // System.out.println("tree size "+tree.size());
//        return tree;
//    }


