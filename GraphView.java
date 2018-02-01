import java.util.*;
import java.util.Queue;
import javax.swing.*;
import java.awt.*;

	/**
	 * Displays the information stored in a graph
	 * @author Jiamin Li
	 * @version 12/15/16
	 */

public class GraphView extends JComponent {
	/** Graph containing display nodes and display edges */
	private Graph<DisplayNodeData, DisplayEdgeData> graphCanvas;
	/** Radius of points */
	private int diameter;
	/** Contains points of a line being currently drawn */
	private Point[] line;
	
	/**
	 * Constructor
	 * @param graphCanvas The graph to be displayed
	 */
	GraphView(Graph<DisplayNodeData, DisplayEdgeData> graphCanvas) {
		super();
		this.diameter = 20; // Default diameter of points
		this.graphCanvas = graphCanvas;
		setMinimumSize(new Dimension(1000,600));
    	setPreferredSize(new Dimension(1000,600));
	}
	
	/**
	 * Accessor for diameter of point
	 * @return int diameter
	 */
    public int getDiameter() {
    	return this.diameter;
    }
    
    /**
     * Accessor for line
     * @return Points on the line
     */
    public Point[] getLine() {
    	return this.line;
    }
    
    /**
     * Gets radius 
     * @return int radius is half of diameter
     */
    public int getRadius() {
    	return this.diameter/2;
    }
    
    /**
     * Sets diameter of point
     * @param diameter
     */
    public void setDiameter (int diameter) {
    	this.diameter = diameter;
    }
    
    /**
     * Sets coordinates of the line
     * @param p1 Point 1
     * @param p2 Point 2
     */
    public void setLine(Point p1, Point p2) {
    	this.line = new Point[2];
    	this.line[0] = p1;
    	this.line[1] = p2;
    }
    
    /**
     * Removes line on release
     */
    public void removeLine() {
    	this.line = null;
    }
    
	/**
	 *  Performs depth-first traversal of a displayNodeData & displayEdgeData graph
	 *  @param start -ing node
	 *  @visited hashset of visited nodes, initial set should be empty
	 *  @return traversedEdges hashset of edges traversed, initial set should be empty
	 */
	public HashSet<Graph<DisplayNodeData, DisplayEdgeData>.Edge> DFT(Graph<DisplayNodeData, DisplayEdgeData>.Node node, 
			HashSet<Graph<DisplayNodeData, DisplayEdgeData>.Node> visited, HashSet<Graph<DisplayNodeData, DisplayEdgeData>.Edge> traversedEdges) {
		if (visited.contains(node)) {
			return traversedEdges;
		} else {
			visited.add(node);
			node.getData().setColor(Color.BLACK);
			if (node.getNeighbors() != null) {
				for (Graph<DisplayNodeData, DisplayEdgeData>.Node neighbor : node.getNeighbors()) {
					if (!traversedEdges.contains(node.edgeTo(neighbor))) {
						traversedEdges.add(node.edgeTo(neighbor));
						traversedEdges = DFT(neighbor, visited, traversedEdges);
					}
				}
			}			
			return traversedEdges;
		}
	}
	
	/**
	 * Performs breadth-first traversal of a graph
	 * @param start -ing node
	 * @return traversedEdges hashset of edges traversed 
	 */
	public HashSet<Graph<DisplayNodeData, DisplayEdgeData>.Edge> BFT(Graph<DisplayNodeData, DisplayEdgeData>.Node start) {
		HashSet<Graph<DisplayNodeData, DisplayEdgeData>.Edge> traversedEdges = new HashSet<Graph<DisplayNodeData, DisplayEdgeData>.Edge>();
		HashSet<Graph<DisplayNodeData, DisplayEdgeData>.Node> visited = new HashSet<Graph<DisplayNodeData, DisplayEdgeData>.Node>();
		Queue<Graph<DisplayNodeData, DisplayEdgeData>.Node> queueNodes = new LinkedList<Graph<DisplayNodeData, DisplayEdgeData>.Node>();
		queueNodes.add(start);
		while (!queueNodes.isEmpty()) {
			Graph<DisplayNodeData, DisplayEdgeData>.Node currentNode = queueNodes.remove();
			visited.add(currentNode); // Marking as visited
			currentNode.getData().setColor(Color.BLACK);
			if (currentNode.getNeighbors() != null) {
				for (Graph<DisplayNodeData, DisplayEdgeData>.Node neighbor : currentNode.getNeighbors()) {
					if (!visited.contains(neighbor)) {
						queueNodes.add(neighbor);
						traversedEdges.add(currentNode.edgeTo(neighbor));
					}
				}
			}
			
		}
		return traversedEdges;
	}
    
    /**
     *  Paints a colored circle at each point.
     *  @param g The graphics object to draw with
     */
    public void paintComponent(Graphics g) {
    	
    	// Draw current line if one exists (drag operation)
    	if (this.line != null) {
    		g.drawLine(this.line[0].x, this.line[0].y, this.line[1].x, this.line[1].y);  
    	}
    	
    	// Paint the nodes 
    	for (Graph<DisplayNodeData, DisplayEdgeData>.Node node : graphCanvas.getNodesList()) {
    		Color color = node.getData().getColor();
    		g.setColor(color);
    		Point point = node.getData().getPoint();
    		g.fillOval(point.x-getRadius(), point.y-getRadius(), this.diameter, this.diameter);
    		String text = node.getData().getLabel();
    		g.drawString(text, point.x-getRadius(), point.y-getRadius());
    	}
    	
    	// Paint the edges
    	for (Graph<DisplayNodeData, DisplayEdgeData>.Edge edge : graphCanvas.getEdgesList()) {
    		Color color = edge.getData().getColor();
    		g.setColor(color);
    		Point p1 = edge.getData().getP1();
    		Point p2 = edge.getData().getP2();
    		g.drawLine(p1.x, p1.y, p2.x, p2.y);
    		String strCost =  String.valueOf(edge.getData().getCost());
    		int midX = (p1.x+p2.x)/2;
    		int midY = (p1.y+p2.y)/2;
    		g.drawString(strCost, midX, midY);
    	}
    	
    }
        
    
}
