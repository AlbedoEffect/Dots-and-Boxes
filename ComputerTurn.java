package gameplay;

public class ComputerTurn {
	//For the 3 dimensional array triggermoves, 0 represents rows in (x,y) coordinates starting from the top left.
	//and 1 represents columns.
	//Global Declarations
	static int rows;
	static int columns;
	static int numBoxes;
	int numFilled;
	static int [][]triggerMoves1;
	int [][] boxes1;
	int [][] boxes2;
	static int [][]triggerMoves2;
	int userBoxes;
	int oppBoxes;
	
	//Constant declarations
	static final int ROWVSCOL = 2;
	static final int START = 1;
	static final int desDepth = 1;
	static final int TOTAL = rows*(columns-1) + columns*(rows-1);
	//
	public ComputerTurn(int rows, int columns){
		boxes1 = new int[rows][columns-1];
		boxes2 = new int[columns][rows-1];
		numBoxes = 1;
		this.rows = rows;
		this.columns = columns;
		triggerMoves1 = new int[rows][columns-1];
		triggerMoves2 = new int[columns][rows-1];
	}
	
	//Initial function to check for 'trigger' moves, or moves that allow the AI to take a long chain.
	//array1: horizLineArray2
	//array2: vertLineArray2
	//numFilled: number of currently used spaces on the game board
	//(x,y) -> ordered pair to represent horiz. move if col = 0, or vert. move if col = 1
	//move represented is the last move played by the opponent.
	public int[] analyseBoard(boolean[][] array1, boolean [][] array2, int numFilled, int x, int y, int col){
		this.numFilled = numFilled;
		int [] move = new int[3];
		userBoxes = 0;
		oppBoxes = 0;
		//resetTriggers(0);
		//resetTriggers(1);
		/*move = checkTriggers(rows,columns-1,0,triggerMoves1,array1);
		if(move[2] == -1){
			move = checkTriggers(columns,rows-1,1,triggerMoves2,array2);
		}else{
			if(move[2]==0){
				triggerMoves1[move[0]][move[1]] = false;
			}else{
				triggerMoves2[move[0]][move[1]] = false;
			}
			move = allButTwo(array1,array2,numFilled);
			if(move[2] != -1){
				return move;
			}
		}
		if(move[2] != -1){
			if(move[2]==0){
				triggerMoves1[move[0]][move[1]] = false;
			}else{
				triggerMoves2[move[0]][move[1]] = false;
			}
				move = allButTwo(array1,array2,numFilled);
				if(move[2]!=-1){
					return move;
				}
			}
		*/
		//if(numFilled >= 6){
			//if(numBoxes >= 3){
				//move = allButTwo(array1,array2,numFilled);
				//if(move[2] == -1){
					//System.out.println("found1 -1");
				//}
			//}else{
				//move = evaluate(array1,array2,0,1);
			//}
		//}else{
		/*for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns -1; j++){
				if(array1[i][j]) continue;
				array1[i][j] = true;
				boxes1[i][j]=numTriggered(i,j,0,array1,array2, new boolean[rows][columns-1],new boolean[columns][rows-1]);
				array1[i][j] = false;
				System.out.println("Array1 val: " + i + " "  + j + " " + boxes1[i][j]);
			}
		}
		
		for(int i = 0; i < columns; i++){
			for(int j = 0; j < rows - 1; j++){
				if(array2[i][j]) continue;
				array2[i][j] = true; 
				boxes2[i][j] = numTriggered(i,j,1,array1,array2, new boolean[rows][columns-1],new boolean[columns][rows-1]);
				array2[i][j] = false;
				System.out.println("Array2 val: " + i + " "  + j + " " + boxes2[i][j]);
			}
		}*/
		
		//System.out.println("boxes " + numBoxes);
		 move = minimax(array1,array2,1,0);
		//}
	return move;
	}
	
	public static int[] checkTriggers(int count1, int count2,int col, boolean [][] triggerMoves, boolean [][] array){
		int move[] = new int[3];
		move[2] = -1;
		for(int i = 0; i < count1; i++){
			for(int j = 0; j < count2; j++){
				if(triggerMoves[i][j]){
					if(array[i][j]){
						System.out.println("success with " + i + " " + j + " " + col);
							move[0] = i;
							move[1] = j;
							move[2] = col;
							return move;
					}
				}
			}
		}
		return move;
	}
	
