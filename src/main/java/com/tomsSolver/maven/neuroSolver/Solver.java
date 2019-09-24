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
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.ConnectionFactory;

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
								//state.set_posofMovedBlockInPrestate(states.get(i).bigblock);
								//state.set_movDirectionForMovedBlockInPrestate("up");
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
		NeuralNetwork nn = new NeuralNetwork();
		int inputSize = 20;
		int outputSize = 24;
		DataSet ds = new DataSet(inputSize, outputSize);
			
		Layer inputLayer = new Layer();
		inputLayer.addNeuron(new Neuron());		//position(0,0) on the board
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());		//position(3,4) on the board
		
		Layer outputLayer = new Layer();
		outputLayer.addNeuron(new Neuron());	//position(0,0) on the board
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());
		outputLayer.addNeuron(new Neuron());	
		outputLayer.addNeuron(new Neuron());	//position(3,4) on the board
		outputLayer.addNeuron(new Neuron());	//move up
		outputLayer.addNeuron(new Neuron());	//move down
		outputLayer.addNeuron(new Neuron());	//move left
		outputLayer.addNeuron(new Neuron());	//move right

		Layer hiddenLayer1 = new Layer();
		hiddenLayer1.addNeuron(new Neuron());
		hiddenLayer1.addNeuron(new Neuron());
		hiddenLayer1.addNeuron(new Neuron());
		hiddenLayer1.addNeuron(new Neuron());
		hiddenLayer1.addNeuron(new Neuron());
		hiddenLayer1.addNeuron(new Neuron());
		
		Layer hiddenLayer2 = new Layer();
		hiddenLayer2.addNeuron(new Neuron());
		hiddenLayer2.addNeuron(new Neuron());
		hiddenLayer2.addNeuron(new Neuron());
		hiddenLayer2.addNeuron(new Neuron());
		hiddenLayer2.addNeuron(new Neuron());
		hiddenLayer2.addNeuron(new Neuron());
		hiddenLayer2.addNeuron(new Neuron());
		hiddenLayer2.addNeuron(new Neuron());	
		
		nn.addLayer(0, inputLayer);
		nn.addLayer(1,hiddenLayer1);
		ConnectionFactory.fullConnect(nn.getLayerAt(0), nn.getLayerAt(1));
		nn.addLayer(2,hiddenLayer2);
		ConnectionFactory.fullConnect(nn.getLayerAt(1), nn.getLayerAt(2));
		nn.addLayer(3, outputLayer);
		ConnectionFactory.fullConnect(nn.getLayerAt(2), nn.getLayerAt(3),true);
		nn.setInputNeurons(inputLayer.getNeurons());
		nn.setOutputNeurons(outputLayer.getNeurons());
	

		//long startTime = System.currentTimeMillis();
	//train function starts
		int flag = 2;    // 1 means solved, 0 means not solved
		ArrayList<State> states = new ArrayList<State>();
		Set<ArrayList> mapSet = new HashSet<ArrayList>();
		State temp = new State();
		State state0 = new State();
		
		state0.initialBlocks(new int[]{1,4,4,1,0,1,2,0,0,3,0,0,2,3,2,0,4,1,3,2,3,3,4,}); //the classic klotski
		//state0.initialBlocks(inputArgs());		//user inputs his own Klotski puzzle
		state0.setMap();
		states.add(state0);
		mapSet.add(state0.mapToArrayList());
		
		//System.out.println("start solving with bruteforce strategy...");
		for(int i=0; i<500000; i++) {
			flag = 1;
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
								state.set_posofMovedBlockofPrestate(states.get(i).bigblock);
								state.set_movDirectionForMovedBlockOfPrestate("up");
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
								state.set_posofMovedBlockofPrestate(states.get(i).bigblock);
								state.set_movDirectionForMovedBlockOfPrestate("down");
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
								state.set_posofMovedBlockofPrestate(states.get(i).bigblock);
								state.set_movDirectionForMovedBlockOfPrestate("left");
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
								state.set_posofMovedBlockofPrestate(states.get(i).bigblock);
								state.set_movDirectionForMovedBlockOfPrestate("right");
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
									state.set_posofMovedBlockofPrestate(states.get(i).lyblock[id]);
									state.set_movDirectionForMovedBlockOfPrestate("up");
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
									state.set_posofMovedBlockofPrestate(states.get(i).lyblock[id]);
									state.set_movDirectionForMovedBlockOfPrestate("down");
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
						state.set_posofMovedBlockofPrestate(states.get(i).lyblock[id]);
						state.set_movDirectionForMovedBlockOfPrestate("right");
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
						state.set_posofMovedBlockofPrestate(states.get(i).lyblock[id]);
						state.set_movDirectionForMovedBlockOfPrestate("left");
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
									state.set_posofMovedBlockofPrestate(states.get(i).stndblock[id]);
									state.set_movDirectionForMovedBlockOfPrestate("left");
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
									state.set_posofMovedBlockofPrestate(states.get(i).stndblock[id]);
									state.set_movDirectionForMovedBlockOfPrestate("right");
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
						state.set_posofMovedBlockofPrestate(states.get(i).stndblock[id]);
						state.set_movDirectionForMovedBlockOfPrestate("up");
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
						state.set_posofMovedBlockofPrestate(states.get(i).stndblock[id]);
						state.set_movDirectionForMovedBlockOfPrestate("down");
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
						state.set_posofMovedBlockofPrestate(states.get(i).smlblock[id]);
						state.set_movDirectionForMovedBlockOfPrestate("up");
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
						state.set_posofMovedBlockofPrestate(states.get(i).smlblock[id]);
						state.set_movDirectionForMovedBlockOfPrestate("down");
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
						state.set_posofMovedBlockofPrestate(states.get(i).smlblock[id]);
						state.set_movDirectionForMovedBlockOfPrestate("right");
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
						state.set_posofMovedBlockofPrestate(states.get(i).smlblock[id]);
						state.set_movDirectionForMovedBlockOfPrestate("left");
						states.add(state);
						mapSet.add(state.mapToArrayList());
					}
				}
			}//smallblock move posibility
			//check if bigblock reaches the escape point, yes->show and break, not->continue
			/*for(int m=i ;m<states.size() ;m++) {
				if(states.get(m).bigblock.positionX==1 && states.get(m).bigblock.positionY==3) {
					System.out.println("brute done, train dataset");
					int index = states.get(m).preStateNr;
					int step = 0;
					while(index!=0) {
						ArrayList<Double> desiredOutput = (ArrayList) Arrays.stream(states.get(m).get_preMovBlockPositionForNN()).boxed().collect(Collectors.toList());
						desiredOutput.addAll((ArrayList) Arrays.stream(states.get(m).get_preMovDirectionForNN()).boxed().collect(Collectors.toList()));
						ArrayList<Double> input = states.get(index).mapToNeuroInput();
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
			}*/
		}
		ArrayList<Double> input = new ArrayList<Double>(Arrays.asList(3.0, 1.0, 1.0, 3.0, 3.0, 1.0, 1.0, 3.0, 3.0, 2.0, 2.0, 3.0, 3.0, 4.0, 4.0, 3.0, 0.0, 4.0, 0.0, 4.0 ));
		ArrayList<Double> desiredoutput = new ArrayList<Double>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0));
		DataSetRow dsr = new DataSetRow(input, desiredoutput);
		ds.add(dsr);
		BackPropagation backPropagation = new BackPropagation();
		backPropagation.setMaxIterations(1);
		System.out.println("start learning");
		nn.learn(ds, backPropagation);
		//long endTime = System.currentTimeMillis();
		//System.out.println("searching took " + (endTime - startTime) + " milliseconds");
		System.out.println("train finish. plz input your klotski puzzle");
		
		State newGame = new State();
		double[] newGameInput = new double[20];
		newGame.initialBlocks(new int[]{3,1,1,3,3,1,1,3,3,2,2,3,3,4,4,3,0,4,0,4});
		for(int i=0; i<20; i++) {
			newGameInput[i] = (double) newGame.mapToNeuroInput().get(i);
		}
		for(double value : newGameInput) {
			
		}
		nn.setInput(newGameInput);
		nn.calculate();
		double[] networkOutput = nn.getOutput();
		for(double value : networkOutput) {
			System.out.print((int)value + ", ");
		}
}

	public static void main(String[] args){
		Solver solver = new Solver();
		//solver.bruteforceStrate();
		solver.AIStrate();
	}
}
