package monteCarloTreeSearch;
import java.awt.*;
import java.util.ArrayList;
import java.math.*;
import ticTacToe.*;
public class Node {
	public BoardOfBoard boardOfBoard = new BoardOfBoard();
	public double player1WinNum = 0;
	public double player2WinNum = 0;
	public double totalPlayNum = 0;
	public Node parentNode = null;
	public ArrayList<Node> childNodes = new ArrayList<Node>();
	//starting Node
	public Node() {
	}
	public Node(Node pN, BoardOfBoard b) {
		parentNode = pN;
		boardOfBoard.copy(b);
	}

	//print out properties of Node
	public void printOutNode() {
		System.out.println("Node, printOutNode, boardOfboard = ");
		boardOfBoard.printOutBoard();
		System.out.println("Node, printOutNode, player1WinNum = " + player1WinNum);
		System.out.println("Node, printOutNode, player2WinNum = " + player2WinNum);
		System.out.println("Node, printOutNode, totalPlayNum = " + totalPlayNum);
		if(childNodes instanceof ArrayList) {
			System.out.println("Node, printOutNode, childNodes.size() = "+childNodes.size());
		}
	}

	//return a color based on winrate
	public Color nodeColor() {
		//player 1 = red, player 2 = blue
		//subtract oppositions color and green by the winrate
		double player1WinRate = player1WinNum / totalPlayNum;
		double player2WinRate = player2WinNum / totalPlayNum;
		int RG = (int)Math.round(player1WinRate*510);
		int GB = (int)Math.round(player2WinRate*510);
		//if player2 has the lower winrate
		if (player1WinRate > player2WinRate) {
			return new Color(255, 0,GB);
		}
		//if player 1 has the lower winrate
		else {
			return new Color(RG, 0, 255);
		}
	}
	//copies a node
	public void copyNode(Node n) {
		boardOfBoard.copyBigG(n.boardOfBoard.bigG);
		boardOfBoard.turns = n.boardOfBoard.turns;
		//child Nodes can be shallow copied since it will lead to 
		childNodes = n.childNodes;
	}

	//return the highestWinRate Move
	public Node nextBestMove() {
		double highestWinRate = 0;
		Node selectedNode = new Node();
		for(Node n : childNodes) {
			//if the winrate is higher
			double winNum = 0;
			if(boardOfBoard.turns%2 == 1) {
				winNum = n.player2WinNum;
			}
			if(boardOfBoard.turns%2 == 0) {
				winNum = n.player1WinNum;
			}
			double winRate = (winNum/n.totalPlayNum);
			if(winRate > highestWinRate) {
				selectedNode = n;
				highestWinRate = winRate;
			}
		}
		return selectedNode;
	}

	//finds the corresponding Node that has the same board from the childNodes
	public Node findFromChildren(BoardOfBoard missingChild) {
		for(Node n : childNodes) {
			if(n.boardOfBoard.sameBoardOfBoard(missingChild)) {
				return n;
			}
		}
		return null;
	}

	//add to the Tree by given branchingNum
	public void createTree(int branchingNum) {
		Node selectedNode = this;
		for(int counter = 0 ; counter < branchingNum; counter++) {
			selectedNode = this;
			//while the Node has children
			while(selectedNode.childNodes.size() > 0) {
				//System.out.println("Node, createTree, child exists selecting next child");
				selectedNode = selectedNode.selectNode();
			}
			//if the node is already explored
			if(!(selectedNode.totalPlayNum == 0) ){
				//System.out.println("Node, createTree, the node is already explored or simulated so make children and select");
				//create childNodes
				selectedNode.makeChildNodes();
				//select a random ChildNode
				selectedNode = selectedNode.selectNode();
			}
			//randomly simulate and propagate results
			//System.out.println("randomly simulating and backpropagating results");
			selectedNode.backPropagate(selectedNode.randomSimulate());
		}
	}

	//add child Nodes(expansion?)
	public void makeChildNodes() {
		for(BoardOfBoard BB : boardOfBoard.possibleMoves()){
			childNodes.add(new Node(this, BB));
		}
	}

	//select Node based on UCB1 statistical confidence interval(selection?)
	//num of wins/ num of outcomes + root(2ln(number of times a node is selected)/number of times parent node is selected)
	public Node selectNode() {
		double HighestUCB1Score = 0;
		Node selectedNode = new Node();
		for(Node n : childNodes) {
			double winNum = 0;
			if(boardOfBoard.turns%2 == 0) {
				winNum = n.player1WinNum;
			}
			if(boardOfBoard.turns%2 == 1) {
				winNum = n.player2WinNum;
			}
			//if the UCB1 Score is higher
			double winRate = (winNum/n.totalPlayNum);
			double exploitationValue = Math.sqrt(2*Math.log(totalPlayNum)/n.totalPlayNum);
			//System.out.println("Node, selectNode(), selectedNode winrate = "+winRate);
			if((winRate+exploitationValue) >= HighestUCB1Score||n.totalPlayNum == 0) {
				selectedNode = n;
				HighestUCB1Score = winRate+exploitationValue;
			}
		}
		//System.out.println("Node, selectNode(), selectedNode printNode = ");
		//selectedNode.
		// de();
		return selectedNode;
	}

	//randomly simulate
	//returns 1 for player 1 win, 2 for player 2 win, and 3 for a tie
	public int randomSimulate(){
		return boardOfBoard.randomlySimulate();
	}

	//back propagate the simulation result
	public void backPropagate(int result) {
		totalPlayNum++;
		if(result == 1) {
			player1WinNum++;
		}
		if(result == 2) {
			player2WinNum++;
		}
		if(result == 3) {
			player1WinNum += 0.5;
			player2WinNum += 0.5;
		}
		if(parentNode instanceof Node) {
			//System.out.println("Node, backPropagate(), result = "+result);

			parentNode.backPropagate(result);
		}
	}
}
