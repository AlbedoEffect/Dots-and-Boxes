package frameCreation;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandling implements MouseListener {
	static MainGameplay gameFrame;
	
	
	public static void main(String args[]){
		gameFrame = new MainGameplay(5,5);
		gameFrame.panel.initializeGrid();
		//gameFrame.time.start();
	}

	@Override	
	public void mouseClicked(MouseEvent e) {
		if(MainGameplay.turn % 2 ==  0){
			gameFrame.panel.checkClick(e.getX(), e.getY());
		}else{
			System.out.println("comp turn");
			gameFrame.panel.compTurn();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
