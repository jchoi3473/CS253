
package edu.emory.cs.graph.span;

import edu.emory.cs.graph.Edge;
import edu.emory.cs.graph.Graph;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

public class MSTWordChoi extends MSTWord {
    List<Edge> edges;
    public MSTWordChoi(InputStream in) {
        super(in);
    }

    //Create Undirected graph to make it a complete graph
    @Override
    protected Graph createGraph() {
        Graph graph = new Graph(vertices.size());
        //Cosine Similarity
        //vertices get(0) word, get(1) vector
        //index from the list functions as int source/target
        for(int i=0;i<vertices.size()-1;i++){
            for(int j=i+1;j<vertices.size();j++){
                double a = 0, b = 0, ab = 0;
                //Calculate Cosine distance
                for(int count =0;count<vertices.get(i).getVector().length;count++) {
                    double[] temp1 = vertices.get(i).getVector();
                    double[] temp2 = vertices.get(j).getVector();
                    ab += temp1[count]*temp2[count];
                    a += temp1[count]*temp1[count];
                    b += temp2[count]*temp2[count];
                }
                double weight =1-( ab / (Math.sqrt(a)*Math.sqrt(b)));
                graph.setUndirectedEdge(i,j,weight);
            }
        }
        return graph;
    }
    //implemented Prim's algorithm for the getMinimumSpanningTree() as it is a dense graph
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
        return tree;
    }
    //Add method for getMinimumSpanningTree()
    private void add(PriorityQueue<Edge> queue, Set<Integer> visited, Graph graph, int target) {
        visited.add(target);
        for (Edge edge : graph.getIncomingEdges(target)) {
            if (!visited.contains(edge.getSource()))
                queue.add(edge);
        }
    }
    //Used Dijkstra Algorithm to get the shortest path
    public List<String> getShortestPath(int source, int target) {
        List<String> result = new ArrayList<>();
        PriorityQueue<VertexDistancePair> queue = new PriorityQueue<>();
        Integer[] previous = new Integer[graph.size()];
        double[] distances = new double[graph.size()];
        Set<Integer> visited = new HashSet<>();

        if(target>graph.size()||source>graph.size()){//if source/target doesn't exist, return empty list
            return result;
        }

        init(distances, previous, target);
        queue.add(new VertexDistancePair(target, 0));
        while (!queue.isEmpty()) {
            VertexDistancePair u = queue.poll();
            visited.add(u.vertex); //logV

            for (Edge edge : graph.getIncomingEdges(u.vertex)) {
                //Vertex that can be reached through current vertex
                int v = edge.getSource();
                //If the vertex has yet been visited
                if (!visited.contains(v)) {
                    //Calculated distance from target to v
                    double dist = distances[u.vertex] + edge.getWeight();
                    if (dist < distances[v]) {
                        distances[v] = dist;
                        previous[v] = u.vertex;
                        queue.add(new VertexDistancePair(v, dist));
                    }
                }
            }
        }

        int index = source;
        while(target!=index){
            result.add(vertices.get(index).getWord());
            index = previous[index];
        }
        result.add(vertices.get(target).getWord());
        return result;
    }
    //Initialize Dijkstra Algorithm
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
    //VertextDistancePair for Dijkstra Algorithm
    private class VertexDistancePair implements Comparable<VertexDistancePair> {
        public int vertex;
        public double distance;

        public VertexDistancePair(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
        @Override
        public int compareTo(VertexDistancePair p) {
            double diff = this.distance - p.distance;
            if (diff > 0) return 1;
            if (diff < 0) return -1;
            return 0;
        }
    }

    static public void main(String[] args) throws Exception {
        final String INPUT_FILE = "src/main/resources/word_vectors.txt";
        final String OUTPUT_FILE = "src/main/resources/word_vectors.dot";

        MSTWord mst = new MSTWordChoi(new FileInputStream(INPUT_FILE));
        SpanningTree tree = mst.getMinimumSpanningTree();
        mst.printSpanningTree(new FileOutputStream(OUTPUT_FILE), tree);
        System.out.println(tree.getTotalWeight());

        System.out.println(mst.getShortestPath(0,0));
        System.out.println(mst.getShortestPath(1,128));
        System.out.println(mst.getShortestPath(3,125));
        System.out.println(mst.getShortestPath(5,127));
        System.out.println(mst.getShortestPath(124,175));
        System.out.println(mst.getShortestPath(161,283));
        System.out.println(mst.getShortestPath(472,273));
        System.out.println(mst.getShortestPath(127,132));
        System.out.println(mst.getShortestPath(161,137));
        System.out.println(mst.getShortestPath(448,274));
    }
}


