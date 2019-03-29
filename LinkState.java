import java.util.*;
import java.io.InputStreamReader;

public class LinkState {
    static int n;
    int from;
    final static int INFINITY =Integer.MAX_VALUE;

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
        sc.nextLine();
        String gwrInput = sc.nextLine();
        String[] gwrVals = gwrInput.split(" ");
        Integer[] gatewayRouterNumbers = new Integer[gwrVals.length];
        for (int i = 0; i < gwrVals.length; i++) {
            gatewayRouterNumbers[i] = Integer.parseInt(gwrVals[i]) - 1;
        }


        for (int sourceRouterNum = 0; sourceRouterNum < n; sourceRouterNum++) {
            boolean isGateway = false;
            for (int k = 0; k < gatewayRouterNumbers.length; k++) {
                if (sourceRouterNum == gatewayRouterNumbers[k]) {
                    isGateway = true;
                    System.out.println();
                    break;
                }
            }
            if (!isGateway) {
                ArrayList<Integer> to;
                ArrayList<Integer> cost;
                ArrayList<Integer> hops;
                ArrayList<ArrayList<Integer>> nextHop;

                to = new ArrayList<Integer>();
                cost = new ArrayList<Integer>();
                nextHop = new ArrayList<ArrayList<Integer>>();

                ArrayList<Integer[]> shortestPaths = dijkstra(adjMatrix, sourceRouterNum);


                //loop through gatewayRouterID (rID) to check shortest paths to each gateway router
                for (int routerIndex = 0; routerIndex < gatewayRouterNumbers.length; routerIndex++) {

                    Integer gwRouterNumber = gatewayRouterNumbers[routerIndex];

                    Integer[] dist = shortestPaths.get(0);
                    Integer[] prev = shortestPaths.get(1);


                    //skip if there is no path
                    if (dist[gwRouterNumber] != INFINITY) {//TODO  problem must be happening here

                        //Empty hops, start new row
                        hops = new ArrayList<Integer>();
                        to.add(gwRouterNumber);
                        cost.add(dist[gwRouterNumber]);

                        Integer[][] tempAdjMatrx = new Integer[n][n];
                        for(int p=0; p<n; p++){
                            for(int l=0; l<n; l++) {
                                tempAdjMatrx[p][l] = new Integer(adjMatrix[p][l].intValue());
                            }
                        }

                        Integer routerNumber = new Integer(gwRouterNumber.intValue());
                        //While previous of previous is not the start, ie we are at the first hop
                        while (prev[routerNumber] != null && prev[routerNumber] != sourceRouterNum) {
                            routerNumber = prev[routerNumber];
                        }

                        hops.add(routerNumber);
                        tempAdjMatrx[sourceRouterNum][routerNumber] = -1;


                        routerNumber = new Integer(gwRouterNumber.intValue());
                        ArrayList<Integer[]> tempshPt = dijkstra(tempAdjMatrx, sourceRouterNum);
                        Integer[] tempDist = tempshPt.get(0);
                        Integer[] tempPrev = tempshPt.get(1);

                        while (tempDist[routerNumber] == cost.get(cost.size() - 1)){
                            while (tempPrev[routerNumber] != null && tempPrev[routerNumber] != sourceRouterNum) {
                                routerNumber = tempPrev[routerNumber];
                            }
                            hops.add(routerNumber);

                            tempAdjMatrx[sourceRouterNum][routerNumber] = -1;
                            tempshPt = dijkstra(tempAdjMatrx, sourceRouterNum);
                            routerNumber = new Integer(gwRouterNumber.intValue());

                            tempDist = tempshPt.get(0);
                            tempPrev = tempshPt.get(1);
                        }
                    } else {
                        hops = new ArrayList<Integer>();
                        to.add(gatewayRouterNumbers[routerIndex]);
                        cost.add(-1);
                        hops.add(-2);//-2 instead of -1 beacuse it adds 1 at print time
                    }

                    nextHop.add(hops);

                }


                printTable(sourceRouterNum, to, cost, nextHop);

            }

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

            public boolean equals(Integer o1[], Integer o2[]) {
                return o1[0] == o2[0];
            }
        }

        PriorityQueue<Integer[]> Q = new PriorityQueue<Integer[]>(n, new QCompare());

        for (int vert = 0; vert < n; vert++) {
            if (vert != source) {
                dist[vert] = INFINITY;
            }
            prev[vert] = null;
            Q.add(new Integer[]{vert, dist[vert]});
        }

        while (!Q.isEmpty()) {
            Integer u = Q.poll()[0];

            for (int v = 0; v < n; v++) {
                if (adjMatrix[u][v] > 0) {
                    Integer alt;
                    if (dist[u] != INFINITY) {
                        alt = dist[u] + adjMatrix[u][v];
                    } else {
                        alt = INFINITY;
                    }
                    if (alt < dist[v]) {
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


    static public void printTable(Integer from, List<Integer> to, List<Integer> cost, ArrayList<ArrayList<Integer>> nextHop) {

        System.out.println("Forwarding Table for " + (1 + from));
        System.out.println("To\tCost\tNext Hop");

        for (int i = 0; i < to.size(); i++) {
            System.out.print("" + (to.get(i) + 1) + "\t\t" + cost.get(i));
            for (int j = 0; j < nextHop.get(i).size(); j++) {
                System.out.print("\t" + (nextHop.get(i).get(j) + 1));
            }
            System.out.println();
        }
        System.out.println();
    }


}
