//Kelvin Choi
//COMP4106 A1
import java.util.*;

public class PegSolitaire {
	
	public static boolean isEqualTo(char[][] one, char[][] two) {
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				if(one[i][j] != two[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	//-----HEURISTICS-------
	//Heuristic function #2
	//returns the number of isolated marbles on the board state
	public static int h2(Node current) {
		int isolatedNodes = 0;
		
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				//if isolated on the top side
				if((i-1 < 0)&&(i+1 <= 6)&&(j-1 >= 0)&&(j+1 <= 6)&&(current.state[i][j]=='o')){
					if((current.state[i+1][j] != 'o')&&(current.state[i][j-1] != 'o')&&
					(current.state[i][j+1] != 'o')&&(current.state[i+1][j+1] != 'o')&&(current.state[i+1][j-1] != 'o')){
						isolatedNodes += 1;
					}
				}
				//if isolated on the bottom side
				else if((i-1 >= 0)&&(i+1 > 6)&&(j-1 >= 0)&&(j+1 <= 6)&&(current.state[i][j]=='o')){
					if((current.state[i-1][j] != 'o')&&(current.state[i][j-1] != 'o')&&
					(current.state[i][j+1] != 'o')&&(current.state[i-1][j+1] != 'o')&&(current.state[i-1][j-1] != 'o')){
						isolatedNodes += 1;
					}
				}
				//if isolated on the left side
				else if((i-1 >= 0)&&(i+1 <= 6)&&(j-1 < 0)&&(j+1 <= 6)&&(current.state[i][j]=='o')){
					if((current.state[i-1][j] != 'o')&&(current.state[i+1][j] != 'o')&&
					(current.state[i][j+1] != 'o')&&(current.state[i+1][j+1] != 'o')&&(current.state[i-1][j+1] != 'o')){
						isolatedNodes += 1;
					}
				}
				//if isolated on the left side
				else if((i-1 >= 0)&&(i+1 <= 6)&&(j-1 >= 0)&&(j+1 > 6)&&(current.state[i][j]=='o')){
					if((current.state[i-1][j] != 'o')&&(current.state[i+1][j] != 'o')&&
					(current.state[i][j-1] != 'o')&&(current.state[i-1][j-1] != 'o')&&(current.state[i+1][j-1] != 'o')){
						isolatedNodes += 1;
					}
				}
				//if isolated in the middle area
				else if((i-1 >= 0)&&(i+1 <= 6)&&(j-1 >= 0)&&(j+1 <= 6)&&(current.state[i][j]=='o')){
					if((current.state[i-1][j] != 'o')&&(current.state[i+1][j] != 'o')&&
					(current.state[i][j-1] != 'o')&&(current.state[i][j+1] != 'o')&&(current.state[i-1][j-1] != 'o')
					&&(current.state[i+1][j+1] != 'o')&&(current.state[i-1][j+1] != 'o')&&(current.state[i+1][j-1] != 'o')){
						isolatedNodes += 1;
					}
				}
			}
		}//end outer for
		
		return isolatedNodes;
	}
	
	//Heuristic function #1
	//returns the number of nodes created from a board state
	public static int h1(Node current) {
		int nodesCreated = 0;
		
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				//Check if a marble can move up
				if(i-2 >= 0) {
					if((current.state[i][j] == 'o')&&(current.state[i-1][j] == 'o')&&(current.state[i-2][j] == ' ')) {
						nodesCreated += 1;
					}
				}
				//Check if a marble can move down
				if(i+2 <= 6) {
					if((current.state[i][j] == 'o')&&(current.state[i+1][j] == 'o')&&(current.state[i+2][j] == ' ')) {
						nodesCreated += 1;
					}
				}
				//Check if a marble can move left
				if(j-2 >= 0) {
					if((current.state[i][j] == 'o')&&(current.state[i][j-1] == 'o')&&(current.state[i][j-2] == ' ')) {
						nodesCreated += 1;
					}
				}
				//Check if a marble can move right
				if(j+2 <= 6) {
					if((current.state[i][j] == 'o')&&(current.state[i][j+1] == 'o')&&(current.state[i][j+2] == ' ')) {
						nodesCreated += 1;
					}
				}
			}
		}//end outer for
		
