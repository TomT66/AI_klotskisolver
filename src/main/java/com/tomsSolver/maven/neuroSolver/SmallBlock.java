package com.tomsSolver.maven.neuroSolver;

public class SmallBlock extends Block{
	public String upDetector(int[][] map) {
		String result = new String();
		if(positionY!=0 && map[positionX][positionY-1] == 0) {
			result = "up";
		}
		return result;
	}
	public String downDetector(int[][] map) {
		String result = new String();
		if(positionY!=4 && map[positionX][positionY+1] == 0) {
			result ="down";
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
	public String rightDetector(int[][] map) {
		String result = new String();
		if(positionX!=3 && map[positionX+1][positionY] == 0) {
			result = "right";
		}
		return result;
	}
}
