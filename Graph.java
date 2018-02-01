import java.util.*;

/**
 * A sparse arrayList implementation of a graph
 * Containing two arraylists,
 * One for the Nodes of type N 
 * And another for Edges of type E
 * @author Jiamin Li
 * @version 12/7/16
 */

public class Graph<N, E> {
	/** Arraylist containing all the nodes in a graph */
	private ArrayList<Node> masterNodeList;
	/** Arraylist containing all the edges in a graph */
	private ArrayList<Edge> masterEdgeList;
	
	/**
	 * Constructor
	 * Initializes an empty array list of nodes and edges
	 */
	Graph() {
		this.masterNodeList = new ArrayList<Node>();
		this.masterEdgeList = new ArrayList<Edge>();
	}
	
	/**
	 * Returns the master node list
	 * @return masterNodeList
	 */
	public ArrayList<Node> getNodesList() {
		return this.masterNodeList;
	}
	
	/**
	 * Returns the master edge list
	 * @return masterEdgeList
	 */
	public ArrayList<Edge> getEdgesList() {
		return this.masterEdgeList;
	}
	
	/**
	 * Gets the node at specified index
	 * @param i The index in the node list
	 * @return node The node at index i
	 */
	public Node getNode(int i) {
		return this.masterNodeList.get(i);
	}
	
	/**
	 *  Gets the edge at specified index 
	 *  @param i The index in the edge list
	 *  @return edge The edge at index i
	 */
	public Edge getEdge(int i) {
		return this.masterEdgeList.get(i);
	}
	
	/**
	 * Gets the edge between a head and tail node
	 * @param head The head node
	 * @param tail The tail node
	 * @return edge The edge between the head and tail node connecting them
	 */
	public Edge getEdgeRef(Node head, Node tail) {
		for (Edge edge : this.masterEdgeList) {
			if ((edge.head == head && edge.tail == tail) || (edge.head == tail && edge.tail == head)) {
				return edge;
			}
			
		}
		return null;
	}
	
	/**
	 * Gets the number of edges in the entire graph
	 * @return num
	 */
	public int numEdges() {
		return this.masterEdgeList.size();
	}
	
	/**
	 * Gets the number of nodes in the entire graph
	 * @return num
	 */
	public int numNodes() {
		return this.masterNodeList.size();
	}
	
	/**
	 * Adds a node to the graph 
	 * @param nodeData The data that will go into the new node
	 * @return node The newly added node
	 */
	public Node addNode(N nodeData) {
		Node node = new Node(nodeData);
		this.masterNodeList.add(node);
		return node;
	}
	
	/**
	 *  Adds an edge to the graph
	 *  @param edgeData The edge's data
	 *  @param head The head node
	 *  @param tail The tail node
	 *  @return edge The newly added edge between the head and tail nodes
	 *   */
	public Edge addEdge(E edgeData, Node head, Node tail) {
		Edge edge = new Edge(edgeData, head, tail);
		this.masterEdgeList.add(edge);
		head.addEdge(edge);
		tail.addEdge(edge);
		return edge;
	}
	
	/**
	 * Removes an edge from the graph
	 * @param edge The edge to remove
	 */
	public void removeEdge(Edge edge) {
		edge.getHead().getEdgeList().remove(edge);
		edge.getTail().getEdgeList().remove(edge);
		this.masterEdgeList.remove(edge);
	}
	
	/**
	 * Removes an edge from the graph given head and tail nodes
	 * @param head The head of an edge
	 * @param tail The tail of an edge
	 */
	public void removeEdge(Node head, Node tail) { 
		Edge edge = head.edgeTo(tail); // The edge to remove;
		removeEdge(edge);
	}
	
	/**
	 * Removes a node from the graph
	 * @param node The node to remove
	 */
	public void removeNode(Node node) {
		ArrayList<Node> neighborList = node.getNeighbors();
		if (neighborList != null) {
			for (Node neighbor : neighborList) {
				removeEdge(node.edgeTo(neighbor));
			}
		}
		this.masterNodeList.remove(node);

	}
	
	/**
	 * Returns a list of all the nodes not in a given list
	 * @param group The given list of nodes
	 * @return list The list of nodes not in the given list
	 */
	public HashSet<Node> otherNodes(HashSet<Node> group) {
		HashSet<Node> list = new HashSet<Node>();
		for (Node node : this.masterNodeList) {
			if (!group.contains(node)) {
				list.add(node);
			}
		}
		return list;
	}
	
