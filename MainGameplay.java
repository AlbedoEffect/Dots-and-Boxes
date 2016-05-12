package frameCreation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import gameplay.ComputerTurn;
public class MainGameplay extends JPanel{	
	
	final int boxSize = 80;
	final int START = 1;
	final Dimension frameSize = new Dimension(1000,1000);
	final int [] spacing;
	final int lineThickness = 5;
	
	int numFilled = 0;
	static int turn = 0;
	static JFrame frame1;
	MainGameplay.Grid panel;
	
	final int rows;
	final int cols;
	
	ComputerTurn computer;
	public  MainGameplay(int rows, int cols){
		this.rows = rows;
		this.cols = cols;
		computer = new ComputerTurn(rows,cols);
		spacing = new int[]{(boxSize*cols-(lineThickness*cols))/(cols-1),
				(boxSize*cols-lineThickness)/(rows-1)};
		frame1 = new JFrame("Main Game");
		frame1.setSize(frameSize);
		frame1.setVisible(true);
		panel = new MainGameplay.Grid();
		frame1.add(panel);
		frame1.setDefaultCloseOperation(frame1.EXIT_ON_CLOSE);
		panel.addMouseListener(new MouseHandling());
	}
	
	public void drawing(Graphics g){
		repaint();
	}
	
	public class Grid extends JPanel{
		
		//Constant declarations
		final int colWidth = (boxSize*cols)/(rows-1); 
		// Arrays to keep track of the coordinates of the
		// vertices within the grid (relative to JPanel).
		int [] horizLineArray = new int[cols];
		int [] vertLineArray = new int[rows];
		
		//Arrays to keep track of clicked versus un-clicked lines.
		 boolean [][] horizLineArray2 = new boolean[rows][cols-1];
		 boolean [][] vertLineArray2 = new boolean[cols][rows-1];
		 
		 boolean [][] playerFilled1 = new boolean[rows][cols-1];
		 boolean [][] playerFilled2 = new boolean[cols][rows-1];
		 boolean [][] compFilled1 = new boolean[rows][cols-1];
		 boolean [][] compFilled2 = new boolean[cols][rows-1];
		 int [] prevMove = new int[3];
		
		public Grid(){
			for(int i = 0; i < 3; i++){
				prevMove[i] = -1;
			}
		}
		//Initializes grid lines, separated into different arrays
		// for horizontal and vertical lines.
		// false will be an unfilled line, true will be a filled line.
		public void initializeGrid(){
			//
			//System.out.println("initialized grid");
				for(int j = 0; j < cols; j++){
					horizLineArray[j] = spacing[0]*(j+1);
				}
			
				for(int j = 0; j < rows; j++){
					vertLineArray[j] = 10 + spacing[1]*j;
				}
				
				for(int i = 0; i < rows; i++){
					for(int k = 0; k < cols - 1; k++){
						horizLineArray2[i][k] = false;
					}
				}
				
				for(int i = 0; i < cols; i++){
					for(int k = 0; k < rows - 1; k++){
						vertLineArray2[i][k] = false;
					}
				}
		}
		
		//Paints the grid for the game.
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			//column creation
				for(int i = 0; i < cols; i++){
					for(int j = 0; j < rows - 1; j++){
						if(playerFilled2[i][j]){
							g.setColor(Color.red);
							g.fillRect(spacing[0]*(i+1), 10+(colWidth*j), lineThickness, colWidth);
						}else if(compFilled2[i][j]){
							g.setColor(Color.blue);
							g.fillRect(spacing[0]*(i+1), 10+(colWidth*j), lineThickness, colWidth);
						}else{
							g.setColor(Color.black);
							g.drawRect(spacing[0]*(i+1), 10+(colWidth*j), lineThickness, colWidth);
						}
					}
				}
				
				//row creation
				for(int i = 0; i < rows; i++){
					for(int j = 0; j < cols - 1; j++){
						if(playerFilled1[i][j]){
							g.setColor(Color.red);
							g.fillRect((1+j)*spacing[0], spacing[1]*i + 10, spacing[0], lineThickness);
						}else if(compFilled1[i][j]){
							g.setColor(Color.blue);
							g.fillRect((1+j)*spacing[0], spacing[1]*i + 10, spacing[0], lineThickness);
						}else{
							g.setColor(Color.black);
							g.drawRect((1+j)*spacing[0], spacing[1]*i + 10, spacing[0], lineThickness);
						}
					}
				}
			}
		
		// Checks the (x,y) Cartesian coordinate of the mouse click to see if
		// if it's on a line.
		public void checkClick(int posX, int posY){
			int move[] = new int[3];
			
			//Row clicking detection
			for(int i = 0; i < rows; i++){
				if(((vertLineArray[i]<= posY) && (posY 	<= (vertLineArray[i]+lineThickness)))){
					for(int k = 0; k < cols - 1; k++){
						if(((horizLineArray[k]+lineThickness) <= posX) && (posX <= (horizLineArray[k+1]))){
							if(!(horizLineArray2[i][k])){
								numFilled++;
								if(!computer.completesSquare(i, k, 0, horizLineArray2, vertLineArray2,false)){
									turn++;
								}
								horizLineArray2[i][k] = true;
								playerFilled1[i][k] = true;
								prevMove[0] = i;
								prevMove[1] = k;
								prevMove[2] = 0;
								panel.revalidate();
								panel.repaint();
							}
						}
					}
				}
			}
			
			//Column clicking detection
		for(int i = 0; i < cols; i++){
					for(int k = 0; k < rows-1; k++){
						if(((vertLineArray[k] + lineThickness) < posY) && (posY < vertLineArray[k+1])){
						if((horizLineArray[i] <= posX) && (posX <= (horizLineArray[i]
							+ lineThickness))){
							if(!(vertLineArray2[i][k])){
								System.out.println(
										"vert Test " + computer.completesSquare(i, k, 1, horizLineArray2, vertLineArray2,false));
								if(!computer.completesSquare(i, k, 1, horizLineArray2, vertLineArray2,false)){
									turn++;
								}
								numFilled++;
								vertLineArray2[i][k] = true;
								playerFilled2[i][k] = true;
								prevMove[0] = i;
								prevMove[1] = k;
								prevMove[2] = 1;
								panel.revalidate();
								panel.repaint();
							}
						}
					}
				}
			}
		}
		
		public void compTurn(){
			int []move = new int[3];
				move = computer.analyseBoard(horizLineArray2, vertLineArray2, numFilled,prevMove[0],prevMove[1],prevMove[2]);
				System.out.println("completion: " + move[0] + " " + move[1] + " " + move[2] + " " +
						computer.completesSquare(move[0],move[1],move[2],horizLineArray2,vertLineArray2,false));
				if(!computer.completesSquare(move[0],move[1],move[2],horizLineArray2,vertLineArray2,false)){
					turn++;
				}else{ 
				}
				if(move[2] == 0){
					compFilled1[move[0]][move[1]] = true;
					horizLineArray2[move[0]][move[1]] = true;
				}else{
					compFilled2[move[0]][move[1]] = true;
					vertLineArray2[move[0]][move[1]] = true;
				}
				
				for(int i = 0; i < 3; i++){
					System.out.println("comp move: " + move[i]);
				}
				panel.revalidate();
				panel.repaint();
				
			}
	}
}
		