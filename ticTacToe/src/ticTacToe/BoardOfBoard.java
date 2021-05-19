package ticTacToe;

import java.util.ArrayList;
import java.util.Random; 

public class BoardOfBoard {
	public Board[][] bigG = new Board[3][3];
	//playable boards shown as a board of 0 and 1s
	public Board playableBoards = new Board(1);
	// number of turns player 1s turn if even, player 2s turn if odd
	public int turns = 0;
	public BoardOfBoard() {
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 3; col++) {
				bigG[row][col] = new Board();
			}
		}
	}
	public BoardOfBoard(Board[][] bigBoard, int turn) {
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 3; col++) {
				bigG[row][col] = new Board();
			}
		}
		turns = turn;
		copyBigG(bigBoard);
	}
	
	//board copied the given board
	public void copy(BoardOfBoard bb) {
		turns = bb.turns;
		playableBoards.copySmallBoard(bb.playableBoards);
		copyBigG(bb.bigG);
	}
	//returns an array that represents a move
	public int[] differentMove(BoardOfBoard kid){
		int[] move = new int[4];
		for(int row = 0; row < 9; row ++) {
			for(int col = 0; col < 9; col++) {
				if(!(bigG[col/3][row/3].b[col%3][row%3]==kid.bigG[col/3][row/3].b[col%3][row%3])) {
					move[0] = col/3;
					move[1] = row/3;
					move[2] = col%3;
					move[3] = row%3;
				}
			}
		}
		return move;
	}

	public boolean sameBoardOfBoard(BoardOfBoard bb){
		if(!(bb.turns == turns)) {
			return false;
		}
		for(int col = 0; col < 3; col++) {
			for(int row = 0; row < 3; row++) {
				if(!(bigG[col][row].sameBoard(bb.bigG[col][row]))) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void copyBigG(Board[][] board) {
		for(int col = 0; col < 3; col++) {
			for(int row = 0; row < 3; row++) {
				if(bigG[col][row] instanceof Board) {
					bigG[col][row].copySmallBoard(board[col][row]);
				}
			}
		}
	}
	public void printOutBoard() {
		for(int row = 0; row < 9; row ++) {
			for(int col = 0; col < 9; col++) {
				if(bigG[col/3][row/3].b[col%3][row%3]==1) {
					System.out.print("|X");
				}
				if(bigG[col/3][row/3].b[col%3][row%3]==2) {
					System.out.print("|O");
				}
				if(bigG[col/3][row/3].b[col%3][row%3]==0) {
					System.out.print("| ");
				}
			}
			System.out.println("|");
		}
		System.out.println();
	}

	public void addPiece(int playerNum, int boardNumX, int boardNumY, int x, int y) {
		bigG[boardNumX][boardNumY].addPiece(playerNum, x, y);

	}

	//randomly simulates a game and returns the integer of the winner or 3 when tied
	//works lika charm!
	public int randomlySimulate() {
		//if the game is still going
		if(gameState() == 0) {
			//create a randomNumber
			Random rand = new Random(); 
			int randomMoveNum = rand.nextInt(possibleMoves().size());
			//			System.out.println("BoardOfBoard, randomlySimulate, possibleMoves().size() = " + possibleMoves().size());
			return (possibleMoves().get(randomMoveNum)).randomlySimulate();
		}
		else 
			//printOutBoard();
		//			System.out.println("BoardOfBoard, randomlySimulate(), gameState() = "+gameState());
		return gameState();
	}

	//returns all possible boards from this position
	//prob works
	public ArrayList<BoardOfBoard> possibleMoves(){
		ArrayList<BoardOfBoard> pM = new ArrayList<BoardOfBoard>();
		for(int col = 0; col < 3; col++) {
			for (int row = 0; row < 3; row++) {
				//if the board is playable
				if (playableBoards.b[col][row] == 1) {
					for (int sCol = 0; sCol < 3; sCol++) {
						for (int sRow = 0; sRow < 3; sRow++) {
							//if the space is open
							if (bigG[col][row].b[sCol][sRow] == 0) {
								BoardOfBoard childBB = new BoardOfBoard(bigG, turns);
								childBB.addPiece(turns % 2 + 1, col, row, sCol, sRow);
								//System.out.println("BoardOfBoard, possibleMoves(), col,row,scol,srow = "+col+row+sCol+sRow);
								childBB.turns = turns + 1;
								childBB.updatePlayable(sCol, sRow);
								pM.add(childBB);
							}
						}
					}
				}
			}
		}
		//		System.out.println("BoardOfBoard, possibleMoves(before returning), pM.size() = "+ pM.size());
		return pM;
	}

	//returns the gameState as an integer
	//0 = game is still going
	//1 = player 1 has won
	//2 = player 2 has won
	//3 = tie
	public int gameState() {
		// check all 3 rows and columns
		for (int n = 0; n < 3; n++) {
			if ((bigG[n][0].boardState() == bigG[n][1].boardState())
					&& (bigG[n][0].boardState() == bigG[n][2].boardState())
					&&!(bigG[n][0].boardState()==0)) {
				return bigG[n][0].boardState();
			}
			if ((bigG[0][n].boardState() == bigG[1][n].boardState())
					&& (bigG[0][n].boardState() == bigG[2][n].boardState())
					&&!(bigG[0][n].boardState()==0)) {
				return bigG[0][n].boardState();
			}
		}
		// check diagonals
		if ((bigG[0][0].boardState() == bigG[1][1].boardState())
				&& (bigG[1][1].boardState() == bigG[2][2].boardState())
				&&!(bigG[0][0].boardState()==0)) {
			return bigG[0][0].boardState();
		}
		if ((bigG[2][0].boardState() == bigG[1][1].boardState())
				&& (bigG[1][1].boardState() == bigG[0][2].boardState())
				&&!(bigG[2][0].boardState()==0)) {
			return bigG[2][0].boardState();
		}
		for (int r = 0; r < 3; r++) {
			for (int w = 0; w < 3; w++) {
				if (bigG[r][w].boardState() == 0)
					return 0;
			}
		}
		return 3;
	}
	public void updatePlayable(int x, int y) {
		//if the board selected is not finished select that board as only playable board
		if(bigG[x][y].boardState() == 0) {
			playableBoards = new Board(0);
			playableBoards.b[x][y] = 1;
		}
		//if the board selected is finished play on any other board that is not finished
		else {
			for(int col = 0; col < 3; col++) {
				for(int row = 0; row < 3; row++) {
					if(bigG[col][row].boardState() == 0) {
						playableBoards.b[col][row] = 1;
					}
					else {
						playableBoards.b[col][row] = 0;
					}
				}
			}
		}
	}
}
