import java.util.LinkedList;

public class Node {
	//Attributes
		char[][] state;
		private int cost;
		private int numMarbles;
		private LinkedList<Node> neighbours;
		Node parent;
		
		//Constructors
		public Node(char[][] state, int marbles) {
			this.state = state;
			this.numMarbles = marbles;
			this.neighbours = new LinkedList<Node>();
			this.parent = null;
		}
		
		public Node(char[][] state, int cost, int marbles) {
			this.state = state;
			this.numMarbles = marbles;
			this.cost = cost;
			this.neighbours = new LinkedList<Node>();
			this.parent = null;
		}
		
		//Getters/Setters
		public int getNumMarbles() {
			return this.numMarbles;
		}
		public void setNumMarbles(int marbles) {
			this.numMarbles = marbles;
		}
		public int getCost() {
			return this.cost;
		}
		public void setCost(int cost) {
			this.cost = cost;
		}
		public LinkedList<Node> getNeighbours(){
			return this.neighbours;
		}
		public void addNeighbour(Node newNeighbour) {
			this.neighbours.add(newNeighbour);
		}
		public Node getParent() {
			return this.parent;
		}
		public void setParent(Node parent) {
			this.parent = parent;
		}
		
		//Functions
		public void PrintState() {
			for (int i = 0; i<7; i++){
		        for (int j = 0; j<7; j++){
		            System.out.print(state[i][j]);
		        }
		        System.out.println("");
		    }
		}
		
		public void makeEqualTo(char[][] target) {
			for(int i = 0; i < 7; i++) {
				for(int j = 0; j < 7; j++) {
					target[i][j] = this.state[i][j];
				}
			}
		}
		
		public boolean IsGoal() {
			if(this.numMarbles == 1) {
				return true;
			}
			/*
			int marbleCount = 0;
			for (int i = 0; i<7; i++){
		        for (int j = 0; j<7; j++){
		            if(state[i][j] == 'o') {
		            	marbleCount += 1;
		            }
		        }
		    }
			if ((marbleCount == 1)&&(state[1][3] == 'o')) {
				return true;
			}
			*/
			return false;
		}
}
