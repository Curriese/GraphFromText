 /**
 * GraphSearch interface (Programming assignment #1)
 */
public interface GraphSearch {

	/**
	 * Use depth first search to determine if there is a path from node1 to node2. 
	 * If a path exists return true otherwise return false
	 * @param node1 is the source node
	 * @param node2 is the destination node
	 * @return true if a path exits and false otherwise
	 */
	boolean pathExists(int node1, int node2);
	
	/**
	 * Use breadth first search to find and return the length of the shortest path 
	 * from node1 to node2. If no such path exists return -1.
	 * @param node1 is the source node
	 * @param node1 is the destination node
	 * @return length of the shortest path from node1 to node2. 
	 *         If no such path exists return -1
	 */
	int distance(int node1, int node2);
	
	/**
	 * Returns true if the graph is undirected and false otherwise. We will consider a graph 
	 * G(V,E) to be undirected if whenever (v,u) is in E, then (u,v) is in E.
	 * @return true if the graph is undirected and false otherwise
	 */
	boolean undirected( );
	
	/**
	 * Returns true if the graph has a cycle and false otherwise. Note that you must 
	 * handle directed and undirected graphs differently. 
	 * @return true if the graph has a cycle and false otherwise
	 */
	boolean cycle( );
	
	/**
	 * Return true if a directed graph is strongly connected or an undirected graph is
	 * is connected, and false otherwise. Note that you must 
	 * handle directed and undirected graphs differently. 
	 * @return true if the graph is connected and false otherwise
	 */
	boolean connected( );

}