	/**
	 * Checks the graph for consistency
	 */
	public void check() {
		/** Does head and tail of an edge also link backto that edge? */
		for (Edge mEdge : this.masterEdgeList) {
			if (!mEdge.getHead().getEdgeList().contains(mEdge) && !mEdge.getTail().getEdgeList().contains(mEdge)) {
				System.out.println("Head or tail of edge "+mEdge.getData()+" does not link back to edge.");
			}
		/** Is every head and tail of an edge also in the master edge list? */
			if (!this.masterNodeList.contains(mEdge.getHead()) && !this.masterNodeList.contains(mEdge.getTail())) {
				System.out.println("Head or tail of "+mEdge.getData()+" is not in the master node list.");
			}
		}
		/** Every edge referenced by a node is in masterlist of edges */
		for (Node node : this.masterNodeList){
			if (node.getEdgeList().size() != 0) {
				for (Edge edge : node.getEdgeList()) {
					if (!this.masterEdgeList.contains(edge)) {
						System.out.println("Edge "+edge.getData()+" is not in the master edge list.");
					}
		/** Every edge for a node has that node as either its head or tail */
					if (edge.getHead() != node && edge.getTail() != node) {
						System.out.println("Head or tail of edge "+edge.getData()+" does not reference back to node "+node.getData());
					}
				}	
			}
		}
		System.out.println("Check done.");
		
	}
	
	/**
	 * Prints a basic representation of the graph
	 */
	public void print() {
		/** Print node list */
		for (Node node : this.masterNodeList) {
			System.out.print("Node: "+node.getData());
			if (node.getEdgeList().size() != 0) {
				System.out.print(" has edges: ");
				for (Edge edge : node.getEdgeList()) {
					System.out.print(edge.getData()+" ");
				}
			} else { System.out.print(" has no edges."); }
			System.out.println();
		}
		/** Print edge list */
		for (Edge edge : this.masterEdgeList) {
			System.out.println("Edge "+edge.getData()+" has the following endpoints: "+edge.getHead().getData()+" is head and "
					+edge.getTail().getData()+" is tail");
		}
	}
	
	/**
	 *  Performs depth-first traversal of a graph (recursive)
	 *  @param start The starting node
	 *  @visited hashset The hashset of of visited nodes (initial set should be empty)
	 *  @return traversedEdges The hashset of edges that have been traversed (initial set should be empty)
	 */
	public HashSet<Edge> DFT(Node node, HashSet<Node> visited, HashSet<Edge> traversedEdges) {
		if (visited.contains(node)) {
			return traversedEdges;
		} else {
			visited.add(node);
			if (node.getNeighbors() != null) {
				for (Node neighbor : node.getNeighbors()) {
					if (!traversedEdges.contains(node.edgeTo(neighbor))) {
						traversedEdges.add(node.edgeTo(neighbor));
						System.out.println("Nodes: "+node.getData()+" and "+neighbor.getData()+" are connected via edge "+node.edgeTo(neighbor).getData());
						traversedEdges = DFT(neighbor, visited, traversedEdges);
					}
				}
			}			
			return traversedEdges;
		}
	}
	
