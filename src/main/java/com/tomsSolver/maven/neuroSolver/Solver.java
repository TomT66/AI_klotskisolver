package com.tomsSolver.maven.neuroSolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.TransferFunctionType;

public class Solver {

	public int[] inputArgs() {
		Scanner in = new Scanner(System.in);
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		System.out.print("plz input num of lyblocks: ");
		int numoflyblocks = in.nextInt();
		result.add(numoflyblocks);
		System.out.print("plz input num of stnblocks: ");
		int numofstnblocks = in.nextInt();
		result.add(numofstnblocks);
		System.out.print("plz input num of smlblocks: ");
		int numofsmlblocks = in.nextInt();
		result.add(numofsmlblocks);
		
		System.out.print("plz input Xcoordinate of bigblock: ");
		result.add(in.nextInt());
		System.out.print("plz input Ycoordinate of bigblock: ");
		result.add(in.nextInt());
		
		for(int x=0; x<numoflyblocks; x++) {
			System.out.print("plz input Xcoordinate of " + (x+1) + ". lyblock: ");
			result.add(in.nextInt());
			System.out.print("plz input Ycoordinate of " + (x+1) + ". lyblocks: ");
			result.add(in.nextInt());
		}
		for(int x=0; x<numofstnblocks; x++) {
			System.out.print("plz input Xcoordinate of " + (x+1) + ". stnblock: ");
			result.add(in.nextInt());
			System.out.print("plz input Ycoordinate of " + (x+1) + ". stnblocks: ");
			result.add(in.nextInt());
		}
		for(int x=0; x<numofsmlblocks; x++) {
			System.out.print("plz input Xcoordinate of " + (x+1) + ". smlblock: ");
			result.add(in.nextInt());
			System.out.print("plz input Ycoordinate of " + (x+1) + ". smlblocks: ");
			result.add(in.nextInt());
		}
		in.close();
		return result.stream().mapToInt(i -> i).toArray();
	}
	
