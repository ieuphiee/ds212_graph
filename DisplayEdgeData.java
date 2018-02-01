import java.util.*;
import java.awt.*;

	/**
	 * Class for storing GUI edge data
	 * @author Jiamin Li
	 * @version 12/15/16
	 */
public class DisplayEdgeData extends Number {
	/** Coordinate of p1 */
	private Point p1;
	/** Coordinate of p2 */
	private Point p2;
	/** Color of edge */
	private Color color;
	/** Cost of edge, if any */
	private double cost;
	
	DisplayEdgeData(Point p1, Point p2, Color color) {
		this.p1 = p1;
		this.p2 = p2;
		this.color = color;
		this.cost = 1; // Default cost
	}
	
	/** Copy constructor */
	DisplayEdgeData(Point p1, Point p2, Color color, double cost) {
		this.p1 = p1;
		this.p2 = p2;
		this.color = color;
		this.cost = cost;
	}
	
	/**
	 * Accessor for Point 1
	 * @return Point 
	 */
	public Point getP1() {
		return this.p1;
	}
	
	/**
	 * Accessor for Point 2
	 * @return Point 
	 */
	public Point getP2() {
		return this.p2;
	}

	/**
	 * Accessor for edge color
	 * @return color
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Accessor for edge cost
	 * @return cost
	 */
	public double getCost() {
		return this.cost;
	}
	
	/**
	 * Sets p1 
	 * @param Point p1
	 */
	public void setP1(Point p1) {
		this.p1 = p1;
	}
	
	/**
	 * Sets p1 
	 * @param Point p2
	 */
	public void setP2(Point p2) {
		this.p2 = p2;
	}
	
	
	/**
	 * Set edge color
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Sets the edge cost
	 * @param cost
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	@Override
	/**
	 * Returns the cost of the edge (used in Dijkstra's method)
	 */
	public double doubleValue() {
		return this.cost;
	}

	@Override
	public float floatValue() {
		return 0;
	}

	@Override
	public int intValue() {
		return 0;
	}

	@Override
	public long longValue() {
		return 0;
	}
}