	/**
	 * Performs breadth-first traversal of a graph
	 * @param start The starting node
	 * @return traversedEdges The hashset of edges that have been traversed 
	 */
	public HashSet<Edge> BFT(Node start) {
		HashSet<Edge> traversedEdges = new HashSet<Edge>();
		HashSet<Node> visited = new HashSet<Node>();
		Queue<Node> queueNodes = new LinkedList<Node>();
		queueNodes.add(start);
		while (!queueNodes.isEmpty()) {
			Node currentNode = queueNodes.remove();
			visited.add(currentNode); // Marking as visited
			if (currentNode.getNeighbors() != null) {
				for (Node neighbor : currentNode.getNeighbors()) {
					if (!visited.contains(neighbor)) {
						queueNodes.add(neighbor);
						traversedEdges.add(currentNode.edgeTo(neighbor));
						System.out.println("Nodes: "+currentNode.getData()+" to "+neighbor.getData()+" connected via edge "+currentNode.edgeTo(neighbor).getData());
					}
				}
			}
		}
		/** Print out all nodes that are not reachable from the starting node (if any) */
		System.out.print("Unreachable nodes from node "+start.getData()+":");
		for (Node unreachable : otherNodes(visited)) {
			System.out.print(" "+unreachable.getData());
		}
		return traversedEdges;
	}
	
	
	/**
	 * Performs Dijkstra's shortest-path algorithm to compute the shortest distance between two nodes
	 * @param graph An instance of a graph
	 * @param start The starting node
	 * @param end The end node
	 * @return distance The shortest distance between the start and end node
	 */
	public static <S, T extends Number> double distances(Graph<S, T> graph, Graph<S, T>.Node start, Graph<S, T>.Node end){
		Map<Graph<S, T>.Node, Graph<S, T>.DijkstraNode> hashMap = new HashMap<Graph<S, T>.Node, Graph<S, T>.DijkstraNode>();
		for (Graph<S, T>.Node node : graph.masterNodeList) {
			hashMap.put(node, graph.new DijkstraNode(Double.POSITIVE_INFINITY, null, false));
		}
		hashMap.get(start).setCost(0);
		hashMap.get(start).setDirection(start);
		while (graph.getSmallestUnvisited(hashMap) != null) {
			Graph<S, T>.Node smallestUnvisited = graph.getSmallestUnvisited(hashMap);
			double currentCost = hashMap.get(smallestUnvisited).getCost();
			hashMap.get(smallestUnvisited).setVisited(true);
			if (smallestUnvisited.getNeighbors() != null) {
				for (Graph<S, T>.Node neighbor : smallestUnvisited.getNeighbors()) {
					if (!hashMap.get(neighbor).getVisited()) {
						double neighborsCost = hashMap.get(neighbor).getCost();
						double edgeCost = smallestUnvisited.edgeTo(neighbor).getData().doubleValue();
						double combinedCost = currentCost+edgeCost;
						if (combinedCost <= neighborsCost) {
							hashMap.get(neighbor).setCost(combinedCost);
							hashMap.get(neighbor).setDirection(smallestUnvisited);
						}
					}
				}
			}
		}

		return hashMap.get(end).getCost();
	}
	
	/**
	 * Gets the smallest unvisited nodes in a hashmap, if none return null
	 * @param hashMap
	 * @return true If there are any unvisited nodes, false otherwise
	 */
	private Node getSmallestUnvisited(Map<Node, DijkstraNode> hashMap){
		Node smallestUnvisited = null;
		double lowestSeenCost = Double.POSITIVE_INFINITY;
		for (Map.Entry<Node, DijkstraNode> entry : hashMap.entrySet()) {
		    Node nodeKey = entry.getKey();
		    DijkstraNode value = entry.getValue();
		    if (!value.visited && value.getCost() <= lowestSeenCost) {
		    	lowestSeenCost = value.getCost();
		    	smallestUnvisited = nodeKey;
		    }
		}
		return smallestUnvisited;
	}
	
	public class Node {
		/** Data stored in a node */
		private N data;
		/** Arraylist containing all the edges of a node **/
		private ArrayList<Edge> edgeList = new ArrayList<Edge>();
		
		/**
		 * Constructor
		 * @param data The data that goes into a node
		 */
		Node(N data) {
			this.data = data;
		}
		
		/**
		 *  Get data in node 
		 * @return data stored in a node
		 */
		public N getData() {
			return this.data;
		}
		
		/** 
		 * Get node's edge list 
		 * @return a node's edge list
		 */
		public ArrayList<Edge> getEdgeList() {
			return this.edgeList;
		}
		
		/**
		 *  Set data in node 
		 * @param node data
		 */
		public void setData(N data) {
			this.data = data;
		}
		
		/**
		 *  Adds edge to node's edgelist 
		 *  @param edge 
		 */
		public void addEdge(Edge edge) {
			edgeList.add(edge);
		}
		
		/**
		 * Returns edge to a specified node, or null if none
		 * @param neighbor neighbor node
		 * @return edge
		 */
		public Edge edgeTo(Node neighbor) {
			for (Edge edge : this.edgeList) {
				if (edge.getHead() == neighbor || edge.getTail() == neighbor) {
						return edge;
				}
			}
			return null; 
		}
		
		/**
		 * Returns an array list of all the neighbors of a node
		 * If no neighbors, returns null
		 * @return neighbors list of all nodes that are neighbors
		 */
		public ArrayList<Node> getNeighbors() {
			ArrayList<Node> neighbors = new ArrayList<Node>();
			if (this.edgeList.size() != 0) {
				for (Edge edge : edgeList) {
					if (edge.getHead() != null && edge.getTail() == this) {
						neighbors.add(edge.getHead());
					}
					if (edge.getHead() == this && edge.getTail() != null) {
						neighbors.add(edge.getTail());
					}
				}
				return neighbors;
			}
			return null;
		}
		
