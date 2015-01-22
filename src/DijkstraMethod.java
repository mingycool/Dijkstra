import java.util.ArrayList;

public class DijkstraMethod {

	public static int[] dijkstraMST(Graph g) {
	    int source=g.getSource();
	    int number=g.getNumber();
	    ArrayList<ArrayList<Integer>> graph=g.getGraph();
		boolean[] determined = new boolean[number];// store nodes that has been found the shortest path
		int[] distance = new int[number];//store distance from nodes to source
		for (int i = 0; i < number; i++) {
			determined[i] = false;
			distance[i] = Integer.MAX_VALUE;
		}
		distance[source] = 0;	

		//main part of dijkstra DijkstraMethod
		for (int i = 0; i < number; i++) {
			//find the node with minimal distance but not determined so far
			int min = 0;
			while(determined[min]){//loop the cycle
				min++;
			}
			for (int j = min; j < number; j++) {
				if (determined[j]==false && distance[j]<distance[min]) {
					min = j;
				}
			}
			determined[min] = true; //determined the min as labeled

			//in each time, find the new shorter distance and update it
			
			for (int j = 0; j < graph.get(min).size()/2; j++) {
				int dest = graph.get(min).get(2*j);
				int cost = graph.get(min).get(2*j+1);
				if(determined[dest]==false && distance[dest] > distance[min] + cost){
					distance[dest] = distance[min] + cost;
				}
			}

		}
		return distance;
	}


	public static int[] dijkstraFib(Graph g) {
	    int x=g.getSource();
	    int n=g.getNumber();
	    ArrayList<ArrayList<Integer>> graph=g.getGraph();
		FibonacciHeap pq = new FibonacciHeap(); //establish a new Fibonacci Heap
		FibonacciHeap.Node[] distance = new FibonacciHeap.Node[n]; //distance is stored in the array of entry
		int[] result = new int[n];
		boolean[] determined = new boolean[n];

		//initialize it for the first time and make all the distance to max_value
		for(int i = 0; i < n; i++){
			distance[i] = pq.insert(i, Integer.MAX_VALUE); 
			//notice that enqueue will return a reference to the entry we inserted
		}
		pq.decreaseKey(distance[x], 0); //decrease the distance to source node to 0
		determined[x] = true; 

		while (!pq.isEmpty()) {

			FibonacciHeap.Node curr = pq.deleteMin();
			int min = curr.getValue();	//get value return the number of the node
			int cost = curr.getPriority();	//get priority return the distance to that node
			determined[min] = true;	
			result[min] = cost;

			//for the node that adjacent to the [min] node, if it shortest path was not determined
			//try to update it distance if need
			for (int i = 0; i < graph.get(min).size()/2; i++) {
				int adj = graph.get(min).get(2*i);
				int arc = graph.get(min).get(2*i+1);

				int dist = distance[adj].getPriority(); //dist is the current shortest path to the node adjacent to [min]
				if (!determined[adj] && dist > cost + arc) {
					pq.decreaseKey(distance[adj], cost + arc);
				}
			}		
		}

		return result;
	}
}
