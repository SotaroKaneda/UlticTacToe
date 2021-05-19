package ticTacToe;
import java.awt.event.MouseAdapter;
import monteCarloTreeSearch.*;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.*;

public class UlTicTacToePanel extends JPanel implements MouseListener{

	public BoardOfBoard boardOfBoard = new BoardOfBoard();
	public final Node originalNode = new Node();
	public Node currentNode = originalNode;
	public int playMode = 0;	//0 if playing 1 if analyzing game.
	public UlTicTacToePanel() {
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//create frame
		JFrame gameFrame = new JFrame();
		//create panel
		UlTicTacToePanel gamePanel = new UlTicTacToePanel();
		//set frame title
		gameFrame.setTitle("Ultimate Tic-Tac-Toe");
		//set frame to visible
		gameFrame.setVisible(true);
		//set frame size
		gameFrame.setBounds(50, 50, 600, 600);
		//restricting the user to resize\
		gameFrame.setResizable(false);
		//add panel to frame
		gameFrame.add(gamePanel);
		//add mouseListener to the frame 
		gameFrame.addMouseListener(gamePanel);
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//if the game is still going on
		if(boardOfBoard.gameState() == 0)
		{
			g.setColor(Color.white);
			//draw highlighted boards
			paintHighlightedBoards(g);
			g.setColor(Color.black);
			//draw board grid
			paintGrid(g);
			//draw pieces
			paintPieces(g);
			//paint nodes
			//paintPossibleNodes(g);
		}
		else {
			if(boardOfBoard.gameState() == 2) {
				g.drawOval(50, 50, 450, 450);
			}
			if(boardOfBoard.gameState() == 1){
				g.drawLine(50, 50, 450, 450);
				g.drawLine(50, 450, 450,50);
			}
			if(boardOfBoard.gameState() == 3) {
				g.setFont(new Font("TimesRoman", Font.PLAIN, 400));
				g.drawString("Tie!", 20,400);
			}
		}
	}
	//paints board to highlight
	public void paintHighlightedBoards(Graphics g) {
		for(int col = 0; col < 3; col ++) {
			for(int row = 0; row < 3; row ++) {
				//if the board is playable highlight it in white
				if(boardOfBoard.playableBoards.b[col][row] == 1) {
					g.fillRect(175*col+25, 175*row+25, 150, 150);
				}
			}
		}
	}
	//paints grid
	public void paintGrid(Graphics g) {
		for(int col = 1; col < 4; col ++) {
			for(int row = 1; row < 4; row ++) {
				for(int sCol = 0; sCol < 4; sCol++) {
					g.drawLine(col*175-150+sCol*50, row*175-150, col*175-150+sCol*50, row*175);
				}
				for(int sRow = 0; sRow < 4; sRow++) {
					g.drawLine(col*175-150, row*175-150+sRow*50, col*175, row*175-150+sRow*50);
				}
			}
		}
	}
	//paints pieces
	public void paintPieces(Graphics g) {
		//paint 
		for(int bigCol = 0; bigCol < 3; bigCol++) {
			for(int bigRow = 0; bigRow < 3; bigRow++) {
				for(int col = 0; col < 3; col++) {
					for(int row = 0; row < 3; row++) {
						int spaceNum = boardOfBoard.bigG[bigCol][bigRow].b[col][row];
						if(spaceNum == 2) {
							g.drawOval((bigCol+1)*175-145+col*50, (bigRow+1)*175-145+row*50, 40, 40);
						}
						if(spaceNum == 1) {
							g.drawLine((bigCol+1)*175-140+col*50, (bigRow+1)*175-140+row*50, (bigCol+1)*175-110+col*50, (bigRow+1)*175-110+row*50);
							g.drawLine((bigCol+1)*175-140+col*50, (bigRow+1)*175-110+row*50, (bigCol+1)*175-110+col*50, (bigRow+1)*175-140+row*50);
						}
					}
				}
			}
		}
	}
	//paints possible moves on the board from current node
	public void paintPossibleNodes(Graphics g){
		for(Node move :currentNode.childNodes){
			g.setColor(move.nodeColor());

			int col = currentNode.boardOfBoard.differentMove(move.boardOfBoard)[0];
			int row = currentNode.boardOfBoard.differentMove(move.boardOfBoard)[1];
			int sCol = currentNode.boardOfBoard.differentMove(move.boardOfBoard)[2];
			int sRow = currentNode.boardOfBoard.differentMove(move.boardOfBoard)[3];
			DecimalFormat f = new DecimalFormat("#.###");
			g.drawString(String.valueOf(f.format(move.player1WinNum/move.totalPlayNum)),(col+1)*175-145+sCol*50+5, (row+1)*175-145+sRow*50+25);
			g.drawOval((col+1)*175-145+sCol*50, (row+1)*175-145+sRow*50, 40, 40);
		}
	}
	
