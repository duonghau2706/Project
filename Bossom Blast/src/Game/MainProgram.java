package Game;

import javax.swing.JFrame;

public class MainProgram { 
	
	public static void main(String[] args) {
		
		Menu menu = new Menu();
		menu.setTitle("Bossom Blast");
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menu.pack();
		menu.setLocationRelativeTo(null);
		menu.setResizable(false);
		menu.setVisible(true);
		
	}

}
