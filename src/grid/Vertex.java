package grid;

import java.util.ArrayList;
import java.util.List;

/**
 * One of the elements of TSP problem that user can draw it on grid.
 * Each vertex can be displayed on grid only with x and y coordinates.
 * Each vertex has children too. children is vertex children in MST.
 * @author Majid Feyzi
 * */
public class Vertex {

	// Coordinate of vertex on grid
	private final int x, y;
	
	private List<Vertex> children = new ArrayList<>();

	public Vertex(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public List<Vertex> getChildren() {
		return children;
	}

	public void setChildren(List<Vertex> children) {
		this.children = children;
	}
}
