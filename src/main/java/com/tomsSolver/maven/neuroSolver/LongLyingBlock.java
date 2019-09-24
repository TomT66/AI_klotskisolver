package com.tomsSolver.maven.neuroSolver;

public class LongLyingBlock extends Block {
	public String upddownDetector(int[][] map) {
		String result = new String();
		if(positionY!=0 && map[positionX][positionY-1] == 0 && map[positionX+1][positionY-1] == 0) {
			result = "up";
		}
		else if(positionY!=4 && map[positionX][positionY+1] == 0 && map[positionX+1][positionY+1] == 0) {
			result = "down";
		}
		return result;
	}
	public String rightDetector(int[][] map) {
		String result = new String();
		if(positionX!=2 && map[positionX+2][positionY] == 0) {
			result = "right";
		}
		return result;
	}
	public String leftDetector(int[][] map) {
		String result = new String();
		if(positionX!=0 && map[positionX-1][positionY] == 0) {
			result = "left";
		}
		return result;
	}
}