	public static void resetTriggers(int col){
		//Row moves reset
		if(col == 0){
			for(int i = 0; i < rows; i++){
				for(int j = 0; j < columns - 1; j++){
					triggerMoves1[i][j] = 0;
				}
			}
		}else{
			//Column moves reset
			for(int i = 0; i < columns; i++){
				for(int j = 0; j < rows - 1; j++){
					triggerMoves2[i][j] = 0;
				}
			}
		}
	}
	
	public  boolean completesSquare(int x, int y, int col, boolean[][]array1, boolean[][] array2, boolean checkFill){
		if(!((col == 0 && array1[x][y]) || (col == 1 && array2[x][y])) || checkFill){
			if(x == 0 && col == 0){
				if(!array1[x+1][y] || !array2[y][x] || !array2[y+1][x]){
					return false;
				}
			}else if(x == 0){
				if(!array2[x+1][y] || !array1[y][x] || !array1[y+1][x]){
					//System.out.println("Doesn't complete square....2 " + x + " " + y + " " + col);
					return false;
				}
			}else if(x == rows-1 && col == 0){
				if(!array1[x-1][y] || !array2[y][x-1]|| !array2[y+1][x-1]){
					//System.out.println("Doesn't complete square....3 " + x + " " + y + " " + col);
					return false;
				}
			}else if(x == columns-1 && col == 1){
				if(!array2[x-1][y] || !array1[y][x-1] || !array1[y+1][x-1]){
					//System.out.println("Doesn't complete square....4 " + x + " " + y + " " + col);
					return false;
				}
			}else if(col == 0){
				if((!array1[x+1][y] || !array2[y][x] || !array2[y+1][x]) &&
					(!array1[x-1][y] || !array2[y][x-1] || !array2[y+1][x-1])){
					//System.out.println("Doesn't complete square....5 " + x + " " + y + " " + col);
					return false;
				}
			}else if(col == 1){
				if((!array2[x-1][y] || !array1[y][x-1] || !array1[y+1][x-1]) && 
						(!array2[x+1][y] || !array1[y][x] || !array1[y+1][x])){
					//System.out.println("Doesn't complete square...6 " + x + " " + y + " " + col);
					return false;
				}
			}
			//System.out.println("Completes square! " + x + " " + y + " " + col);
			return true;
		}else{
			//System.out.println("Doesn't complete square....checked " + x + " " + y + " " + col);
			//System.out.println("initial fail");
			return false;
		}
	}