	public void bruteforceStrate(){
		int flag = 2;    // 1 means solved, 0 means not solved
		ArrayList<State> states = new ArrayList<State>();
		@SuppressWarnings("rawtypes")
		Set<ArrayList> mapSet = new HashSet<ArrayList>();
		State temp = new State();
		State state0 = new State();
		
		//state0.initialBlocks(new int[]{2,4,2,0,0,0,2,2,2,0,3,1,3,2,3,3,3,3,0,3,1,}); //a unsolvable klotski puzzle
		state0.initialBlocks(new int[]{1,4,4,1,0,1,2,0,0,3,0,0,2,3,2,0,4,1,3,2,3,3,4,}); //the classic klotski

		//state0.initialBlocks(inputArgs());		//user inputs his own Klotski puzzle
		long startTime = System.currentTimeMillis();
		state0.setMap();
		states.add(state0);
		mapSet.add(state0.mapToArrayList());
		
		System.out.println("start solving with bruteforce strategy...");
		for(int i=0; i<500000; i++) {
			if(i == states.size()) {
				state0.showStateMap();
				System.out.println("the puzzle is unsolvabe.");
				break;
			}
			if(flag == 1) break;
			//bigblock move posibility, detector tells if it is able to go up/down/left/right,  
			//a new state is stored in "states" if same map hasn't been searched
			switch(states.get(i).bigblock.moveDetector(states.get(i).map)) {
			case "up":		states.get(i).assign(temp);
							temp.bigblock.moveUp();
							temp.setMap();
							if(!mapSet.contains(temp.mapToArrayList())) {
								State state = new State();
								temp.assign(state);
								state.preStateNr = i;
								states.add(state);
								mapSet.add(state.mapToArrayList());
							}
							break;
			case "down":	states.get(i).assign(temp);
							temp.bigblock.moveDown();
							temp.setMap();
							if(!mapSet.contains(temp.mapToArrayList())) {
								State state = new State();
								temp.assign(state);
								state.preStateNr = i;
								states.add(state);
								mapSet.add(state.mapToArrayList());
							}
							break;
			case "left":	states.get(i).assign(temp);
							temp.bigblock.moveLeft();
							temp.setMap();
							if(!mapSet.contains(temp.mapToArrayList())) {
								State state = new State();
								temp.assign(state);
								state.preStateNr = i;
								states.add(state);
								mapSet.add(state.mapToArrayList());
							}
							break;
			case "right":	states.get(i).assign(temp);
							temp.bigblock.moveRight();
							temp.setMap();
							if(!mapSet.contains(temp.mapToArrayList())) {
								State state = new State();
								temp.assign(state);
								state.preStateNr = i;
								states.add(state);
								mapSet.add(state.mapToArrayList());
							}break;
			}
			//bigblock move posibility
			//longlyingblocks move posibility
			for(int id=0; id<states.get(i).numOfLyblock; id++) {
				switch(states.get(i).lyblock[id].upddownDetector(states.get(i).map)) {
				case "up":		states.get(i).assign(temp);
								temp.lyblock[id].moveUp();
								temp.setMap();
								if(!mapSet.contains(temp.mapToArrayList())) {
									State state = new State();
									temp.assign(state);
									state.preStateNr = i;
									states.add(state);
									mapSet.add(state.mapToArrayList());
								}
								break;
				case "down":	states.get(i).assign(temp);
								temp.lyblock[id].moveDown();
								temp.setMap();
								if(!mapSet.contains(temp.mapToArrayList())) {
									State state = new State();
									temp.assign(state);
									state.preStateNr = i;
									states.add(state);
									mapSet.add(state.mapToArrayList());
								}
								break;	
				}
				if(states.get(i).lyblock[id].rightDetector(states.get(i).map) == "right") {
					states.get(i).assign(temp);
					temp.lyblock[id].moveRight();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
				if(states.get(i).lyblock[id].leftDetector(states.get(i).map) == "left") {
					states.get(i).assign(temp);
					temp.lyblock[id].moveLeft();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
			}
			//longlyingblocks move posibility
			//longstandingblocks move posibility
			for(int id=0; id<states.get(i).numOfStndblock; id++) {
				switch(states.get(i).stndblock[id].leftrightDetector(states.get(i).map)) {
				case "left":	states.get(i).assign(temp);
								temp.stndblock[id].moveLeft();
								temp.setMap();
								if(!mapSet.contains(temp.mapToArrayList())) {
									State state = new State();
									temp.assign(state);
									state.preStateNr = i;
									states.add(state);
									mapSet.add(state.mapToArrayList());
								}
								break;
				case "right":	states.get(i).assign(temp);
								temp.stndblock[id].moveRight();
								temp.setMap();
								if(!mapSet.contains(temp.mapToArrayList())) {
									State state = new State();
									temp.assign(state);
									state.preStateNr = i;
									states.add(state);
									mapSet.add(state.mapToArrayList());
								}
								break;
				}
				if(states.get(i).stndblock[id].upDetector(states.get(i).map) == "up") {
					states.get(i).assign(temp);
					temp.stndblock[id].moveUp();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
				if(states.get(i).stndblock[id].downDetector(states.get(i).map) == "down") {
					states.get(i).assign(temp);
					temp.stndblock[id].moveDown();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
			}//longstandingblocks move posibility
			//smallblock move posibility
			for(int id=0; id<states.get(i).numOfSmlblock; id++) {
				if(states.get(i).smlblock[id].upDetector(states.get(i).map) == "up") {
					states.get(i).assign(temp);
					temp.smlblock[id].moveUp();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
				if(states.get(i).smlblock[id].downDetector(states.get(i).map) == "down") {
					states.get(i).assign(temp);
					temp.smlblock[id].moveDown();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
				if(states.get(i).smlblock[id].rightDetector(states.get(i).map) == "right") {
					states.get(i).assign(temp);
					temp.smlblock[id].moveRight();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
				if(states.get(i).smlblock[id].leftDetector(states.get(i).map) == "left") {
					states.get(i).assign(temp);
					temp.smlblock[id].moveLeft();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
			}//smallblock move posibility
			//check if bigblock reaches the escape point, yes->show and break, not->continue
			for(int m=i ;m<states.size() ;m++) {
				if(states.get(m).bigblock.positionX==1 && states.get(m).bigblock.positionY==3) {
					int step = 0;
					int index = states.get(m).preStateNr;
					states.get(m).showStateMap();
					System.out.println(" ");
					while(index!=0) {
						step++;
						System.out.println("upward��");
						states.get(index).showStateMap();
						System.out.println(" ");
						index = states.get(index).preStateNr;
					}
					System.out.println("upward��");
					states.get(0).showStateMap();
					flag = 1;
					System.out.println("shortest solution needs "+ ++step +" steps");
					System.out.println(i + " states searched!");
					break;
				}	
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("searching took " + (endTime - startTime) + " milliseconds");
	}// end of bruteforceStrat
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void AIStrate() {
		int inputSize = 20;
		int outputSize = 24;
		DataSet ds = new DataSet(inputSize, outputSize);
		
		MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 20, 15, 30, 24);
		
        
		//long startTime = System.currentTimeMillis();
		//train function starts
		int flag = 2;    // 1 means solved, 0 means not solved
		ArrayList<State> states = new ArrayList<State>();
		Set<ArrayList> mapSet = new HashSet<ArrayList>();
		State temp = new State();
		State state0 = new State();
		
		state0.initialBlocks(new int[]{1,4,4,1,0,1,2,0,0,3,0,0,2,3,2,0,4,1,3,2,3,3,4,}); //the classic klotski
		state0.setMap();
		states.add(state0);
		mapSet.add(state0.mapToArrayList());
		
		//System.out.println("start solving with bruteforce strategy...");
		for(int i=0; i<500000; i++) {
			if(flag == 1) break;
			//bigblock move posibility, detector tells if it is able to go up/down/left/right,  
			//a new state is stored in "states" if same map hasn't been searched
			switch(states.get(i).bigblock.moveDetector(states.get(i).map)) {
			case "up":		states.get(i).assign(temp);
							temp.bigblock.moveUp();
							temp.setMap();
							if(!mapSet.contains(temp.mapToArrayList())) {
								State state = new State();
								temp.assign(state);
								state.preStateNr = i;
								state.set_preMovedBlock(states.get(i).bigblock);
								state.set_preMvDirction("up");
								states.add(state);
								mapSet.add(state.mapToArrayList());
							}
							break;
			case "down":	states.get(i).assign(temp);
							temp.bigblock.moveDown();
							temp.setMap();
							if(!mapSet.contains(temp.mapToArrayList())) {
								State state = new State();
								temp.assign(state);
								state.preStateNr = i;
								state.set_preMovedBlock(states.get(i).bigblock);
								state.set_preMvDirction("down");
								states.add(state);
								mapSet.add(state.mapToArrayList());
							}
							break;
			case "left":	states.get(i).assign(temp);
							temp.bigblock.moveLeft();
							temp.setMap();
							if(!mapSet.contains(temp.mapToArrayList())) {
								State state = new State();
								temp.assign(state);
								state.preStateNr = i;
								state.set_preMovedBlock(states.get(i).bigblock);
								state.set_preMvDirction("left");
								states.add(state);
								mapSet.add(state.mapToArrayList());
							}
							break;
			case "right":	states.get(i).assign(temp);
							temp.bigblock.moveRight();
							temp.setMap();
							if(!mapSet.contains(temp.mapToArrayList())) {
								State state = new State();
								temp.assign(state);
								state.preStateNr = i;
								state.set_preMovedBlock(states.get(i).bigblock);
								state.set_preMvDirction("right");
								states.add(state);
								mapSet.add(state.mapToArrayList());
							}break;
			}
			//bigblock move posibility
			//longlyingblocks move posibility
			for(int id=0; id<states.get(i).numOfLyblock; id++) {
				switch(states.get(i).lyblock[id].upddownDetector(states.get(i).map)) {
				case "up":		states.get(i).assign(temp);
								temp.lyblock[id].moveUp();
								temp.setMap();
								if(!mapSet.contains(temp.mapToArrayList())) {
									State state = new State();
									temp.assign(state);
									state.preStateNr = i;
									state.set_preMovedBlock(states.get(i).lyblock[id]);
									state.set_preMvDirction("up");
									states.add(state);
									mapSet.add(state.mapToArrayList());
								}
								break;
				case "down":	states.get(i).assign(temp);
								temp.lyblock[id].moveDown();
								temp.setMap();
								if(!mapSet.contains(temp.mapToArrayList())) {
									State state = new State();
									temp.assign(state);
									state.preStateNr = i;
									state.set_preMovedBlock(states.get(i).lyblock[id]);
									state.set_preMvDirction("down");
									states.add(state);
									mapSet.add(state.mapToArrayList());
								}
								break;	
				}
				if(states.get(i).lyblock[id].rightDetector(states.get(i).map) == "right") {
					states.get(i).assign(temp);
					temp.lyblock[id].moveRight();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						state.set_preMovedBlock(states.get(i).lyblock[id]);
						state.set_preMvDirction("right");
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
				if(states.get(i).lyblock[id].leftDetector(states.get(i).map) == "left") {
					states.get(i).assign(temp);
					temp.lyblock[id].moveLeft();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						state.set_preMovedBlock(states.get(i).lyblock[id]);
						state.set_preMvDirction("left");
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
			}
			//longlyingblocks move posibility
			//longstandingblocks move posibility
			for(int id=0; id<states.get(i).numOfStndblock; id++) {
				switch(states.get(i).stndblock[id].leftrightDetector(states.get(i).map)) {
				case "left":	states.get(i).assign(temp);
								temp.stndblock[id].moveLeft();
								temp.setMap();
								if(!mapSet.contains(temp.mapToArrayList())) {
									State state = new State();
									temp.assign(state);
									state.preStateNr = i;
									state.set_preMovedBlock(states.get(i).stndblock[id]);
									state.set_preMvDirction("left");
									states.add(state);
									mapSet.add(state.mapToArrayList());
								}
								break;
				case "right":	states.get(i).assign(temp);
								temp.stndblock[id].moveRight();
								temp.setMap();
								if(!mapSet.contains(temp.mapToArrayList())) {
									State state = new State();
									temp.assign(state);
									state.preStateNr = i;
									state.set_preMovedBlock(states.get(i).stndblock[id]);
									state.set_preMvDirction("right");
									states.add(state);
									mapSet.add(state.mapToArrayList());
								}
								break;
				}
				if(states.get(i).stndblock[id].upDetector(states.get(i).map) == "up") {
					states.get(i).assign(temp);
					temp.stndblock[id].moveUp();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						state.set_preMovedBlock(states.get(i).stndblock[id]);
						state.set_preMvDirction("up");
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
				if(states.get(i).stndblock[id].downDetector(states.get(i).map) == "down") {
					states.get(i).assign(temp);
					temp.stndblock[id].moveDown();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						state.set_preMovedBlock(states.get(i).stndblock[id]);
						state.set_preMvDirction("down");
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
			}//longstandingblocks move posibility
			//smallblock move posibility
			for(int id=0; id<states.get(i).numOfSmlblock; id++) {
				if(states.get(i).smlblock[id].upDetector(states.get(i).map) == "up") {
					states.get(i).assign(temp);
					temp.smlblock[id].moveUp();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						state.set_preMovedBlock(states.get(i).smlblock[id]);
						state.set_preMvDirction("up");
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
				if(states.get(i).smlblock[id].downDetector(states.get(i).map) == "down") {
					states.get(i).assign(temp);
					temp.smlblock[id].moveDown();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						state.set_preMovedBlock(states.get(i).smlblock[id]);
						state.set_preMvDirction("down");
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
				if(states.get(i).smlblock[id].rightDetector(states.get(i).map) == "right") {
					states.get(i).assign(temp);
					temp.smlblock[id].moveRight();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						state.set_preMovedBlock(states.get(i).smlblock[id]);
						state.set_preMvDirction("right");
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
				if(states.get(i).smlblock[id].leftDetector(states.get(i).map) == "left") {
					states.get(i).assign(temp);
					temp.smlblock[id].moveLeft();
					temp.setMap();
					if(!mapSet.contains(temp.mapToArrayList())) {
						State state = new State();
						temp.assign(state);
						state.preStateNr = i;
						state.set_preMovedBlock(states.get(i).smlblock[id]);
						state.set_preMvDirction("left");
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
			}//smallblock move posibility
			
			//check if bigblock reaches the escape point, yes->show and break, not->continue
			for(int m=i ;m<states.size() ;m++) {
				if(states.get(m).bigblock.positionX==1 && states.get(m).bigblock.positionY==3) {
					System.out.println("brute done, get dataset from it");
					int index = states.get(m).preStateNr;
					int step = 0;
					while(index!=0) {
						
						ArrayList<Double> desiredOutput = states.get(index).preStateNeuroInput();
						desiredOutput.addAll(states.get(index).get_preMovDirection());
						ArrayList<Double> input = states.get(states.get(index).preStateNr).mapToNeuroInput();
						DataSetRow dsr = new DataSetRow(input, desiredOutput);
						System.out.println(step++);
						for(double value : input) {
							System.out.print(value + ", ");
						}
						System.out.println();
						for(double value : desiredOutput) {
							System.out.print(value + ", ");
						}
						System.out.println();
						ds.add(dsr);
						index = states.get(index).preStateNr;
					}
					flag = 1;
					break;
				}
			}
		}
		
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.setLearningRate(0.7);
        learningRule.setMomentum(0.8);
        
        /*double[] intry = {3.0, 1.0, 1.0, 3.0, 3.0, 1.0, 1.0, 3.0, 3.0, 2.0, 2.0, 3.0, 3.0, 4.0, 4.0, 3.0, 4.0, 0.0, 0.0, 4.0}; 
        double[] outry = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
        DataSet dstry = new DataSet(20, 24);
        DataSetRow dsrtry = new DataSetRow(intry, outry);
		dstry.add(dsrtry);*/
        
		System.out.println("start learning");
		long startTime = System.currentTimeMillis();
		neuralNet.learn(ds);
		long endTime = System.currentTimeMillis();
		System.out.println("training took " + (endTime - startTime) + " milliseconds");
		System.out.println("train finish. plz input your klotski puzzle");
		
		State newGame = new State();
		double[] newGameInput = new double[20];
		newGame.initialBlocks(new int[]{3,1,1,3,3,1,1,3,3,2,2,3,3,4,4,3,0,4,0,4});
		
		neuralNet.setInput(3,1,1,3,3,1,1,3,3,2,2,3,3,4,4,3,0,4,0,4);
		neuralNet.calculate();
		double[] networkOutput = neuralNet.getOutput();
		System.out.println(" Output: " + Arrays.toString(networkOutput));
}

	public static void main(String[] args){
		Solver solver = new Solver();
		//solver.bruteforceStrate();
		solver.AIStrate();
	}
}
