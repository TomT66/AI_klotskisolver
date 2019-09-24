package com.tomsSolver.maven.neuroSolver;

public class LongStandingBlock extends Block {
	public String leftrightDetector(int[][] map) {
		String result = new String();
		if(positionX!=0 && map[positionX-1][positionY] == 0 && map[positionX-1][positionY+1] == 0) {
			result = "left";
		}
		else if(positionX!=3 && map[positionX+1][positionY] == 0 && map[positionX+1][positionY+1] == 0) {
			result = "right";
		}
		return result;
	}
	public String upDetector(int[][] map) {
		String result = new String();
		if(positionY!=0 && map[positionX][positionY-1] == 0) {
			result = "up";
		}
		return result;
	}
	public String downDetector(int[][] map) {
		String result = new String();
		if(positionY!=3 && map[positionX][positionY+2] == 0) {
			result = "down";
		}
		return result;
	}
}