	public int numTriggered(int x, int y, int col, boolean[][] array1, boolean [][]array2, boolean checked1[][]
			,boolean [][] checked2){
		int boxes = 0;
		int triggers = 0;
		if(col==0){
			checked1[x][y] = true;
		}else{
			checked2[x][y] = true;
		}
		if(!allButTwo(x,y,col,array1,array2)){
			return boxes;
		}
		if((col == 0 && x == 0)){
					if(!checked1[x+1][y]){
						if(completesSquare(x+1,y,col,array1,array2,false)){
							boxes++;
							triggerMoves1[x+1][y]++;
							array1[x+1][y] = true;
							boxes += numTriggered(x+1,y,col,array1,array2,checked1,checked2);
							triggerMoves1[x+1][y]+= boxes;
							array1[x+1][y] = false;
						}
					}
					if(!checked2[y+1][x]){
						if(completesSquare(y+1,x,1,array1,array2,false)){
							boxes++;
							triggerMoves2[y+1][x]++;
							array2[y+1][x] = true;
							boxes  += numTriggered(y+1,x,1,array1,array2,checked1,checked2);
							triggerMoves2[y+1][x]+= boxes;
							array2[y+1][x] = false;
						}
					}
					if(!checked2[y][x]){
						if(completesSquare(y,x,1,array1,array2,false)){
							boxes++;
							triggerMoves2[y][x]++;
							array2[y][x] = true;
							boxes += numTriggered(y,x,1,array1,array2,checked1,checked2);
							triggerMoves1[y][x]+= boxes;
							array2[y][x] = false;
						}
					}
				}else if (x == 0){
					if(!checked2[x+1][y]){
						if(completesSquare(x+1,y,1,array1,array2,false)){
							boxes++;
							triggerMoves2[x+1][y]++;
							array2[x+1][y] = true;
							boxes += numTriggered(x+1,y,1,array1,array2,checked1,checked2);
							triggerMoves2[x+1][y]+= boxes;
							array2[x+1][y] = false;
						}
					}
					
					if(!checked1[y+1][x]){
						if(completesSquare(y+1,x,0,array1,array2,false)){
							boxes++;
							triggerMoves1[y+1][x]++;
							array1[y+1][x] = true;
							boxes += numTriggered(x+1,y,1,array1,array2,checked1,checked2);
							triggerMoves1[y+1][x]+= boxes;
							array1[y+1][x] = false;
						}
					}
					if(!checked1[y][x]){
						if(completesSquare(y,x,0,array1,array2,false)){
							boxes++;
							triggerMoves1[y][x] ++;
							array1[y][x] = true;
							boxes += numTriggered(x+1,y,1,array1,array2,checked1,checked2);
							triggerMoves1[x+1][y]+= boxes;
							array1[y][x] = false;
						}
					}
				}else if(col == 0 && x == rows-1){
					if(!checked1[x-1][y]){
						if(completesSquare(x-1,y,0,array1,array2,false)){
							boxes++;
							triggerMoves1[x-1][y] ++;
							array1[x-1][y] = true;
							boxes += numTriggered(x-1,y,col,array1,array2,checked1,checked2);
							triggerMoves1[x-1][y]+= boxes;
							array1[x-1][y] = false;
						}
					}
					if(!checked2[y+1][x-1]){
						if(completesSquare(y+1,x-1,1,array1,array2,false)){
							boxes++;
							triggerMoves2[y+1][x-1]++;
							array2[y+1][x-1] = true;
							boxes += numTriggered(y+1,x-1,1,array1,array2,checked1,checked2);
							triggerMoves2[y+1][x-1]+= boxes;
							array2[y+1][x-1] = false;
						}
					}
					if(!checked2[y][x-1]){
						if(completesSquare(y,x-1,1,array1,array2,false)){
							boxes++;
							triggerMoves1[y][x-1]++;
							array2[y][x-1] = true;
							boxes += numTriggered(y,x-1,1,array1,array2,checked1,checked2);
							triggerMoves1[y][x-1]+= boxes;
							array2[y][x-1] = false;
						}
					}
					
					
				}else if(col == 1 && x == columns-1){
					if(!checked2[x-1][y]){
						if(completesSquare(x-1,y,col,array1,array2,false)){
							boxes++;
							triggerMoves2[x-1][y]++;
							array2[x-1][y] = true;
							boxes += numTriggered(x-1,y,1,array1,array2,checked1,checked2);
							triggerMoves2[x-1][y]+= boxes;
							array2[x-1][y] = false;
						}
					}
					
					if(!checked1[y+1][x-1]){
						if(completesSquare(y+1,x-1,0,array1,array2,false)){
							boxes++;
							triggerMoves1[y+1][x-1]++;
							array1[y+1][x-1] = true;
							boxes += numTriggered(y+1,x-1,0,array1,array2,checked1,checked2);
							triggerMoves1[y+1][x-1]+= boxes;
							array1[y+1][x-1] = false;
						}
					}
	
					if(!checked1[y][x-1]){
						if(completesSquare(y,x-1,0,array1,array2,false)){
							boxes++;
							triggerMoves1[y][x-1]++;
							array1[y][x-1] = true;
							boxes += numTriggered(y,x-1,0,array1,array2,checked1,checked2);
							triggerMoves1[y][x-1]+= boxes;
							array1[y][x-1] = false;
						}
					}
					
				}else if(col == 0){
					if(!checked1[x+1][y]){
						if(completesSquare(x+1,y,0,array1,array2,false)){
							boxes++;
							triggerMoves1[x+1][y]++;
							array1[x+1][y] = true;
							boxes += numTriggered(x+1,y,0,array1,array2,checked1,checked2);
							triggerMoves1[x+1][y]+= boxes;
							array1[x+1][y] = false;
						}
					}
	
					if(!checked2[y+1][x]){
						if(completesSquare(y+1,x,1,array1,array2,false)){
							boxes++;
							triggerMoves2[y+1][x]++;
							array2[y+1][x] = true;
							boxes += numTriggered(y+1,x,1,array1,array2,checked1,checked2);
							triggerMoves2[y+1][x]+= boxes;
							array2[y+1][x] = false;
						
						}
					}
						
						if(!checked1[y][x]){
							if(completesSquare(y,x,0,array1,array2,false)){
								boxes++;
								triggerMoves1[y][x]++;
								array1[y][x] = true;
								boxes += numTriggered(y,x,0,array1,array2,checked1,checked2);
								triggerMoves1[y][x]+= boxes;
								array1[y][x] = false;
							}
						}
	
						if(!checked2[x+1][y]){
							if(completesSquare(x+1,y,1,array1,array2,false)){
								boxes++;
								triggerMoves2[x+1][y]++;
								array2[x+1][y] = true;
								boxes += numTriggered(x+1,y,1,array1,array2,checked1,checked2);
								triggerMoves2[x+1][y]+= boxes;
								array2[x+1][y] = false;
							}
						}
	
							if(!checked1[y+1][x]){
								if(completesSquare(y+1,x,0,array1,array2,false)){
									boxes++;
									triggerMoves1[y+1][x]++;
									array1[y+1][x] = true;
									boxes += numTriggered(y+1,x,0,array1,array2,checked1,checked2);
									triggerMoves1[y+1][x]+= boxes;
									array1[y+1][x] = false;
							}
						}
	
						if(!checked1[y][x]){
							if(completesSquare(y,x,0,array1,array2,false)){
								boxes++;
								triggerMoves1[y][x]++;
								array1[y][x] = true;
								boxes += numTriggered(y,x,0,array1,array2,checked1,checked2);
								triggerMoves1[y][x]+= boxes;
								array1[y][x] = false;
							}
						}
				}else if(col == 1){
					if(!checked2[x+1][y]){
						if(completesSquare(x+1,y,1,array1,array2,false)){
							boxes++;
							triggerMoves2[x+1][y]++;
							array2[x+1][y] = true;
							boxes += numTriggered(x+1,y,1,array1,array2,checked1,checked2);
							triggerMoves2[x+1][y]+= boxes;
							array2[x+1][y] = false;
						}
					}
	
					if(!checked1[y+1][x]){
						if(completesSquare(y+1,x,0,array1,array2,false)){
							boxes++;
							triggerMoves1[y+1][x]++;
							array1[y+1][x] = true;
							boxes += numTriggered(y+1,x,0,array1,array2,checked1,checked2);
							triggerMoves1[y+1][x]+= boxes;
							array1[y+1][x] = false;
						
						}
					}
						
						if(!checked1[y][x]){
							if(completesSquare(y,x,0,array1,array2,false)){
								boxes++;
								triggerMoves1[y][x]++;
								array1[y][x] = true;
								boxes += numTriggered(y,x,0,array1,array2,checked1,checked2);
								triggerMoves1[y][x]+= boxes;
								array1[y][x] = false;
							}
						}
	
						if(!checked2[x-1][y]){
							if(completesSquare(x-1,y,1,array1,array2,false)){
								boxes++;
								triggerMoves2[x-1][y]++;
								array2[x-1][y] = true;
								boxes += numTriggered(x-1,y,1,array1,array2,checked1,checked2);
								triggerMoves2[x-1][y]+= boxes;
								array2[x-1][y] = false;
							}
						}
	
						if(!checked1[y+1][x-1]){
							if(completesSquare(y+1,x-1,0,array1,array2,false)){
								boxes++;
								triggerMoves1[y+1][x-1]++;
								array1[y+1][x-1] = true;
								boxes += numTriggered(y+1,x-1,0,array1,array2,checked1,checked2);
								triggerMoves1[y+1][x-1]+= boxes;
								array1[y+1][x-1] = false;
							}
						}
	
						if(!checked1[y][x-1]){
							if(completesSquare(y,x-1,0,array1,array2,false)){
								boxes++;
								triggerMoves1[y][x-1]++;
								array1[y][x-1] = true;
								boxes += numTriggered(y,x-1,0,array1,array2,checked1,checked2);
								triggerMoves2[y][x-1]+= boxes;
								array1[y][x-1] = false;
							}
						}
					}
				return boxes;
				}

	
	public boolean allButTwo(int x, int y, int col, boolean[][]array1, boolean[][]array2){
		
		if(!((col == 0 && array1[x][y]) || (col == 1 && array2[x][y]))){
			if(x == 0 && col == 0){
				if((array1[x+1][y] && array2[y][x]) || (array2[y+1][x] && array1[x+1][y]) || (array2[y][x] && array2[y+1][x])){
					return true;
				}
			}else if(x == 0){
				if((array2[x+1][y] && array1[y][x]) || (array1[y+1][x] && array2[x+1][y]) || (array1[y][x] && array1[y+1][x])){
					//System.out.println("Doesn't complete square....2 " + x + " " + y + " " + col);
					return true;
				}
			}else if(x == rows-1 && col == 0){
				if((array1[x-1][y] && array2[y][x-1]) || (array2[y+1][x-1] && array1[x-1][y]) ||(array2[y][x-1] && array2[y+1][x-1])){
					//System.out.println("Doesn't complete square....3 " + x + " " + y + " " + col);
					return true;
				}
			}else if(x == columns-1 && col == 1){
				if((array2[x-1][y] && array1[y][x-1]) || (array1[y+1][x-1] && array2[x-1][y])||(array1[y][x-1] && array1[y+1][x-1])) {
					//System.out.println("Doesn't complete square....4 " + x + " " + y + " " + col);
					return true;
				}
			}else if(col == 0){
				if((array1[x+1][y] && array2[y][x]) || (array2[y+1][x] && array1[x+1][y]) || (array2[y][x] && array2[y+1][x])
					|| (((array1[x-1][y] && array2[y][x-1]) || (array2[y+1][x-1] && array1[x-1][y]) 
							|| (array2[y+1][x-1] && array2[y][x-1])))){
					//System.out.println("Doesn't complete square....5 " + x + " " + y + " " + col);
					return true;
				}
			}else if(col == 1){
				if(((array2[x-1][y] && array1[y][x-1]) || (array1[y+1][x-1] && array2[x-1][y]) || (array1[y][x-1] && array1[y+1][x-1])) 
						|| (((array2[x+1][y] && array1[y][x]) || (array1[y+1][x] && array2[x+1][y]) || (array1[y][x] && array1[y+1][x])))){
					//System.out.println("Doesn't complete square...6 " + x + " " + y + " " + col);
					return true;
				}
			}
			//System.out.println("Completes square! " + x + " " + y + " " + col);
			return false;
		}else{
			//System.out.println("Doesn't complete square....checked " + x + " " + y + " " + col);
			//System.out.println("initial fail");
			return false;
		}
		}
	
