import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

public class LinkState {
	static int n;
	int from;



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



		for(int i=0; i<n; i++){
			ArrayList<Integer> to;
			ArrayList<Integer> cost;
			ArrayList<Integer> hops;
			ArrayList<ArrayList<Integer>> nextHop;

			to = new ArrayList<Integer>();
			cost = new ArrayList<Integer>();
			hops = new ArrayList<Integer>();
			nextHop = new ArrayList<ArrayList<Integer>>();

			ArrayList<Integer[]> shortestPaths = dijkstra(adjMatrix, i);
			ArrayList<Integer[]> tempShortestPaths;
			for(int rID =0; rID<gatewayRouters.length; rID++){
				to.add(gatewayRouters[rID]);
				cost.add(shortestPaths.get(0)[gatewayRouters[rID]]);

				do {
					Integer[][] tempAdjMatrx = adjMatrix.clone();
					Integer prev = gatewayRouters[rID];
					//While previous of previous is not the start, ie we are at the first hop
					while (shortestPaths.get(1)[prev] != i) {
						prev = shortestPaths.get(1)[prev];
					}
					if(hops.size()==0){
						hops.add(prev);
					}
					tempAdjMatrx[i][prev] = -1;

					tempShortestPaths = dijkstra(tempAdjMatrx, i);
					if(tempShortestPaths.get(0)[i] == cost.get(cost.size()-1)){
						while (shortestPaths.get(1)[prev] != i) {
							prev = shortestPaths.get(1)[prev];
						}
						hops.add(prev);
					}
				}while(tempShortestPaths.get(0)[i] == cost.get(cost.size()-1));

				nextHop.add(hops);
			}

			printTable(i, to, cost, nextHop);



		}



	}

	public static ArrayList<Integer[]> dijkstra(Integer[][] adjMatrix, int source) {
			Integer[] dist = new Integer[n];
			dist[source] = 0;
			Integer[] prev = new Integer[n];

			class QCompare implements Comparator<Integer[]> {
				@Override
				public int compare(Integer o1[], Integer o2[]) {
					//Double check this
					return o1[1] - o2[1];
				}
				public boolean equals(Integer o1[], Integer o2[]){
					return o1[0] == o2[0];
				}
			}

			PriorityQueue<Integer[]> Q = new PriorityQueue<Integer[]>(n, new QCompare());

			for(int vert =0; vert<n; vert++) {
				if(vert != source) {
					dist[vert] = Integer.MAX_VALUE;
				}
				prev[vert] = null;
						Q.add(new Integer[]{vert, dist[vert]});
			}

			while (!Q.isEmpty()){
				Integer u = Q.poll()[0];

				for (int v=0; v<n; v++){
					if(adjMatrix[u][v] >0) {
						Integer alt = dist[u] + adjMatrix[u][v];
						if(alt <dist[v]){
							Q.remove(new Integer[]{v, dist[v]});
							dist[v] = alt;
							prev[v] = u;
							Q.add(new Integer[]{v, dist[v]});
						}
					}
				}
			}

		ArrayList<Integer[]> returnable = new ArrayList<Integer[]>();
			returnable.add(dist);
			returnable.add(prev);

			return returnable;

	}


		static public void printTable(Integer from, List to, List cost, ArrayList<ArrayList<Integer>> nextHop) {

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
