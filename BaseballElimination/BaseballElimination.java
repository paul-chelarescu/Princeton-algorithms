import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class BaseballElimination {
    private Team[] teams;
    private int numberOfTeams;
    private FordFulkerson fordFulkerson;
    private FlowNetwork flowNetwork;
    // Remembers if last elimination was trivial or mathematical
    private boolean mathematicalElimination;
    // The last team clearly outperforming another one in trivial elimination
    private Set<String> trivialEliminationEnemy;
    
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        try {
            Scanner in = new Scanner(new FileReader(filename));
            numberOfTeams = in.nextInt();
            teams = new Team[numberOfTeams];
            
            for (int teamNumber = 0; teamNumber < numberOfTeams; teamNumber++) {
                teams[teamNumber] = new Team();
                teams[teamNumber].name = in.next();
                teams[teamNumber].wins = in.nextInt();
                teams[teamNumber].loses = in.nextInt();
                teams[teamNumber].remaining = in.nextInt();
                teams[teamNumber].against = new int[numberOfTeams];
                for (int j = 0; j < numberOfTeams; j++) {
                    teams[teamNumber].against[j] = in.nextInt();
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Wrong filename");
        }
    }
    
    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }
    
    // all teams
    public Iterable<String> teams() {
        ArrayList<String> teamNames = new ArrayList<>();
        for (int i = 0; i < numberOfTeams; i++) {
            teamNames.add(teams[i].name);
        }
        return teamNames;
    }
    
    // number of wins for given team
    public int wins(String team) {
        return teams[getIndexOfTeam(team)].wins;
    }
    
    // number of losses for given team
    public int losses(String team) {
        return teams[getIndexOfTeam(team)].loses;
    }
    
    // number of remaining games for given team
    public int remaining(String team) {
        return teams[getIndexOfTeam(team)].remaining;
    }
    
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return teams[getIndexOfTeam(team1)].against[getIndexOfTeam(team2)];
    }
    
    // is given team eliminated?
    public boolean isEliminated(String team) {
        // Create flow network
        int xTeam = getIndexOfTeam(team);
        flowNetwork = new FlowNetwork(1 + nCr(numberOfTeams - 1, 2) + numberOfTeams - 1 + 1);
        
        int i_j = 1;
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == xTeam) continue;
            for (int j = i; j < numberOfTeams; j++) {
                if (j == xTeam || j == i) continue;
                flowNetwork.addEdge(new FlowEdge(0, i_j, against(teams[i].name, teams[j].name)));
                flowNetwork.addEdge(new FlowEdge(i_j, teamIndexToTeamVerticeIndex(i, xTeam), Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(i_j, teamIndexToTeamVerticeIndex(j, xTeam), Double.POSITIVE_INFINITY));
                i_j++;
            }
            
            // Trivial elimination
            if (wins(team) + remaining(team) - wins(teams[i].name) < 0) {
                // System.out.println("Trivial elimination: " + team);
                mathematicalElimination = false;
                trivialEliminationEnemy = new HashSet<>();
                trivialEliminationEnemy.add(teams[i].name);
                return true;
            }
            
            flowNetwork.addEdge(new FlowEdge(teamIndexToTeamVerticeIndex(i, xTeam),
            flowNetwork.V() - 1, wins(team) + remaining(team) - wins(teams[i].name)));
        }
        
        fordFulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V() - 1);
        
        // Mathematical elimination
        for (FlowEdge gameEdge : flowNetwork.adj(0)) {
            if (gameEdge.capacity() - gameEdge.flow() != 0) {
                // System.out.println("Mathematical elimination: " + team);
                mathematicalElimination = true;
                return true;
            }
        }
        
        return false;
    }
    
    private int teamIndexToTeamVerticeIndex(int teamIndex, int xTeam) {
        if (teamIndex < xTeam) {
            return 1 + nCr(numberOfTeams - 1, 2) + teamIndex;
        } else {
            return nCr(numberOfTeams - 1, 2) + teamIndex;
        }
    }
    
    private int teamVerticeIndexToTeamIndex(int teamVerticeIndex, int xTeam) {
        if (teamVerticeIndex - (nCr(numberOfTeams - 1, 2) + 1) < xTeam) {
            return teamVerticeIndex - (nCr(numberOfTeams - 1, 2) + 1);
        } else {
            return teamVerticeIndex - (nCr(numberOfTeams - 1, 2) + 1) + 1;
        }
    }
    
    // Taken from https://rosettacode.org/wiki/Evaluate_binomial_coefficients#Java
    private static int nCr(int n, int k) {
        if (k>n-k)
            k=n-k;
    
        long b=1;
        for (int i=1, m=n; i<=k; i++, m--)
            b=b*m/i;
        return (int) b;
    }
    
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!isEliminated(team)) return null;
        if (!mathematicalElimination) return trivialEliminationEnemy;
        
        Set<Integer> eliminatingTeams = new HashSet<>();
        for (int i = nCr(numberOfTeams - 1, 2) + 1; i < flowNetwork.V() - 1; i++) {
            if (fordFulkerson.inCut(i)) {
                eliminatingTeams.add(i);
            }
        }
        Set<String> eliminatingTeamNames = new HashSet<>();
        for (int teamVerticeIndex : eliminatingTeams) {
            eliminatingTeamNames.add(teams[teamVerticeIndexToTeamIndex(teamVerticeIndex, getIndexOfTeam(team))].name);
        }
        
        return eliminatingTeamNames;
    }
    
    private int getIndexOfTeam(String team) {
        for (int i = 0; i < numberOfTeams; i++) {
            if (teams[i].name.equals(team)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Team name does not exist");
    }
    
    public class Team {
        public String name;
        public int wins;
        public int loses;
        public int remaining;
        public int[] against;
    }
}
