package com.tomsSolver.maven.neuroSolver;

public class Block {
	String name = new String();
	int positionX;
	int positionY;
	
	public void moveUp() {
		positionY = positionY - 1;
//		System.out.println(name + "go up");
	}
	public void moveDown() {
		positionY = positionY + 1;
//		System.out.println(name + "go down");
	}
	public void moveRight() {
		positionX = positionX + 1;
//		System.out.println(name + "go right");
	}
	public void moveLeft() {
		positionX = positionX - 1;
//		System.out.println(name + "go left");
	}
}