		/**
		 * Returns true if there is an edge to the node in question
		 * @param node Node in question
		 * @return true Returns true if node in question is a neighbor, otherwise returns false
		 */
		public boolean isNeighbor(Node node) {
			return this.edgeTo(node) != null;
		}
		
		/**
		 * Removes an edge from a node's edgelist 
		 * @param edge The edge to be removed
		 */
		public void removeEdge(Edge edge) {
			if (this.edgeList.size() != 0 && this.edgeList.contains(edge)) {
				edge.tail.getEdgeList().remove(edge);
				//edge.tail = null;
				edge.head.getEdgeList().remove(edge);
				//edge.head = null;
				masterEdgeList.remove(edge);
			}
			
		}
	}
	
	public class Edge {
		/** Data stored in an edge */
		private E data;
		/** Contains reference to edge's head (starting) node */
		private Node head;
		/** Contains reference to edge's tail (ending) node */
		private Node tail;
		
		/**
		 * Constructor
		 * @param data Data contained in the edge
		 * @param head Head node of edge
		 * @param tail Tail node of edge
		 */
		Edge(E data, Node head, Node tail) {
			this.data = data;
			this.head = head;
			this.tail = tail;
		}
		
		/**
		 * Get edge's data 
		 * @return Edge's data
		 */
		public E getData() {
			return this.data;
		}
		
		/*
		 *  Get an edge's head node 
		 *  @return edge's The edge's head node
		 */
		public Node getHead() {
			return this.head;
		}
		
		/**
		 *  Get edge's tail node 
		 *  @return edge's The edge's tail node
		 */
		public Node getTail() {
			return this.tail;
		}
		
		/**
		 * Gets opposite node 
		 * @param start Starting node
		 * @return opposite Node opposite of the starting node, if any
		 */
		public Node oppositeTo(Node start) {
			if (start.edgeList.size() != 0 && start.edgeList.contains(this)) {
				if (start == this.head) {
					return this.tail;
				} else {
					return this.head; 
				}
			} 
			return null;
		}
		
		/** 
		 * Set edge's data 
		 * @param edge The edge's data
		 */
		public void setData(E data) {
			this.data = data;
		}
		
		/**
		 * @Override
		 * Returns true if two edges contain the same endpoint
		 * @param o The object to compare to
		 * @return true if the two edges are the same
		 */
		public boolean equals(java.lang.Object o) {
			
			boolean result = false;
			
		    if (getClass() == o.getClass()) {
	                @SuppressWarnings("unchecked")
	                    Edge edge = (Edge)o;
	                if (edge.getHead() == this.head && edge.getTail() == this.tail) {
	    				result = true;
	    			} else if (edge.getTail() == this.head && edge.getHead() == this.tail) {
	    				result = true;
	    			} else {
	    				result = false;
	    			}
	            }
	            return result;			
			
		}
		
		/** 
		 * @Override
		 * Redefines hashcode to match redefined equals
		 * @return new hashcode
		 */
		public int hashCode() {
			return (this.getHead().hashCode()*this.getTail().hashCode());
		}
		
	}
	
	public class DijkstraNode {
		/** Contains node cost */
		private double cost;
		/** Contains homeward node */
		private Node direction;
		/** Visited or unvisited node? */
		private boolean visited = false;
		
		/**
		 * Constructor
		 * @param cost The cost of the Dijkstra node
		 * @param direction The direction of the Dijkstra node
		 * @param visited Boolean value for if the Dijkstra node has been visited
		 */
		DijkstraNode(double cost, Node direction, boolean visited) {
			this.cost = cost;
			this.direction = direction;
			this.visited = visited;
		}
		
		/**
		 * Accessor for cost
		 * @return cost The cost of the Dijkstra node
		 */
		public double getCost(){
			return this.cost;
		}
		
		/**
		 * Accessor for homeward node or direction 
		 * @return direction The direction of the Dijkstra node
		 */
		public Node getDirection(){
			return this.direction;
		}
		
		/**
		 * Accessor for visited
		 * @return visited
		 */
		public boolean getVisited(){
			return this.visited;
		}
		
		/**
		 * Sets node cost
		 * @param cost The new cost of the Dijkstra node
		 */
		public void setCost(double cost){
			this.cost = cost;
		}
		
		/**
		 * Sets direction
		 * @param direction The new direction of the Dijkstra node
		 */
		public void setDirection(Node direction){
			this.direction = direction;
		}
		
		/**
		 * Sets visited boolean value
		 * @param visited 
		 */
		public void setVisited(boolean visited){
			this.visited = visited;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
