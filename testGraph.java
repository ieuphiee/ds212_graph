import java.util.*;
import java.awt.*;
	/**
	 * For testing graph class
	 * @author Jiamin Li
	 * @version 12/7/16
	 */

public class testGraph {
	
	public static void main(String[] args) {
		
		Graph<String, Double> graph = new Graph<String, Double>();

		Graph<String, Double>.Node a = graph.addNode("A");
		Graph<String, Double>.Node b = graph.addNode("B");
		Graph<String, Double>.Node c = graph.addNode("C");
		Graph<String, Double>.Node d = graph.addNode("D");
		Graph<String, Double>.Node e = graph.addNode("E");
		Graph<String, Double>.Node f = graph.addNode("F");
		Graph<String, Double>.Node g = graph.addNode("G");
		Graph<String, Double>.Node h = graph.addNode("H");	
		
		Graph<String, Double>.Edge atob = graph.addEdge(8.0, a, b);
		Graph<String, Double>.Edge atoc = graph.addEdge(2.0, a, c);
		Graph<String, Double>.Edge atod = graph.addEdge(5.0, a, d);
		Graph<String, Double>.Edge btod = graph.addEdge(2.0, b, d);
		Graph<String, Double>.Edge ctod = graph.addEdge(2.0, c, d);
		Graph<String, Double>.Edge btof = graph.addEdge(13.0, b, f);
		Graph<String, Double>.Edge ctoe = graph.addEdge(5.0, c, e);
		Graph<String, Double>.Edge dtoe = graph.addEdge(1.0, d, e);
		Graph<String, Double>.Edge dtog = graph.addEdge(3.0, d, g);
		Graph<String, Double>.Edge dtof = graph.addEdge(6.0, d, f);
		Graph<String, Double>.Edge ftog = graph.addEdge(2.0, f, g);
		Graph<String, Double>.Edge etog = graph.addEdge(1.0, e, g);
		Graph<String, Double>.Edge ftoh = graph.addEdge(3.0, f, h);
		Graph<String, Double>.Edge gtoh = graph.addEdge(6.0, g, h);

		System.out.println(Graph.distances(graph, a, f));
		
		
		
		//Graph<String, Double>.Edge atob = graph.addEdge(8.0, a, b);
	}

}
