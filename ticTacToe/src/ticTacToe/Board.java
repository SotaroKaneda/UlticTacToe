package ticTacToe;

public class Board {
	public int[][] b = new int[3][3];
	//constructor for a TicTacToe board
	public Board() {
	}
	//constructs the board filled with num
	public Board(int num) {
		for(int col = 0; col < 3; col++)
		{
			for(int row = 0; row < 3; row++) {
				b[col][row] = num;
			}
		}
	}

	public boolean sameBoard(Board board) {
		for(int col = 0; col < 3; col++) {
			for(int row = 0; row < 3; row++) {
				if(!(b[col][row] == board.b[col][row])) {
					return false;
				}
			}
		}
		return true;
	}
	public void copySmallBoard(Board board) {
		for(int col = 0; col < 3; col++) {
			for(int row = 0; row < 3; row++) {
				b[col][row] = board.b[col][row];
			}
		}
	}
	public void addPiece(int playerNum, int x, int y) {
		b[x][y] = playerNum;
	}

	//returns the gameState as an integer
	//0 = game is still going
	//1 = player 1 has won
	//2 = player 2 has won
	//3 = tie
	public int boardState() {
		//check all 3 rows and columns
		for(int n=0; n<3; n++) {
			if((b[n][0] == b[n][1]) && (b[n][0] == b[n][2])&&!(b[n][0]==0)) {
				return b[n][0];
			}
			if((b[0][n] == b[1][n]) && (b[0][n] == b[2][n])&&!(b[0][n]==0)) {
				return b[0][n];
			}
		}
		//check diagonals
		if((b[0][0] == b[1][1]) && (b[1][1] == b[2][2])&&!(b[0][0]==0)) {
			return b[0][0];
		}
		if((b[2][0] == b[1][1]) && (b[1][1] == b[0][2])&&!(b[2][0]==0)) {
			return b[2][0];
		}
		for(int r = 0; r < 3; r++) {
			for(int w = 0; w < 3; w++) {
				if(b[r][w]==0) 
					return 0;
			}
		}
		return 3;
	}

}
