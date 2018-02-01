import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
        
	/**
	 *  Implements a GUI for inputting nodes and edges on a graph.
	 *  @author  Jiamin Li
	 *  @version 12/8/16
	 */

public class GraphGUI {
	/** The graph containing display nodes and display edge */
	private Graph<DisplayNodeData, DisplayEdgeData> graphCanvas;
	
    /** The graph to be displayed */
    private GraphView graphView;

    /** Label for the input mode instructions */
    private JLabel instruction;

    /** The input mode */
    InputMode mode = InputMode.ADD_POINTS;
    
    /** Counter for removing edges */
    int counter = 0;

    /** Remembers point where last mousedown event occurred */
    Graph<DisplayNodeData, DisplayEdgeData>.Node nodeUnderMouse;
    
    /** Also remembers point where last mousedown event occured, used for remembering two nodes */
    Graph<DisplayNodeData, DisplayEdgeData>.Node nodeUnderMouse2;

    /** Remembers color of last node on mousedown event */
    Color savedColor;

    /**
     *  Schedules a job for the event-dispatching thread
     *  creating and showing this application's GUI.
     */
    public static void main(String[] args) {
        final GraphGUI GUI = new GraphGUI();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    GUI.createAndShowGUI();
                }
            });
    }

    /** Sets up the GUI window */
    public void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Graph GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createComponents(frame);
        frame.pack();
        frame.setVisible(true);
    }
    

    /** Puts content in the GUI window */
    public void createComponents(JFrame frame) {
        Container pane = frame.getContentPane();
        JPanel instrPanel = new JPanel();
        instrPanel.setBackground(Color.white);
        instrPanel.setLayout(new BorderLayout());
        instrPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        graphCanvas = new Graph<DisplayNodeData, DisplayEdgeData>();
        graphView = new GraphView(graphCanvas);
        PointMouseListener pointM1 = new PointMouseListener();
        graphView.addMouseListener(pointM1);
        graphView.addMouseMotionListener(pointM1);
        instrPanel.add(graphView);
        instruction = new JLabel("Click to add new points or change their location. Right click to change the color.");
        instruction.setFont(new Font("Calibri", Font.PLAIN, 30));
        instrPanel.add(instruction,BorderLayout.NORTH);
        pane.add(instrPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,4));
        ArrayList<JButton> listOfButtons = new ArrayList<JButton>();
        
        // Initialize all the buttons
        JButton addPointButton = new JButton("A D D / M O V E | P O I N T S");
        addPointButton.addActionListener(new ButtonListener(InputMode.ADD_POINTS, "Click to add new points or change their location. Right click to change the color."));
        listOfButtons.add(addPointButton);
        
        JButton removePointButton = new JButton("R E M O V E | P O I N T S");
        removePointButton.addActionListener(new ButtonListener(InputMode.RMV_POINTS, "Click to remove points."));
        listOfButtons.add(removePointButton);
        
        JButton addEdgeButton = new JButton("A D D | E D G E S");
        addEdgeButton.addActionListener(new ButtonListener(InputMode.ADD_EDGES, "Click and drag to add an edge between two points."));
        listOfButtons.add(addEdgeButton);

        JButton removeEdgeButton = new JButton("R E M O V E | E D G E S");
        removeEdgeButton.addActionListener(new ButtonListener(InputMode.RMV_EDGES, "Click on two nodes to remove their edge."));
        listOfButtons.add(removeEdgeButton);

        JButton BFTButton = new JButton("T R A V E R S A L ( B F T )");
        BFTButton.addActionListener(new ButtonListener(InputMode.BFT, "Choose a starting node to begin breadth first traversal."));
        listOfButtons.add(BFTButton);

        JButton DFTButton = new JButton("T R A V E R S A L ( D F T )");
        DFTButton.addActionListener(new ButtonListener(InputMode.DFT, "Choose a starting node to begin depth first traversal."));
        listOfButtons.add(DFTButton);

        JButton DistanceButton = new JButton("D I S T A N C E");
        DistanceButton.addActionListener(new ButtonListener(InputMode.DISTANCE, "Select two nodes to find the shortest cost distance between them."));
        listOfButtons.add(DistanceButton);

        JButton ResetButton = new JButton("R E S E T");
        ResetButton.addActionListener(new ResetListener());
        listOfButtons.add(ResetButton);

        // Change the look of all the buttons
        for (JButton button : listOfButtons) {
        	button.setFont(new Font("Calibri", Font.PLAIN, 18));
            button.setForeground (Color.WHITE);
            button.setBackground(new Color(55, 55, 55));
            button.setOpaque(true);
            buttonPanel.add(button);
        }
        
        pane.add(buttonPanel,BorderLayout.SOUTH);
    }

    /** 
     * Returns a point found within the drawing radius of the given location, 
     * or null if none
     *
     *  @param x  the x coordinate of the location
     *  @param y  the y coordinate of the location
     *  @return  a point from the canvas if there is one covering this location, 
     *  or a null reference if not
     */
    public Graph<DisplayNodeData, DisplayEdgeData>.Node findNearbyNode(int x, int y) {
    	Point location = new Point(x, y);
    	for (Graph<DisplayNodeData, DisplayEdgeData>.Node node  : graphCanvas.getNodesList()) {
    		Point nodeCoords = node.getData().getPoint();
    		double distance = nodeCoords.distance(location);
    		if (distance <= graphView.getRadius()) {
    			return node;
    		}
    	}
    	return null;   
    }
    

    /** Constants for recording the input mode */
    enum InputMode {
        ADD_POINTS, RMV_POINTS, ADD_EDGES, RMV_EDGES, BFT, DFT, DISTANCE, RESET
    }
    
    /** Action listener for ResetButton */
    private class ResetListener implements ActionListener {
    	
    	public void actionPerformed(ActionEvent e) {
    		mode = InputMode.RESET;
    		instruction.setText("Resets all the nodes colors.");
    		// Loops through all the nodes in the graph and reverts all their colors to original red
    		for (Graph<DisplayNodeData, DisplayEdgeData>.Node node : graphCanvas.getNodesList()) {
    			node.getData().setColor(Color.RED);
    			graphView.repaint();
    		}
    	}
    }
    
    /** Button listener for several buttons */
    private class ButtonListener implements ActionListener {
    	/** Input mode of the current button */
    	private InputMode mo;
    	/** Text for instructions in current mode */
    	private String text;
    	
    	/*
    	 * Constructor takes in input mode and text for instructions
    	 */
    	ButtonListener(InputMode mo, String text) {
    		this.mo = mo;
    		this.text = text;
    	}
    	
    	/* Set global mode and instructions */
    	public void actionPerformed(ActionEvent e) {
    		mode = this.mo; 
    		instruction.setText(text);
    	}
    	
    }
    
    
    /** Listener for ChooseColorMenu */
    private class ColorMenuListener implements ActionListener {
    	/** Original node */
    	private Graph<DisplayNodeData, DisplayEdgeData>.Node node;
    	/** The new color to color the node */
    	private Color color;
    	
    	/*
    	 * Constructor
    	 */
    	ColorMenuListener(Graph<DisplayNodeData, DisplayEdgeData>.Node node, Color color) {
    		this.node = node;
    		this.color = color;
    	}
    	
    	/** Change color of the node */
    	public void actionPerformed(ActionEvent e) {
    		node.getData().setColor(this.color);
    		graphView.repaint();
    	}
    }
    
    /**
     * Creates a popup menu for changing a node's color
     * @param e Event (right-click) on which the node was clicked to change color 
     * @param node The node which was clicked
     */
    public void colorChooser(MouseEvent e, Graph<DisplayNodeData, DisplayEdgeData>.Node node) {
    	// Sets up the pop-up menu on right-click
    	JPopupMenu mainPopup = new JPopupMenu();
		JMenuItem orange = new JMenuItem("Holland Orange");
		JMenuItem yellow = new JMenuItem("Over Easy Yellow");
		JMenuItem red = new JMenuItem("Koka red");
		JMenuItem green = new JMenuItem("Bug Leg Green");
		JMenuItem teal = new JMenuItem("One Day Explosion Green");
		
		// Add action listener for each color
        orange.addActionListener(new ColorMenuListener(node, new Color(255, 138, 0)));
        yellow.addActionListener(new ColorMenuListener(node, new Color(250, 190, 40)));
        red.addActionListener(new ColorMenuListener(node, new Color(255, 0, 60)));
        green.addActionListener(new ColorMenuListener(node, new Color(136, 193, 0)));
        teal.addActionListener(new ColorMenuListener(node, new Color(0, 193, 118)));
        
        // Add to pop-up menu
		mainPopup.add(orange);
		mainPopup.add(yellow);
		mainPopup.add(red);
		mainPopup.add(green);
		mainPopup.add(teal);
		// Display pop-up menu at the location of the node
		mainPopup.show(e.getComponent(), e.getX(), e.getY());
    }
    
    /** Mouse listener for PointCanvas element */
    private class PointMouseListener extends MouseAdapter
        implements MouseMotionListener {

        /** Responds to click event depending on mode */
        public void mouseClicked(MouseEvent e) { 
        	Graph<DisplayNodeData, DisplayEdgeData>.Node clickedNode = findNearbyNode(e.getX(), e.getY());
            switch (mode) {
            case ADD_POINTS:
            	if (clickedNode == null) {
            		// Displays a popup menu asking for input
                    JFrame frame = new JFrame();
            		Point newPoint = new Point(e.getX(), e.getY());
            		String text = JOptionPane.showInputDialog(frame, "Enter data for this node or hit enter for an untitled node.", null);
            		if (text != null) {
            			DisplayNodeData displayNode = new DisplayNodeData(newPoint, text, new Color(255, 0, 60));
            			graphCanvas.addNode(displayNode);
            		} 
            	} else if (SwingUtilities.isRightMouseButton(e) && clickedNode != null) {
            		colorChooser(e, clickedNode);
            	} else { 
            		Toolkit.getDefaultToolkit().beep();
            	}
                break;
            case RMV_POINTS:
            	if (clickedNode != null) {
            		// Removes node that is clicked
            		graphCanvas.removeNode(clickedNode);
            	} else {
            		Toolkit.getDefaultToolkit().beep();
            	}
                break;
            case ADD_EDGES:
            	break;
            case RMV_EDGES:
            	if (clickedNode != null) {
            		// If the second click occurs on a node, find the edge to the previously clicked node and remove it
            		if (counter == 1) {
            			nodeUnderMouse2 = clickedNode;
            			if (nodeUnderMouse.edgeTo(nodeUnderMouse2) != null) {
	            			graphCanvas.removeEdge(nodeUnderMouse.edgeTo(nodeUnderMouse2));
	            			counter = 0;
            			}
            		} else { // First time clicking a node 
	            		counter++;
	            		nodeUnderMouse = clickedNode;
            		}
            	} 
            	break;
            case BFT:
            	if (clickedNode != null) {
            		graphView.BFT(clickedNode);
            	}
            	break;
            case DFT:
            	if (clickedNode != null) {
            		// Empty hashsets are required for the recursive DFT method
            		HashSet<Graph<DisplayNodeData, DisplayEdgeData>.Node> visited = new HashSet<Graph<DisplayNodeData, DisplayEdgeData>.Node>();
            		HashSet<Graph<DisplayNodeData, DisplayEdgeData>.Edge> listEdges = new HashSet<Graph<DisplayNodeData, DisplayEdgeData>.Edge>();
            		graphView.DFT(clickedNode, visited, listEdges);
            	} else {
            		Toolkit.getDefaultToolkit().beep();
            	}
            	break;
            case DISTANCE:
            	if (clickedNode != null) {
            		// Activates shortest distance method once second node is clicked
            		if (counter == 1) {
            			nodeUnderMouse2 = clickedNode;
            			JOptionPane.showMessageDialog(null, "The shortest distance between "+nodeUnderMouse.getData().getLabel()+"and "+
                    	nodeUnderMouse2.getData().getLabel()+" is "+Graph.distances(graphCanvas, nodeUnderMouse, nodeUnderMouse2), "Shortest Distance", JOptionPane.INFORMATION_MESSAGE);
            			counter = 0;
            		} else { // First time clicking a node 
	            		counter++;
	            		nodeUnderMouse = clickedNode;
            		}
            	} 
            	break;       				
            }
            graphView.repaint();
        }
        
        /** Records point under mousedown event in anticipation of possible drag */
        public void mousePressed(MouseEvent e) {
    		Graph<DisplayNodeData, DisplayEdgeData>.Node pressedNode = findNearbyNode(e.getX(),e.getY());
        	switch (mode) {
        	case ADD_POINTS:
        		if (pressedNode != null) {
        			// Nodes that are in the middle of a drag sequence are shown black
            		nodeUnderMouse = pressedNode;
            		savedColor = nodeUnderMouse.getData().getColor();
            		nodeUnderMouse.getData().setColor(Color.BLACK);
            	} 
        		break;
        	case RMV_POINTS:
        		break;
        	case ADD_EDGES:
        		if (pressedNode != null) {
        			nodeUnderMouse = pressedNode;
        		}
        		break;
        	case RMV_EDGES:
            	break;
        	}
        	graphView.repaint();
        }

        /** Responds to mouse released event */
        public void mouseReleased(MouseEvent e) {
    		Graph<DisplayNodeData, DisplayEdgeData>.Node nodeAtRelease = findNearbyNode(e.getX(),e.getY());
        	switch (mode) {
        	case ADD_POINTS:
        		if (nodeUnderMouse != null) {
        			nodeUnderMouse.getData().setColor(savedColor);
                	nodeUnderMouse = null;
                }
        		break;
        	case RMV_POINTS:
        		break;
        	case ADD_EDGES:
        		if (nodeUnderMouse != null && nodeAtRelease != null) {
        			Point p1 = nodeUnderMouse.getData().getPoint();
            		Point p2 = nodeAtRelease.getData().getPoint();
                    JFrame frame = new JFrame();
            		String text = JOptionPane.showInputDialog(frame, "Enter only a numeric cost for this edge or hit enter for no cost.", null);
            		try {
            			double cost = Double.parseDouble(text);
                		DisplayEdgeData newEdge = new DisplayEdgeData(p1, p2, Color.BLACK, cost);
                		graphCanvas.addEdge(newEdge, nodeUnderMouse, nodeAtRelease);
            		} catch (NumberFormatException exc) { // If user does not enter a numeric value
            			DisplayEdgeData newEdge = new DisplayEdgeData(p1, p2, Color.BLACK);
            			graphCanvas.addEdge(newEdge, nodeUnderMouse, nodeAtRelease);
            		} catch (NullPointerException exc) { // If the user presses cancel
            			DisplayEdgeData newEdge = new DisplayEdgeData(p1, p2, Color.BLACK);
            			graphCanvas.addEdge(newEdge, nodeUnderMouse, nodeAtRelease);
            		}
            		nodeUnderMouse = null;
        		}
    			graphView.removeLine();
        		break;
        	case RMV_EDGES:
        		break;
        	}
        	graphView.repaint();
        }

        /** Responds to mouse drag event */
        public void mouseDragged(MouseEvent e) {
        	switch (mode) {
        	case ADD_POINTS:
        		// Drags a node across the screen
        		// If a node has neighbors, the edges connecting to its neighbors will also move along with it
        		if (nodeUnderMouse != null) {
        			nodeUnderMouse.getData().setPoint(e.getPoint());
        			if (nodeUnderMouse.getNeighbors() != null) {
        				for (Graph<DisplayNodeData, DisplayEdgeData>.Node neighbor : nodeUnderMouse.getNeighbors()) {
        					Graph<DisplayNodeData, DisplayEdgeData>.Edge edgeRef = graphCanvas.getEdgeRef(nodeUnderMouse, neighbor);
        					edgeRef.getData().setP1(e.getPoint());
        					edgeRef.getData().setP2(neighbor.getData().getPoint());
        				}
        			}
            	}
        		break;
        	case RMV_POINTS:
        		break;
        	case ADD_EDGES:
        		if (nodeUnderMouse != null) {
        			// Draws a line from node to anywhere on the graph, does not necessarily become an edge
        			graphView.setLine(nodeUnderMouse.getData().getPoint(), e.getPoint());
        		}
        		break;
        	}
        	graphView.repaint();
			
        }

		// Empty but necessary to comply with MouseMotionListener interface.
        public void mouseMoved(MouseEvent e) {

        }

    }

}