	//count all nodes in the same level
	public int[] countNodes(Node n) {
		int [] nodeSum = {0,0,0};
		//if the node has been simulated
		if((n.player1WinNum + n.player2WinNum) == 0) {
							nodeSum[1]++;
			}
		//if the node has children
		else if(n.childNodes.size() == 0) {
			nodeSum[0]++;
		}
		
		//if the node does not have children and has not been simulated
		else {
			nodeSum[2]++;
		}
		for(Node childNodes : n.childNodes) {
			nodeSum[0] += countNodes(childNodes)[0];
			nodeSum[1] += countNodes(childNodes)[1];
			nodeSum[2] += countNodes(childNodes)[2];
		}
		return nodeSum;
	}
	
	//winrate determines RGB Values for red and blue 0 for green
	//paints nodes in the tree
	public void paintNodesOnTree(Graphics g, int height, int shift, Node n){
		Node startNode = currentNode;
		g.setColor(Color.RED);
		System.out.println("paintNodesOnTree height = " + height + " shift = " + shift);
		g.drawOval(height * 20, shift * 20, 40, 40);
		if(startNode.childNodes.size() != 0 && height < 10) {
			height ++;
			for(Node nextLevelNode : startNode.childNodes) {
				paintNodesOnTree(g, height, shift, nextLevelNode);
				shift++;
			}
		}
	}
	
	//play move based on MCTS
	public void playerMCTS(int branchingNumber){
		//create or expand tree from selected node
		currentNode.createTree(branchingNumber);
		//choose the next best move
		boardOfBoard.copy(currentNode.nextBestMove().boardOfBoard);
		currentNode.nextBestMove().boardOfBoard.printOutBoard();
		currentNode = currentNode.nextBestMove();
		currentNode.createTree(branchingNumber);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//if the game is not finished
		if(boardOfBoard.gameState() == 0) {
			//if it is X's turn(turn%2+1==1) then Tree Search
			if(boardOfBoard.turns%2 == 0) {
				playerMCTS(1000);
			}
			//if it is O's turn(turn%2+1==2) then human uses mouse to play
			else {
				if(boardOfBoard.turns%2 == 1) {
					int actualMouseX = e.getX()-7;
					int actualMouseY = e.getY()-30;
					int boardNumX = actualMouseX/175;
					int boardNumY = actualMouseY/175;
					int boardX = (actualMouseX-boardNumX*175-25)/50;
					int boardY = (actualMouseY-boardNumY*175-25)/50;
					//if the selected board is playable
					if(boardOfBoard.playableBoards.b[boardNumX][boardNumY] == 1) {
						//if the space is open
						if(boardOfBoard.bigG[boardNumX][boardNumY].b[boardX][boardY]==0) {
							//add pieces to the board
							boardOfBoard.addPiece((boardOfBoard.turns%2+1), boardNumX, boardNumY, boardX, boardY);
							System.out.println("playerNum = "+(boardOfBoard.turns%2+1));
							//update turns
							boardOfBoard.turns = boardOfBoard.turns+1;
							System.out.println("turn = "+(boardOfBoard.turns+1));
							//update playableBoards
							boardOfBoard.updatePlayable(boardX, boardY);
							System.out.println("playable boards updated");
							//update currentNode
							if(currentNode.childNodes.size()>0) {
							currentNode = currentNode.findFromChildren(boardOfBoard);
							}
							currentNode.createTree(1000);
						}
						else {
							System.out.println("space is taken by number " +boardOfBoard.bigG[boardX][boardY].b[boardX][boardY]);
						}
					}
				}
			}


		}

		this.repaint();
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		

	}
}
