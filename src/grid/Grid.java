package grid;

import grid.history.Action;
import grid.history.History;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * Grid is a component that user can draw objects such as edge, vertex and etc on it.
 * It also support Ctrl+z keyboard shortcut to undo actions.
 * Every class that inherit from Context class of grid can aware from changes.
 * @author Majid Feyzi
 * */
public class Grid extends JComponent {

	// Use history to keep history of actions
	private final History history = new History();

	// Context or class that use this class
	private final Context context;

	// User specified graph vertices in plane
	private final List<Vertex> vertices = new ArrayList<>();
	
	// Specify first vertex as start and end vertex of algorithm
	private Vertex start;
	
	// List of vertices that picked as MST
	private final List<Vertex> mst = new ArrayList<>();
	
	// List of vertices that picked as answer in preorder of mst
	private final List<Vertex> answer = new ArrayList<>();

	// Total cost of TSP answer
	private int cost = 0;

	// Timer to solve problem automatically and step by step
	private Timer timer;

	// Answer edge counter
	private int counter;

	// Grid plane to draw
	private Graphics2D graphics;
	private Image image;

	public Grid(Context context) {

		this.context = context;
		
		// Implement mouse click and double click event
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Vertex vertex = new Vertex(e.getX(), e.getY());
				addVertex(vertex);
			}
		});

		// Implement key press event
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "ctrlz");
		getActionMap().put("ctrlz", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				undo();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (image == null) {
			image = createImage(getSize().width, getSize().height);
			graphics = (Graphics2D) image.getGraphics();
			clear();
		}
		g.drawImage(image, 0, 0, null);	
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}
	
	/**
	 * Clear content of grid and lists of vertices and edges.
	 * */
	public void clear() {
		history.clear();
		vertices.clear();
		mst.clear();
		answer.clear();
		
		graphics.setPaint(Color.white);
		graphics.fillRect(0, 0, getSize().width, getSize().height);
		repaint();
	}
	
	/**
	 * Add vertices to canvas with specified coordinates on click.
	 * By adding each new vertex, edges is drawing automatically, because TSP graph is a complete graph.
	 * @param vertex vertex to show on plane
	 * */
	public void addVertex(Vertex vertex) {

		// Draw edge between new vertex and exist vertices
		List<Vertex> temp = new ArrayList<>(vertices);
		for (Vertex v : temp) {
			Edge edge = new Edge(vertex, v);
			drawEdge(edge);
		}
		
		graphics.setPaint(Color.black);
		history.push(Action.AddVertex);
		vertices.add(vertex);
		graphics.fillOval(vertex.getX() - (Config.VERTEX_RADIUS / 2), vertex.getY() - (Config.VERTEX_RADIUS / 2), Config.VERTEX_RADIUS, Config.VERTEX_RADIUS);

		if (vertices.size() == 1) {

			// Keep first vertex as start vertex
			start = vertex;
		
			// Highlight start vertex
			highlightStartVertex(vertex);
		}
		
		repaint();
	}

	/**
	 * Draw edge using start and end vertex.
	 * @param edge edge of graph to show on plane
	 * */
	public void drawEdge(Edge edge) {
		graphics.setPaint(Color.lightGray);
		graphics.setStroke(new BasicStroke(Config.EDGE_THICKNESS));
		graphics.drawLine(edge.getStart().getX(), edge.getStart().getY(), edge.getEnd().getX(), edge.getEnd().getY());
		
		// Find middle of edge and show it length
		int minx = Math.min(edge.getEnd().getX(), edge.getStart().getX());
		int miny = Math.min(edge.getEnd().getY(), edge.getStart().getY());
		int x = Math.abs(edge.getEnd().getX() - edge.getStart().getX()) / 2;
		int y = Math.abs(edge.getEnd().getY() - edge.getStart().getY()) / 2;
		graphics.setPaint(Color.gray);
		graphics.setFont(new Font(Config.LABEL_FONT_NAME, Font.PLAIN, Config.LABEL_FONT_SIZE));
		graphics.drawString(String.valueOf(edge.distance()), minx + x, miny + y);
		
		bringVerticesToFront();
	}

	/**
	 * Highlight MST edge.
	 * @param edge edge to be highlight
	 * */
	public void highlightMstEdge(Edge edge) {
		graphics.setPaint(Config.HIGHLIGHT_MST_EDGE_COLOR);
		graphics.setStroke(new BasicStroke(Config.HIGHLIGHTED_EDGE_THICKNESS));
		graphics.drawLine(edge.getStart().getX(), edge.getStart().getY(), edge.getEnd().getX(), edge.getEnd().getY());

		bringVerticesToFront();
	}

	/**
	 * Highlight answer edge.
	 * @param edge edge to be highlight
	 * */
	public void highlightEdge(Edge edge) {
		graphics.setPaint(Config.HIGHLIGHT_EDGE_COLOR);
		graphics.setStroke(new BasicStroke(Config.HIGHLIGHTED_EDGE_THICKNESS));
		graphics.drawLine(edge.getStart().getX(), edge.getStart().getY(), edge.getEnd().getX(), edge.getEnd().getY());

		bringVerticesToFront();
	}

	/**
	 * Highlight start vertex with a border.
	 * @param start vertex to be highlight
	 * */
	public void highlightStartVertex(Vertex start) {
		graphics.setPaint(Config.HIGHLIGHT_EDGE_COLOR);
		graphics.setStroke(new BasicStroke(4));
		graphics.drawOval(start.getX() - (Config.VERTEX_RADIUS / 2) - 5, start.getY() - (Config.VERTEX_RADIUS / 2) - 5, Config.VERTEX_RADIUS + 10, Config.VERTEX_RADIUS + 10);
		graphics.setStroke(new BasicStroke(1));
		graphics.setPaint(Color.black);
	}

	/*
	 * Redraw all objects of the grid.
	 * */
	private void redrawAllGraphics() {
		
		// First, clear grid graphics 
		graphics.setPaint(Color.white);
		graphics.fillRect(0, 0, getSize().width, getSize().height);
		
		for (Vertex vertex : vertices) {
			
			// Redraw edges
			for (Vertex p : vertices) {
				if (vertex != p) {
					graphics.setPaint(Color.lightGray);					
					Edge edge = new Edge(vertex, p);
					graphics.setStroke(new BasicStroke(Config.EDGE_THICKNESS));
					graphics.drawLine(edge.getStart().getX(), edge.getStart().getY(), edge.getEnd().getX(), edge.getEnd().getY());

					// Find middle of edge and show it length
					int minx = Math.min(edge.getEnd().getX(), edge.getStart().getX());
					int miny = Math.min(edge.getEnd().getY(), edge.getStart().getY());
					int x = Math.abs(edge.getEnd().getX() - edge.getStart().getX()) / 2;
					int y = Math.abs(edge.getEnd().getY() - edge.getStart().getY()) / 2;
					graphics.setPaint(Color.gray);
					graphics.setFont(new Font(Config.LABEL_FONT_NAME, Font.PLAIN, Config.LABEL_FONT_SIZE));
					graphics.drawString(String.valueOf(edge.distance()), minx + x, miny + y);
				}
			}
		}
		
		// Redraw vertices in front of all objects
		bringVerticesToFront();

		// Change focus to grid
		requestFocus();
	}

	/**
	 * Solve the problem and show result step by step.
	 * */
	public void next() {

		// First add start vertex to MST if is not in it
		if (!mst.contains(start))
			mst.add(start);
		
		// Keep remain vertices in a new list
		List<Vertex> remains = new ArrayList<>(vertices);
		remains.removeAll(mst);
		
		// If there is no remain vertex so MST has been completed
		if (remains.size() > 0) {

			// Find the next vertex with minimum distance from MST vertices
			Edge next = null;
			int min = 0;
			for (Vertex mvertex : mst) {
				for (Vertex vertex : remains) {
					Edge edge = new Edge(mvertex, vertex);
					int distance = edge.distance();
					if (min == 0 || min > distance) {
						min = distance;
						next = edge;
					}
				}
			}
			
			// Add next vertex to mst
			if (next != null) {
				mst.add(next.getEnd());
				highlightMstEdge(next);
				
				// Add new vertex as child to his parent
				List<Vertex> children = next.getStart().getChildren();
				children.add(next.getEnd());
				next.getStart().setChildren(children);
			}
			
		} else if (vertices.size() > 0) {
			
			// Clear answer 
			answer.clear();
			
			// Get preorder of MST
			preorder(start);

			if (counter < answer.size() - 1) {
				Edge edge = new Edge(answer.get(counter), answer.get(counter + 1));
				cost += edge.distance();
				highlightEdge(edge);
				counter++;
			} else {
				// Draw vertex from answer finish vertex to start vertex
				Edge edge = new Edge(answer.get(answer.size() - 1), start);
				cost += edge.distance();
				highlightEdge(edge);

				// Send final result to context
				if (cost > 0) {
					context.onSolveComplete("Cost: " + cost);
					cost = 0;
				}
				
				// Stop timer to auto solve problem after finish solving
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
				
				counter = 0;
			}
			
		} else {
			
			// Stop timer to auto solve problem after finish solving
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
		}
	}

	/**
	 * Undo actions that performed.
	 * */
	private void undo() {
		if (history.size() > 0) {

			Action action = history.pop();
			if (action == Action.AddVertex) {
				if (vertices.size() > 0) {

					// First remove vertex from graph
					vertices.remove(vertices.size() - 1);
				}
			}
			
			// Redraw graphics after remove last action 
			redrawAllGraphics();
		} else { 
			clear();
		}

		// Clear answer and queue to able user to solve problem after undo
		mst.clear();
	}
	
	/**
	 * Solve set cover problem step by step and automatically.
	 * */
	public void startAutoSolve() {
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					next();
				}
			}, 0, 1000);
		}
	}

	/**
	 * Generate random graph.
	 * x and y and width and height specifies vertices scale on grid.
	 * @param count count of vertices that must be generated
	 * @param x distance from left
	 * @param y distance from top
	 * @param width width of region that vertices will be located inside it
	 * @param height height of region that vertices will be located inside it
	 * */
	public void generateRandomGraph(int count, int x, int y, int width, int height) {
		
		// First clear all data
		clear();
		
		// Second wee need create vertices
		int counter = 1;
		Random random = new Random();
		while (counter <= count) {
			int rx = x + random.nextInt(width - x);
			int ry = y + random.nextInt(height - y);
			Vertex vertex = new Vertex(rx, ry);
			if (!vertices.contains(vertex)) {
				addVertex(vertex);
				counter++;
			}
		}

		// Change focus to grid
		requestFocus();
	}
	
	/**
	 * Get pre order of vertices in MST and add them into answer list to show on graph.
	 * @param start start vertex of graph
	 * */
	public void preorder(Vertex start) {
		answer.add(start);
		for (Vertex child : start.getChildren())
			preorder(child);
	}
	
	/**
	 * Bring vertices to front.
	 * */
	public void bringVerticesToFront() {

		// Redraw vertices with black colors after edges to bring vertices to front
		for (Vertex vertex : vertices) {

			graphics.setPaint(Color.black);
			graphics.fillOval(vertex.getX() - (Config.VERTEX_RADIUS / 2), vertex.getY() - (Config.VERTEX_RADIUS / 2), Config.VERTEX_RADIUS, Config.VERTEX_RADIUS);

			// Redraw and highlight start vertex
			if (vertex.equals(start))
				highlightStartVertex(vertex);
		}
		
		repaint();
	}

	/**
	 * To resize grid and recreate image in frame resize.
	 * */
	public void resize() {
		image = createImage(getSize().width, getSize().height);
		graphics = (Graphics2D) image.getGraphics();
		graphics.drawImage(image, 0, 0, null);	
		graphics.setStroke(new BasicStroke(Config.EDGE_THICKNESS));
		redrawAllGraphics();
	}
}
