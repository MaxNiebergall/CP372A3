import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

public class LinkState {
	static int n;

	public static void main(String args[]) {
		Scanner sc = new Scanner(new InputStreamReader(System.in));
		n = sc.nextInt();

		// [rows][columns]
		Integer[][] adjMatrix = new Integer[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				adjMatrix[i][j] = sc.nextInt();
			}
		}

		String gwrInput = sc.nextLine();
		String[] gwrVals = gwrInput.split(" ");
		Integer[] gatewayRouters = new Integer[gwrVals.length];
		for (int i = 0; i < gwrVals.length; i++) {
			gatewayRouters[i] = Integer.parseInt(gwrVals[i]);
		}

	}

	public static void dijkstra(Integer[][] adjMatrix, int source) {
			Integer[] dist = new Integer[n];
			dist[source] = 0;
			Integer[] prev = new Integer[n];
			
			class QCompare implements Comparator<Integer[]> {

				@Override
				public int compare(Integer o1[], Integer o2[]) {
					//Double check this
					return o1[1] - o2[1];
				}
				
			}
			 
			PriorityQueue<Integer[]> Q = new PriorityQueue<Integer[]>(n, QCompare);
			
			
			for(int vert =0; vert<n; vert++) {
				if(vert != source) {
					dist[vert] = Integer.MAX_VALUE;
				}
				prev[vert] = null;
						Q.add(vert, dist[vert]);
				
			}
			
			          
			7          if v ≠ source
			8              dist[v] ← INFINITY                 // Unknown distance from source to v
			9          prev[v] ← UNDEFINED                    // Predecessor of v
			10
			
			11         Q.add_with_priority(v, dist[v])
			12
			13
			14     while Q is not empty:                      // The main loop
			15         u ← Q.extract_min()                    // Remove and return best vertex
			16         for each neighbor v of u:              // only v that are still in Q
			17             alt ← dist[u] + length(u, v) 
			18             if alt < dist[v]
			19                 dist[v] ← alt
			20                 prev[v] ← u
			21                 Q.decrease_priority(v, alt)
			22
			23     return dist, prev
	}

	private class ForwardingTable {

		int from;
		ArrayList<Integer> to;
		ArrayList<Integer> cost;
		ArrayList<Integer> hops;
		ArrayList<ArrayList<Integer>> nextHop;

		ForwardingTable() {
			to = new ArrayList<Integer>();
			cost = new ArrayList<Integer>();
			hops = new ArrayList<Integer>();
			nextHop = new ArrayList<ArrayList<Integer>>();

		}

		ForwardingTable(List toO, List costO, List nextHopO, int from) {
			this.from = from;
			to = new ArrayList<Integer>(toO);
			cost = new ArrayList<Integer>(costO);
			nextHop = new ArrayList<ArrayList<Integer>>(nextHopO);
		}

		public void printTable() {

			System.out.println("Forwarding Table for " + from);
			System.out.println("\tTo\tCost\tNext\tHop");

			for (int i = 0; i < to.size(); i++) {
				System.out.print("\t" + to.get(i) + "\t" + cost.get(i));
				for (int j = 0; j < nextHop.get(i).size(); j++) {
					System.out.print("\t" + nextHop.get(i).get(j));
				}
				System.out.println();
			}

		}

	}

}
