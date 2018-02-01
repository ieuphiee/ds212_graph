import java.util.*;
import java.awt.*;

/**
 * Class for storing GUI node data
 * @author Jiamin Li
 * @version 12/15/16
 */

public class DisplayNodeData {
	/** Contains coordinate of node */
	private Point point;
	/** Contains label for node */
	private String label;
	/** Contains color of node */
	private Color color;
	
	/**
	 * Constructor
	 * @param point The coordinates of the node
	 * @param label The label (data) of the node
	 * @param color The color of the node
	 */
	DisplayNodeData(Point point, String label, Color color) {
		this.point = point;
		this.label = label;
		this.color = color;
	}
	
	/**
	 * Accessor for display node coordinate
	 * @return point
	 */
	public Point getPoint() {
		return this.point;
	}
	
	/**
	 * Accessor for display node label
	 * @return E label data
	 */
	public String getLabel() {
		return this.label;
	}
	
	/**
	 * Accessor for display node color
	 * @return color
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Sets display node coordinate
	 * @param point
	 */
	public void setPoint(Point point) {
		this.point = point;
	}
	
	/**
	 * Sets the node label
	 * @param label 
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Sets the node color
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
}
