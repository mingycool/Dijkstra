import java.io.*;

public class Method {

	public static void MST(String fileName) {// This is usual MST mehod for dijkstra, use adjacent list representation of graph, use find minimal edges in array
		Graph g=new Graph(fileName);
		System.out.println("The Graph is as below");
		g.printGraph();
		int[] distance = DijkstraMethod.dijkstraMST(g);
		    //call dijkstra to count the distance of source to each node, distance is stored in an array
		String outputfilename = "MstMethodOutput.txt";// outputfile name
	        try{
                PrintWriter outputfile=new PrintWriter (outputfilename);  // print result
                outputfile.println(" \nThe dijikstra path generated by Mst Method is as below:");
		
                for (int i=0;i<g.getNumber();i++)
                {   
                   outputfile.println(distance[i]+"//cost from node "+g.getSource()+" to "+i+" ");
                }// print out edge of the dijkstra path
                outputfile.close();
                System.out.println("\nThe shortest path of Mst method has been displayed in MstMethodOutput.txt, please refer it to check the result");	      
            }
            catch (IOException e) {  // catch exception
                e.printStackTrace();
            }

	}

	//dijkstra using fibonacci heap
	public static void Fib(String fileName) {

		Graph g=new Graph(fileName);
		System.out.println("The Graph is as below");
        g.printGraph();
		int[] distance = DijkstraMethod.dijkstraFib(g);// call fib dijkstar method to generate the distance array
		String outputfilename = "FibMethodOutput.txt";// output file name
	        try{
                PrintWriter outputfile=new PrintWriter (outputfilename);  // print result
                outputfile.println(" \nThe dijikstra path generated by Fib Method is as below:");
                for (int i=0;i<g.getNumber();i++)
                {   
                   outputfile.println(distance[i]+"//cost from node "+g.getSource()+" to "+i+" ");
                }// print out edge of the dijikstra path
                outputfile.close();
		System.out.println("\nThe shortest path of Fib method has been displayed in FibMethodOutput.txt, please refer it to check the result");	      }
            catch (IOException e) {  // catch exception
                e.printStackTrace();
            }
	}
	public static void randomInput(int n, double d, int x) {// randomly create a graph
	
		long start = System.currentTimeMillis();
		Graph g=new Graph(n,d,x);
		long end = System.currentTimeMillis();
		System.out.println("The Graph is as below");
        g.printGraph();
		System.out.println("\nThe Time of creating a graph is: "+(end-start));

		long starttime1 = System.currentTimeMillis();
		int[] Mdistance = DijkstraMethod.dijkstraMST(g);
		long endtime1 = System.currentTimeMillis();
   		
		String outputfilename = "MSTMethodOutput.txt";
	        try{
                PrintWriter outputfile=new PrintWriter (outputfilename);  // print result
                outputfile.println(" \nThe dijikstra path generated by Mst Method is as below:");
		
                for (int i=0;i<n;i++)
                {   
                    outputfile.println(Mdistance[i]+"//cost from node "+x+" to "+i+" ");
                }// print out edge of the dijkstra path
                outputfile.close();
                System.out.println("\nThe shortest path of Mst method has been displayed in MSTMethodOutput.txt, please refer it to check the result");	      
                }
            catch (IOException e) {  // catch exception
                e.printStackTrace();
            }
		long starttime2 = System.currentTimeMillis();
		int[] Fdistance = DijkstraMethod.dijkstraFib(g);
		long endtime2 = System.currentTimeMillis();
		outputfilename = "FibMethodOutput.txt";
	        try{
                PrintWriter outputfile=new PrintWriter (outputfilename);  // print result
                outputfile.println(" \nThe dijikstra path generated by Fib Method is as below:");
		
                for (int i=0;i<n;i++)
                {   
                   outputfile.println(Fdistance[i]+"//cost from node "+x+" to "+i+" ");
                }// print out edge of the dijkstra path
                outputfile.close();
                System.out.println("\nThe shortest path of Fib method has been displayed in FIbMethodOutput.txt, please refer it to check the result");	      
                }
            catch (IOException e) {  // catch exception
                e.printStackTrace();
            }

		System.out.println("\nThe time of MST method is : " + (endtime1-starttime1));
		System.out.println("\nThe time of Fib method is: " + (endtime2-starttime2));
}
}