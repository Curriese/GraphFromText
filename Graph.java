/*
 Name: Spenser Currier
 Assignment: Programing Assignment 1
 Title: Graph implementation
 Course: CS 371
 Class section: 1
 Semester: Fall 2019
 Instructor: Dr. wolff
 Date: 10-09-2019
 Sources consulted: Sola Gbenro, Daniel Carver, Colton Freitas
 Known Bugs: None
 Program description: Graph class for creating graphs based on a file
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Graph implements GraphSearch {

    //ArrayList of ArrayLists of Integers to store the information about the graph
    private ArrayList<ArrayList<Integer>> adjacencyList = new ArrayList<>();

    /**
     * Constructor for creating a graph given an input file
     * @param fileName is the file that is read to create the graph
     * @throws IOException if there is a error when reading the file
     */
    public Graph(String fileName) throws IOException{
        File file = new File(fileName);
        Scanner sc = new Scanner(file);
        String firstLine = sc.nextLine();
        int nodes = Integer.parseInt(firstLine);
        for(int i = 0; i < nodes; i++){
            adjacencyList.add(new ArrayList<>());
        }
        int k = 0;
        while (sc.hasNextLine()) {
            Scanner readingLine = new Scanner(sc.nextLine());
            while (readingLine.hasNextInt()) {
                adjacencyList.get(k).add(readingLine.nextInt());
            }
            k++;
        }
        for(int j = 0; j < adjacencyList.size(); j++){
            adjacencyList.get(j).sort(Integer::compareTo);
        }
        System.out.println("AdjList = " + adjacencyList.toString());
    }

    /**
     * Use depth first search to determine if there is a path from node1 to node2.
     * If a path exists return true otherwise return false
     *
     * @param node1 is the source node
     * @param node2 is the destination node
     * @return true if a path exits and false otherwise
     */
    @Override
    public boolean pathExists(int node1, int node2) {
        ArrayList<Integer> markArray = new ArrayList<>(adjacencyList.size());
        return(dfsPathSearch(node1,node2,markArray));
    }

    /**
     *
     * @param node1 is the starting node
     * @param node2 is the destination node
     * @param markArray is the array that contains what nodes have been detected
     * @return  True if a path exists false other wise
     */
    private boolean dfsPathSearch(int node1, int node2, ArrayList<Integer> markArray){
        markArray.add(node1);
        ArrayList<Integer> adjNodes = adjacencyList.get(node1);
        for(int j = 0; j < adjNodes.size(); j++){
            if(adjNodes.get(j) == node2 && node1 != node2){
                return true;
            }
            if(!markArray.contains(adjNodes.get(j))){
                if(dfsPathSearch(adjNodes.get(j),node2,markArray)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Use breadth first search to find and return the length of the shortest path
     * from node1 to node2. If no such path exists return -1.
     *
     * @param node1 is the source node
     * @param node2 is the destination node
     * @return length of the shortest path from node1 to node2.
     * If no such path exists return -1
     */
    @Override
    public int distance(int node1, int node2) {
        Queue<Integer> processQ = new LinkedList<>();
        ArrayList<Integer> distanceArray = new ArrayList<>(adjacencyList.size());
        ArrayList<Integer> markArray = new ArrayList<>();
        for(int k = 0; k < adjacencyList.size(); k++){
            distanceArray.add(-1);
        }
        if(!pathExists(node1, node2)){
            return -1;
        }
        processQ.add(node1);
        distanceArray.set(node1, 0);
        markArray.add(node1);
        while(!processQ.isEmpty()){
            ArrayList<Integer> adjNodes = adjacencyList.get(processQ.peek());
            for(int j = 0; j < adjNodes.size(); j++){
                if(adjNodes.get(j) == node2){
                    distanceArray.set(adjNodes.get(j),distanceArray.get(processQ.peek())+1);
                    return distanceArray.get(adjNodes.get(j));
                }
                if(!markArray.contains(adjNodes.get(j))){
                    markArray.add(adjNodes.get(j));
                    distanceArray.set(adjNodes.get(j),distanceArray.get(processQ.peek())+1);
                    processQ.add(adjNodes.get(j));
                }
            }
            processQ.remove();
        }
        return 0;
    }

    /**
     * Returns true if the graph is undirected and false otherwise. We will consider a graph
     * G(V,E) to be undirected if whenever (v,u) is in E, then (u,v) is in E.
     *
     * @return true if the graph is undirected and false otherwise
     */
    @Override
    public boolean undirected() {
        for(int i = 0; i < adjacencyList.size(); i++){
            for(int tempNode : adjacencyList.get(i)){
                if(!adjacencyList.get(tempNode).contains(i)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns true if the graph has a cycle and false otherwise. Note that you must
     * handle directed and undirected graphs differently.
     *
     * @return true if the graph has a cycle and false otherwise
     */
    @Override
    public boolean cycle() {
        ArrayList<Integer> markArray = new ArrayList<>();
        ArrayList<Integer> finishArray = new ArrayList<>();
        ArrayList<Integer> parentArray = new ArrayList<>();
        for(int i = 0; i < adjacencyList.size(); i++){
            parentArray.add(-1);
        }
        if(undirected()){
            return(undirectedCycle(0, markArray, parentArray));
        }
        else{
            return(directedCycle(0,markArray, finishArray));
        }
    }

    /**
     * Cycle detection for undirected graphs
     * @param startNode the first node in the graph
     * @param markArray is the array that contains which nodes have been visited
     * @param parentArray is the array that contains which node is each nodes parent in the depth-first search
     * @return true if the graph has a cycle and false otherwise
     */
    private boolean undirectedCycle(int startNode,ArrayList<Integer> markArray, ArrayList<Integer> parentArray){
        markArray.add(startNode);
        ArrayList<Integer> adjNodes = adjacencyList.get(startNode);
        for(int i = 0; i < adjNodes.size(); i++){
            parentArray.set(adjNodes.get(i),startNode);
            if(markArray.contains(adjNodes.get(i)) && parentArray.get(startNode) != -1){
                return true;
            }
            if(undirectedCycle(adjNodes.get(i),markArray,parentArray)){
                return true;
            }
        }
        return false;
    }

    /**
     * Cycle detection for directed graphs
     * @param startNode is the first node in the graph
     * @param markArray is the array that contains which nodes have been visited
     * @param finishArray is the array that contains which order each node is processed
     * @return true if the graph has a cycle and false otherwise
     */
    private boolean directedCycle(int startNode, ArrayList<Integer> markArray, ArrayList<Integer> finishArray){
        markArray.add(startNode);
        ArrayList<Integer> adjNodes = adjacencyList.get(startNode);
        for(int i = 0; i < adjNodes.size(); i++){
            if(!markArray.contains(adjNodes.get(i))){
                if(directedCycle(adjNodes.get(i),markArray,finishArray)){
                    return true;
                }
            }
            else if (markArray.contains(adjNodes.get(i)) && !finishArray.contains(adjNodes.get(i))){
                return true;
            }
        }
        finishArray.add(startNode);
        return false;
    }

    /**
     * Return true if a directed graph is strongly connected or an undirected graph is
     * is connected, and false otherwise. Note that you must
     * handle directed and undirected graphs differently.
     *
     * @return true if the graph is connected and false otherwise
     */
    @Override
    public boolean connected() {
        ArrayList<Integer> markArray = new ArrayList<>();
        for(int i = 0; i < adjacencyList.size(); i++){
            markArray.add(-1);
        }
        if (undirected()) {
            return (undirectedConnected(0, markArray));
        } else {
            return (directedConnected());
        }
    }


    /**
     * Method for dealing with if a undirected graph is connected
     * @param startNode is the first node in the array
     * @param markArray is an array that determines what node has been seen before
     * @return true if the graph is connected and false otherwise
     */
    private boolean undirectedConnected(int startNode, ArrayList<Integer> markArray){
        markArray.set(startNode,1);
        ArrayList<Integer> adjNodes = adjacencyList.get(startNode);
        if(!markArray.contains(-1)){
            return true;
        }
        for(int i = 0; i < adjNodes.size(); i++){
            if(markArray.get(adjNodes.get(i)) == -1){
                markArray.set(startNode,1);
                return(undirectedConnected(adjNodes.get(i),markArray));
            }
        }
        return false;
    }

    /**
     * Method foe determining if a directed graph is connected
     * @returntrue if the graph is connected and false otherwise
     */
    private boolean directedConnected(){
        for(int i = 0; i < adjacencyList.size(); i++){
            for(int j = 0; j < adjacencyList.size(); j++){
                if(!pathExists(i,j) && i != j){
                    return false;
                }
            }
        }
        return true;
    }
}