		return -nodesCreated;
	}
	
	//------SEARCHES--------
	//A* function #1 using h1
	public static void AS1(PriorityQueue<Node> open) {
		HashSet<Node> closed = new HashSet<Node>();
		
		while(!open.isEmpty()) {
			Node current = open.poll();
			closed.add(current);
			
			if(current.IsGoal()) {
				System.out.println("GOAL FOUND");
				current.PrintState();
				System.out.println("----------");
				Node x = current.getParent();
				while(x != null) {
					x.PrintState();
					System.out.println(-x.getCost());
					System.out.println("----------");
					x = x.getParent();
				}
				break;
			}
			
			//generate all neighbors
			for(int i = 0; i < 7; i++) {
				for(int j = 0; j < 7; j++) {
					//Check if a marble can move up
					if(i-2 >= 0) {
						if((current.state[i][j] == 'o')&&(current.state[i-1][j] == 'o')&&(current.state[i-2][j] == ' ')) {
							char[][] tempB = new char[7][7];
							current.makeEqualTo(tempB);
							tempB[i][j] = ' ';
							tempB[i-1][j] = ' ';
							tempB[i-2][j] = 'o';
							
							Node tempN = new Node(tempB, 0, current.getNumMarbles() - 1);
							
							current.addNeighbour(tempN);
						}
					}
					//Check if a marble can move down
					if(i+2 <= 6) {
						if((current.state[i][j] == 'o')&&(current.state[i+1][j] == 'o')&&(current.state[i+2][j] == ' ')) {
							char[][] tempB = new char[7][7];
							current.makeEqualTo(tempB);
							tempB[i][j] = ' ';
							tempB[i+1][j] = ' ';
							tempB[i+2][j] = 'o';
							Node tempN = new Node(tempB, 0, current.getNumMarbles() - 1);
							
							current.addNeighbour(tempN);
						}
					}
					//Check if a marble can move left
					if(j-2 >= 0) {
						if((current.state[i][j] == 'o')&&(current.state[i][j-1] == 'o')&&(current.state[i][j-2] == ' ')) {
							char[][] tempB = new char[7][7];
							current.makeEqualTo(tempB);
							tempB[i][j] = ' ';
							tempB[i][j-1] = ' ';
							tempB[i][j-2] = 'o';
							
							Node tempN = new Node(tempB, 0, current.getNumMarbles() - 1);
							
							current.addNeighbour(tempN);
						}
					}
					//Check if a marble can move right
					if(j+2 <= 6) {
						if((current.state[i][j] == 'o')&&(current.state[i][j+1] == 'o')&&(current.state[i][j+2] == ' ')) {
							char[][] tempB = new char[7][7];
							current.makeEqualTo(tempB);
							tempB[i][j] = ' ';
							tempB[i][j+1] = ' ';
							tempB[i][j+2] = 'o';
							
							Node tempN = new Node(tempB, 0, current.getNumMarbles() - 1);
							
							current.addNeighbour(tempN);
						}
					}
				}
			}//end outer for
			
			//for each neighbor of current
			for(Node n : current.getNeighbours()) {
				boolean inClosed = false;
				boolean inOpen = false;
				
				//check if neighbor is in open
				for(Node n2 : open) {
					if(isEqualTo(n2.state, n.state)) {
						inOpen = true;
					}
				}
				//check if neighbor is in closed
				for(Node n2 : closed) {
					if(isEqualTo(n2.state, n.state)) {
						inClosed = true;
					}
				}
				//if it was not in closed
				if(!inClosed) {
					//previous path
					int prevCost = n.getCost();
					//new path to neighbor
					
					n.setCost(h1(n));
					
					if((n.getCost() < prevCost)||(!inOpen)) {
						//f_cost was set above
						n.setParent(current);
						if(!inOpen) {
							open.add(n);
						}
					}
					
				}
			}//end neighbor iterating for loop
			
		}//end while
	}
	
	//A* function #2 using h2
	public static void AS2(PriorityQueue<Node> open) {
		HashSet<Node> closed = new HashSet<Node>();
		
		while(!open.isEmpty()) {
			Node current = open.poll();
			closed.add(current);
			
			if(current.IsGoal()) {
				System.out.println("GOAL FOUND");
				current.PrintState();
				System.out.println("----------");
				Node x = current.getParent();
				while(x != null) {
					x.PrintState();
					System.out.println(x.getCost());
					System.out.println("----------");
					x = x.getParent();
				}
				break;
			}
			
			//generate all neighbors
			for(int i = 0; i < 7; i++) {
				for(int j = 0; j < 7; j++) {
					//Check if a marble can move up
					if(i-2 >= 0) {
						if((current.state[i][j] == 'o')&&(current.state[i-1][j] == 'o')&&(current.state[i-2][j] == ' ')) {
							char[][] tempB = new char[7][7];
							current.makeEqualTo(tempB);
							tempB[i][j] = ' ';
							tempB[i-1][j] = ' ';
							tempB[i-2][j] = 'o';
							
							Node tempN = new Node(tempB, 100, current.getNumMarbles() - 1);
							
							current.addNeighbour(tempN);
						}
					}
					//Check if a marble can move down
					if(i+2 <= 6) {
						if((current.state[i][j] == 'o')&&(current.state[i+1][j] == 'o')&&(current.state[i+2][j] == ' ')) {
							char[][] tempB = new char[7][7];
							current.makeEqualTo(tempB);
							tempB[i][j] = ' ';
							tempB[i+1][j] = ' ';
							tempB[i+2][j] = 'o';
							Node tempN = new Node(tempB, 100, current.getNumMarbles() - 1);
							
							current.addNeighbour(tempN);
						}
					}
					//Check if a marble can move left
					if(j-2 >= 0) {
						if((current.state[i][j] == 'o')&&(current.state[i][j-1] == 'o')&&(current.state[i][j-2] == ' ')) {
							char[][] tempB = new char[7][7];
							current.makeEqualTo(tempB);
							tempB[i][j] = ' ';
							tempB[i][j-1] = ' ';
							tempB[i][j-2] = 'o';
							
							Node tempN = new Node(tempB, 100, current.getNumMarbles() - 1);
							
							current.addNeighbour(tempN);
						}
					}
					//Check if a marble can move right
					if(j+2 <= 6) {
						if((current.state[i][j] == 'o')&&(current.state[i][j+1] == 'o')&&(current.state[i][j+2] == ' ')) {
							char[][] tempB = new char[7][7];
							current.makeEqualTo(tempB);
							tempB[i][j] = ' ';
							tempB[i][j+1] = ' ';
							tempB[i][j+2] = 'o';
							
							Node tempN = new Node(tempB, 100, current.getNumMarbles() - 1);
							
							current.addNeighbour(tempN);
						}
					}
				}
			}//end outer for
			
			//for each neighbor of current
			for(Node n : current.getNeighbours()) {
				boolean inClosed = false;
				boolean inOpen = false;
				
				//check if neighbor is in open
				for(Node n2 : open) {
					if(isEqualTo(n2.state, n.state)) {
						inOpen = true;
					}
				}
				//check if neighbor is in closed
				for(Node n2 : closed) {
					if(isEqualTo(n2.state, n.state)) {
						inClosed = true;
					}
				}
				//if it was not in closed
				if(!inClosed) {
					int prevCost = n.getCost();//previous path
					n.setCost(h2(n));//new path to neighbor
					
					if((n.getCost() < prevCost)||(!inOpen)) {
						//f_cost was set above
						n.setParent(current);
						if(!inOpen) {
							open.add(n);
						}
					}
					
				}
			}//end neighbor iterating for loop
			
		}//end while
	}
	
	//A* function #3 using both h1 and h2
	public static void AS3(PriorityQueue<Node> open) {
		HashSet<Node> closed = new HashSet<Node>();
		
		while(!open.isEmpty()) {
			Node current = open.poll();
			closed.add(current);
			
			if(current.IsGoal()) {
				System.out.println("GOAL FOUND");
				current.PrintState();
				System.out.println("----------");
				Node x = current.getParent();
				while(x != null) {
					x.PrintState();
					System.out.println(-x.getCost());
					System.out.println("----------");
					x = x.getParent();
				}
				break;
			}
			
			//generate all neighbors
			for(int i = 0; i < 7; i++) {
				for(int j = 0; j < 7; j++) {
					//Check if a marble can move up
					if(i-2 >= 0) {
						if((current.state[i][j] == 'o')&&(current.state[i-1][j] == 'o')&&(current.state[i-2][j] == ' ')) {
							char[][] tempB = new char[7][7];
							current.makeEqualTo(tempB);
							tempB[i][j] = ' ';
							tempB[i-1][j] = ' ';
							tempB[i-2][j] = 'o';
							
							Node tempN = new Node(tempB, 100, current.getNumMarbles() - 1);
							
							current.addNeighbour(tempN);
						}
					}
					//Check if a marble can move down
					if(i+2 <= 6) {
						if((current.state[i][j] == 'o')&&(current.state[i+1][j] == 'o')&&(current.state[i+2][j] == ' ')) {
							char[][] tempB = new char[7][7];
							current.makeEqualTo(tempB);
							tempB[i][j] = ' ';
							tempB[i+1][j] = ' ';
							tempB[i+2][j] = 'o';
							Node tempN = new Node(tempB, 100, current.getNumMarbles() - 1);
							
							current.addNeighbour(tempN);
						}
					}
					//Check if a marble can move left
					if(j-2 >= 0) {
						if((current.state[i][j] == 'o')&&(current.state[i][j-1] == 'o')&&(current.state[i][j-2] == ' ')) {
							char[][] tempB = new char[7][7];
							current.makeEqualTo(tempB);
							tempB[i][j] = ' ';
							tempB[i][j-1] = ' ';
							tempB[i][j-2] = 'o';
							
							Node tempN = new Node(tempB, 100, current.getNumMarbles() - 1);
							
							current.addNeighbour(tempN);
						}
					}
					//Check if a marble can move right
					if(j+2 <= 6) {
						if((current.state[i][j] == 'o')&&(current.state[i][j+1] == 'o')&&(current.state[i][j+2] == ' ')) {
							char[][] tempB = new char[7][7];
							current.makeEqualTo(tempB);
							tempB[i][j] = ' ';
							tempB[i][j+1] = ' ';
							tempB[i][j+2] = 'o';
							
							Node tempN = new Node(tempB, 100, current.getNumMarbles() - 1);
							
							current.addNeighbour(tempN);
						}
					}
				}
			}//end outer for
			
			//for each neighbor of current
			for(Node n : current.getNeighbours()) {
				boolean inClosed = false;
				boolean inOpen = false;
				
				//check if neighbor is in open
				for(Node n2 : open) {
					if(isEqualTo(n2.state, n.state)) {
						inOpen = true;
					}
				}
				//check if neighbor is in closed
				for(Node n2 : closed) {
					if(isEqualTo(n2.state, n.state)) {
						inClosed = true;
					}
				}
				//if it was not in closed
				if(!inClosed) {
					int prevCost = n.getCost();//previous path
					n.setCost(((h1(n)+h2(n))/2));//new path to neighbor
					
					if((n.getCost() < prevCost)||(!inOpen)) {
						//f_cost was set above
						n.setParent(current);
						if(!inOpen) {
							open.add(n);
						}
					}
					
				}
			}//end neighbor iterating for loop
			
		}//end while
	}
	
	//DFS function
	public static void DFS(Stack<Node> DFSstack){
		HashSet<char[][]> visited = new HashSet<char[][]>();
		
		while(!DFSstack.empty()) {
			
			boolean stateVisited = false;
			
			for(char[][] boards : visited) {
				if(isEqualTo(boards, DFSstack.peek().state)) {
					stateVisited = true;
				}
			}
			if(!stateVisited) {	
				visited.add(DFSstack.peek().state);
				
				Node current = DFSstack.peek();
				DFSstack.pop();
				
				if(current.IsGoal()) {
					System.out.println("GOAL FOUND");
					current.PrintState();
					System.out.println("----------");
					Node x = current.getParent();
					while(x != null) {
						x.PrintState();
						System.out.println("----------");
						x = x.getParent();
					}
					break;
				}
				
				//generate every possible move for current board
				for(int i = 0; i < 7; i++) {
					for(int j = 0; j < 7; j++) {
						//Check if a marble can move up
						if(i-2 >= 0) {
							if((current.state[i][j] == 'o')&&(current.state[i-1][j] == 'o')&&(current.state[i-2][j] == ' ')) {
								char[][] tempB = new char[7][7];
								current.makeEqualTo(tempB);
								tempB[i][j] = ' ';
								tempB[i-1][j] = ' ';
								tempB[i-2][j] = 'o';
								Node tempN = new Node(tempB, current.getNumMarbles() - 1);
								current.addNeighbour(tempN);
							}
						}
						//Check if a marble can move down
						if(i+2 <= 6) {
							if((current.state[i][j] == 'o')&&(current.state[i+1][j] == 'o')&&(current.state[i+2][j] == ' ')) {
								char[][] tempB = new char[7][7];
								current.makeEqualTo(tempB);
								tempB[i][j] = ' ';
								tempB[i+1][j] = ' ';
								tempB[i+2][j] = 'o';
								Node tempN = new Node(tempB, current.getNumMarbles() - 1);
								current.addNeighbour(tempN);
							}
						}
						//Check if a marble can move left
						if(j-2 >= 0) {
							if((current.state[i][j] == 'o')&&(current.state[i][j-1] == 'o')&&(current.state[i][j-2] == ' ')) {
								char[][] tempB = new char[7][7];
								current.makeEqualTo(tempB);
								tempB[i][j] = ' ';
								tempB[i][j-1] = ' ';
								tempB[i][j-2] = 'o';
								Node tempN = new Node(tempB, current.getNumMarbles() - 1);
								current.addNeighbour(tempN);
							}
						}
						//Check if a marble can move right
						if(j+2 <= 6) {
							if((current.state[i][j] == 'o')&&(current.state[i][j+1] == 'o')&&(current.state[i][j+2] == ' ')) {
								char[][] tempB = new char[7][7];
								current.makeEqualTo(tempB);
								tempB[i][j] = ' ';
								tempB[i][j+1] = ' ';
								tempB[i][j+2] = 'o';
								Node tempN = new Node(tempB, current.getNumMarbles() - 1);
								current.addNeighbour(tempN);
							}
						}
					}
				}//end for
				//assign new nodes parent to be current
				for(Node n : current.getNeighbours()) {
					n.setParent(current);
					DFSstack.push(n);
				}
			}
			else {
				DFSstack.pop();
			}
		}//end while
	}//end DFS function
	
	//BFS function
	public static void BFS(LinkedList<Node> BFSqueue){
		HashSet<char[][]> visited = new HashSet<char[][]>();
		while(BFSqueue.size() != 0) {
			
			boolean stateVisited = false;
			
			for(char[][] boards : visited) {
				if(isEqualTo(boards, BFSqueue.peek().state)) {
					stateVisited = true;
				}
			}
			if(!stateVisited) {
				visited.add(BFSqueue.peek().state);
				Node current = BFSqueue.poll();
				
				if(current.IsGoal()) {
					System.out.println("GOAL FOUND");
					current.PrintState();
					System.out.println("----------");
					Node x = current.getParent();
					while(x != null) {
						x.PrintState();
						System.out.println("----------");
						x = x.getParent();
					}
					break;
				}
				
				for(int i = 0; i < 7; i++) {
					for(int j = 0; j < 7; j++) {
						//Check if a marble can move up
						if(i-2 >= 0) {
							if((current.state[i][j] == 'o')&&(current.state[i-1][j] == 'o')&&(current.state[i-2][j] == ' ')) {
								char[][] tempB = new char[7][7];
								current.makeEqualTo(tempB);
								tempB[i][j] = ' ';
								tempB[i-1][j] = ' ';
								tempB[i-2][j] = 'o';
								Node tempN = new Node(tempB, current.getNumMarbles() - 1);
								current.addNeighbour(tempN);
							}
						}	
						//Check if a marble can move down
						if(i+2 <= 6) {
							if((current.state[i][j] == 'o')&&(current.state[i+1][j] == 'o')&&(current.state[i+2][j] == ' ')) {
								char[][] tempB = new char[7][7];
								current.makeEqualTo(tempB);
								tempB[i][j] = ' ';
								tempB[i+1][j] = ' ';
								tempB[i+2][j] = 'o';
								Node tempN = new Node(tempB, current.getNumMarbles() - 1);
								current.addNeighbour(tempN);
							}
						}
						//Check if a marble can move left
						if(j-2 >= 0) {
							if((current.state[i][j] == 'o')&&(current.state[i][j-1] == 'o')&&(current.state[i][j-2] == ' ')) {
								char[][] tempB = new char[7][7];
								current.makeEqualTo(tempB);
								tempB[i][j] = ' ';
								tempB[i][j-1] = ' ';
								tempB[i][j-2] = 'o';
								Node tempN = new Node(tempB, current.getNumMarbles() - 1);
								current.addNeighbour(tempN);
							}
						}
						//Check if a marble can move right
						if(j+2 <= 6) {
							if((current.state[i][j] == 'o')&&(current.state[i][j+1] == 'o')&&(current.state[i][j+2] == ' ')) {
								char[][] tempB = new char[7][7];
								current.makeEqualTo(tempB);
								tempB[i][j] = ' ';
								tempB[i][j+1] = ' ';
								tempB[i][j+2] = 'o';
								Node tempN = new Node(tempB, current.getNumMarbles() - 1);
								current.addNeighbour(tempN);
							}
						}
					}
				}//end for
				//assign new nodes parent to be current
				for(Node n : current.getNeighbours()) {
					n.setParent(current);
					BFSqueue.add(n);
				}
			}//end if
			else {
				BFSqueue.remove();
			}
		}//end while
	}//end BFS function
	
	
	public static void main(String[] args) {
		
		char[][] board = new char[][] {
			{'.','.',' ',' ',' ','.','.'},//0x
			{'.',' ',' ',' ',' ','o','.'},//1|
			{'o','o',' ',' ','o',' ',' '},//2|
			{'o','o','o',' ','o',' ',' '},//3v
		    {'o',' ',' ','o','o',' ',' '},//4
		    {'.',' ',' ',' ','o','o','.'},//5
		    {'.','.',' ',' ','o','.','.'},//6
		};//14 marbles
		
		Scanner reader = new Scanner(System.in);
		System.out.println("DFS (0), BFS (1), A* using h1 (2), A* using h2 (3), A* using h1 and h2 (4): ");
		int decision = reader.nextInt();
		System.out.println(decision);;
		reader.close();
		
		//if you want to run DFS
		if(decision == 0) {
			Stack<Node> DFSstack = new Stack<>();
			Node root = new Node(board, 14);
			//Node root = new Node(board2, 18);
			DFSstack.push(root);
			DFS(DFSstack);
		}
		//else if you want to run BFS
		else if(decision == 1) {
			LinkedList<Node> BFSqueue = new LinkedList<>();
			Node root = new Node(board, 14);
			BFSqueue.add(root);
			BFS(BFSqueue);
		}
		//else if you want to run A* using h1
		//h1: the number of new states it creates (pretty bad heuristic)
		else if(decision == 2) {
			PriorityQueue<Node> open = new PriorityQueue<Node>(10, new Comparator<Node>() {
				@Override
				public int compare(Node t, Node t1) {
					return Double.compare(t.getCost(), t1.getCost());
				}
			});
			Node root = new Node(board, 0, 14);
			open.add(root);
			AS1(open);
		}
		//else if you want to run A* using h2
		//h2: the number isolated marbles on the board state
		else if(decision == 3) {
			PriorityQueue<Node> open = new PriorityQueue<Node>(10, new Comparator<Node>() {
				@Override
				public int compare(Node t, Node t1) {
					return Double.compare(t.getCost(), t1.getCost());
				}
			});
			Node root = new Node(board, 100, 14);
			open.add(root);
			AS2(open);
		}
		//else if you want to run A* using h1 + h2
		else if(decision == 4) {
			PriorityQueue<Node> open = new PriorityQueue<Node>(10, new Comparator<Node>() {
				@Override
				public int compare(Node t, Node t1) {
					return Double.compare(t.getCost(), t1.getCost());
				}
			});
			Node root = new Node(board, 100, 14);
			open.add(root);
			AS3(open);
		}
	}

}
