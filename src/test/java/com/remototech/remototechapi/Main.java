package com.remototech.remototechapi;

public class Main {
	
	public static void main(String[] args) {
		int[][] trustMaster = new int[10][2];
		trustMaster[1][1] = 5;
 		Main.findJudge(2, trustMaster);
	}

	public static int findJudge(int n, int[][] trust) {
		
		int[][] trustMaster = new int[n][2];
		
		for (int i = 1; i <= n; i++) {
			System.out.println(trust[i][i]);
		}
		int ans = 0;
		return ans;
	}
}