	public int testPos(boolean [][] array1, boolean [][] array2, int turn, int depth){
		int points = 0;
		
		if(depth > desDepth){
			return points;
		}
		
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns - 1; j++){
				if(array1[i][j]) continue;
					points += checkMoves(array1,array2,turn,i,j,0);
					/*if(completesSquare(i,j,0,array1,array2,false)){
						array1[i][j] = true;
						points += testPos(array1,array2,turn,depth++);
					}else{
						array1[i][j] = true;
						points += testPos(array1,array2,++turn,depth++);
					}
					array1[i][j] = false;
				}
		}
		
		for(int i = 0; i < columns; i++){
			for(int j = 0; j < rows-1; j++){
				if(array2[i][j]) continue;
					points  += checkMoves(array1,array2,turn,i,j,1);
					/*if(completesSquare(i,j,1,array1,array2,false)){
						array2[i][j] = true;
						points += testPos(array1,array2,turn,depth++);
					}else{
						array2[i][j] = true;
						points += testPos(array1,array2,++turn,depth++);
					}
					array2[i][j] = false;*/
			}
		}
		return points;
	}
	
	public boolean gameOver(){
		return numFilled == TOTAL;
	}
	
	/*private int[] checkMoves(int rows, int columns, int isHorizMove, int[] bestMove){
		
	}*/
	
	public int[] minimax(boolean[][] array1, boolean[][] array2, int turn, int depth){
		int [] bestMove = new int[4];
		boolean compTurn = turn % 2  == START;
		if(turn % 2 == START){
			bestMove[3] = Integer.MIN_VALUE;
		}else{
			bestMove[3] = Integer.MAX_VALUE;
		}
		int [] tempMove = new int[4];
		int points = 0;
		if(depth ==  desDepth || gameOver()){
				int [] base = new int[4]; 
				base[3]= evaluate(array1,array2,turn);
				userBoxes = 0;
				oppBoxes = 0;
				System.out.println("Points: " + base[3]);
				return base;
		}
		
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns-1; j++){
				if(array1[i][j])continue;
				array1[i][j] = true;
				tempMove[0]= i;
				tempMove[1] = j;
				tempMove[2] = 0;
				
 				if(completesSquare(i,j,0,array1,array2,true)){
					if(compTurn){
						userBoxes++;
						System.out.println("userBoxes: "  + userBoxes);
					}else{
						oppBoxes++;
						System.out.println("oppBoxes: "  + oppBoxes);
					}
					tempMove[3] = minimax(array1,array2,turn,depth+1)[3];
				}else{
					tempMove[3] = minimax(array1,array2,turn+1,depth+1)[3];
				}
				array1[i][j] = false;
			if(compTurn){
				if(tempMove[3] > bestMove[3]){
					bestMove[0] = tempMove[0];
					bestMove[1] = tempMove[1];
					bestMove[2] = tempMove[2];
					bestMove[3] = tempMove[3];
				}
			}else{
				if(tempMove[3] < bestMove[3]){
					bestMove[0] = tempMove[0];
					bestMove[1] = tempMove[1];
					bestMove[2] = tempMove[2];
					bestMove[3] = tempMove[3];
				}
			}
		}
	}
		
		for(int i = 0; i < columns; i++){
			for(int j = 0; j < rows-1; j++){
				if(array2[i][j]) continue;
				tempMove[0]= i;
				tempMove[1] = j;
				tempMove[2] = 0;
				array2[i][j] = true;
				if(completesSquare(i,j,1,array1,array2,true)){
					if(compTurn){
						userBoxes++;
					}else{
						oppBoxes++;
					}
					tempMove[3] = minimax(array1,array2,turn,depth+1)[3];
				}else{
					tempMove[3] = minimax(array1,array2,turn+1,depth+1)[3];
				}
				array2[i][j] = false;
				if(compTurn){
					if(tempMove[3] > bestMove[3]){
						bestMove[0] = tempMove[0];
						bestMove[1] = tempMove[1];
						bestMove[2] = tempMove[2];
						bestMove[3] = tempMove[3];
					}
				}else{
					if(tempMove[3] < bestMove[3]){
						bestMove[0] = tempMove[0];
						bestMove[1] = tempMove[1];
						bestMove[2] = tempMove[2];
						bestMove[3] = tempMove[3];
					}
				}
			}
		}
		System.out.println("Move: " + bestMove[3]);
		return bestMove;
	}
	
	public int evaluate(boolean [][] array1, boolean [][] array2, int turn){
		int points = 0;
		System.out.println("Evaluate: User boxes: " + userBoxes + " oppBoxes " + oppBoxes);
		if(turn%2 == START){
			points = userBoxes - oppBoxes;
		}else{
			points = oppBoxes-userBoxes;
		}
		
		/*for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns-1; j++){
				points +=checkMoves(array1,array2,turn,i,j,0);
			}
		}
		
		for(int i = 0; i < columns; i++){
			for(int j = 0; j < rows-1; j++){
				points += checkMoves(array1,array2,turn,i,j,1);
			}
		}*/
		
		return points;
	}
	
	public int checkMoves(boolean [][] array1, boolean[][] array2, int turn, int x, int y, int col){
		int points = 0;
		int triggerPoints = numTriggered(x,y,col,array1,array2, new boolean [rows][columns-1], new boolean[columns][rows-1]);
		if(triggerPoints != 0){
			System.out.println("trigger " +  x + " " + y + " " + col + " " + triggerPoints);
		}
		if(turn % 2 == START){
			if(completesSquare(x,y,col,array1,array2,false)){
					points += triggerPoints;
					//System.out.println("completes square: " + x + " " + y + " " + col);
					points++;
				}else{
					//System.out.println("subtracting trigger points: " + x + " " + y + " " + col);						points -= triggerPoints;
				}
		}
		System.out.println("Points check: " + points);
		return points;
	}
}