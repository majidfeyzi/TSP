package grid;

/**
 * One of the elements of TSP problem that user can draw it on grid.
 * Each edge can be displayed on grid only with start point and end point.
 * @author Majid Feyzi
 * */
public class Edge {

	// Start and end point of the edge
	private final Vertex start, end;
	
	public Edge(Vertex start, Vertex end) {
		this.start = start;
		this.end = end;
	}
	
	public Vertex getStart() {
		return start;
	}
	
	public Vertex getEnd() {
		return end;
	}
	
	/**
	 * Find distance between two points.
	 * @return distance between start point and end point of edge.
	 * */
	public int distance() {
		return (int)Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2));
	}
}
