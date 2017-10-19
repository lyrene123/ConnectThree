
package com.threestones.server.gamestate;

/**
 *
 * @author Lyrene Labor
 */
public class ThreeStonesGameBoard {
    public enum CellState{
        BLACK, WHITE, AVAILABLE, VACANT, UNAVAILABLE
    }
    
    private CellState[][] board;
    private int blackStoneCount;
    private int whiteStoneCount;
    private int whiteScore;
    private int blackScore;
    
    public ThreeStonesGameBoard(){
          board = new CellState[][]{{CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT},
                                 {CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT},
          {CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.AVAILABLE,CellState.AVAILABLE,CellState.AVAILABLE,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT},
          {CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.AVAILABLE,CellState.AVAILABLE,CellState.AVAILABLE,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT},
{CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.AVAILABLE,CellState.AVAILABLE,CellState.AVAILABLE,CellState.AVAILABLE,CellState.AVAILABLE,CellState.VACANT,CellState.VACANT,CellState.VACANT},
{CellState.VACANT,CellState.VACANT,CellState.AVAILABLE,CellState.AVAILABLE,CellState.AVAILABLE,CellState.VACANT,CellState.AVAILABLE,CellState.AVAILABLE,CellState.AVAILABLE,CellState.VACANT,CellState.VACANT},
{CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.AVAILABLE,CellState.AVAILABLE,CellState.AVAILABLE,CellState.AVAILABLE,CellState.AVAILABLE,CellState.VACANT,CellState.VACANT,CellState.VACANT},
{CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.AVAILABLE,CellState.AVAILABLE,CellState.AVAILABLE,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT},
{CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.AVAILABLE,CellState.AVAILABLE,CellState.AVAILABLE,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT},
{CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT},
{CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT,CellState.VACANT}};
          }
  
              
    

    public CellState[][] getBoard() {
        return board;
    }

    public void setBoard(CellState[][] board) {
        this.board = board;
    }

    public int getBlackStoneCount() {
        return blackStoneCount;
    }

    public void setBlackStoneCount(int blackStoneCount) {
        this.blackStoneCount = blackStoneCount;
    }

    public int getWhiteStoneCount() {
        return whiteStoneCount;
    }

    public void setWhiteStoneCount(int whiteStoneCount) {
        this.whiteStoneCount = whiteStoneCount;
    }

    public int getWhiteScore() {
        return whiteScore;
    }

    public void setWhiteScore(int whiteScore) {
        this.whiteScore = whiteScore;
    }

    public int getBlackScore() {
        return blackScore;
    }

    public void setBlackScore(int blackScore) {
        this.blackScore = blackScore;
    }
    /**
     * @returns number of points scored from the coordinate 
     * @param x x coordinate on game board
     * @param y y coordinate on game board
     */
    public int checkForThreeStones (int x, int y, CellState color){
        int points = 0;

        
        return points;
    }
	
	//returns an altered version of the board 
	//I made it to return for now incase we want to 
	//make the ai more sophisticated (to check for moves 2-3 turns ahead)
    
    
        
	public CellState[][] changeBoard(int x, int y){
		CellState[][] board = this.board;

                if (!checkIfFull(x,y)) {
		for (int i = 0 ; i < board[0].length;i++){
			for (int j = 0; j < board[0].length;j++){

				if (i == x || j == y && board[i][j] != CellState.VACANT){
					board[i][j] = CellState.AVAILABLE;
				}
				else if (board[i][j] != CellState.VACANT){
					board[i][j] = CellState.AVAILABLE;
				}
			}
                       
		}
                }
                else {
                    for (int i = 0; i < board[0].length;i++){
                        for (int j = 0;j <board[0].length;j++){
                            if (board[i][j] == CellState.UNAVAILABLE){
                            board[i][j] = CellState.AVAILABLE;
                            }
                        }
                    }
                }
		return board;
	}
        
        //checks if both row and column is full
        private boolean checkIfFull(int x, int y){
            //check row
            boolean full = true;
            for (int i = 0;i <board[0].length && full;i++){
                if (board[i][x] == CellState.AVAILABLE || board[i][x] == CellState.UNAVAILABLE ){
                    full = false;
                }
            }
            //check col
            for (int i = 0; i <board[0].length && full; i++){
                if (board[x][y] == CellState.AVAILABLE || board[x][y] == CellState.UNAVAILABLE ){
                    full = false;
                }
            }
            return full;
        }

    
}
