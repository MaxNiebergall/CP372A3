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
		//TODO return {dist, prev};
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
