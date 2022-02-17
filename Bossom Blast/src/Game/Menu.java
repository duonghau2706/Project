package Game;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class Menu extends JFrame implements ActionListener {

	private static CardLayout cardLayout = new CardLayout();
    private static Container container;
    
    private static JPanel jPanel = new JPanel();
    private static JButton btnNewGame = new JButton("New Game");
    private static JButton btnContinue = new JButton("Continue");
    private static JButton btnQuit = new JButton("Quit");
    
    private static JPanel jPanel2 = new JPanel();
	private static int sec = 0;	
	private static Timer timer = new Timer();
	private static JLabel lblTime = new JLabel("00:00");
	private static JButton btnStart = new JButton("Start");
	private static JButton btnSave = new JButton("Save");
	private static JButton btnUndo = new JButton("Undo");
	private static JButton btnMenu = new JButton("Menu");

	private static Board board = new Board();
	private static boolean isSave = false;
    
    public Menu() {
    	
        container = getContentPane();
        container.setLayout(cardLayout);
        container.setSize(700, 630);

        GridLayout gridLayout = new GridLayout(3, 1);
        gridLayout.setVgap(20);
        
        //Set jPanel
        jPanel.setLayout(gridLayout);
        jPanel.setBackground(Color.YELLOW);
        jPanel.setBorder(new EmptyBorder(new Insets(150,  200,  150,  200)));
        
        btnNewGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				stopGame();
			}
		});
        
        btnQuit.addActionListener(new ActionListener() {
        	
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
        
        btnNewGame.addActionListener(this);
        btnContinue.addActionListener(this);
        btnMenu.addActionListener(this);		
        		
        jPanel.add(btnNewGame);
        jPanel.add(btnContinue);
        jPanel.add(btnQuit);
        
        container.add(jPanel);
        
        //Set jPanel2
        board.setPreferredSize(new Dimension(630, 630));
		
		board.setEndGameListener(new EndGameListener() {
		
			@Override
			public void end(String player, boolean st) {
				if(st) {
					JOptionPane.showMessageDialog(null, "Người chơi " + player + " thắng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
					stopGame();
				}
			}
		});
		
		btnStart.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(btnStart.getText().equals("Start")) startGame();
				else stopGame();
			}
		});
			
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				isSave = true;
				JOptionPane.showMessageDialog(null, "Game đã được lưu", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		btnUndo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				board.undoMatrix();
			}
		});
		
		btnMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isSave) {
					int choose = JOptionPane.showConfirmDialog(null, "Bạn muốn lưu game không?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(choose == JOptionPane.NO_OPTION) stopGame();
					else JOptionPane.showMessageDialog(null, "Game đã được lưu", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				}
				else isSave = false;
			}
		});

		jPanel2.setBackground(Color.WHITE);
		jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.X_AXIS));
		
		JPanel leftPanel = new JPanel();
		leftPanel.add(board);
		leftPanel.setBackground(Color.BLACK);
		
		JPanel rightPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
		rightPanel.setLayout(boxLayout);
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setBorder(new EmptyBorder(new Insets(0, 50, 0, 50)));

		rightPanel.add(lblTime);
		rightPanel.add(btnStart);
		rightPanel.add(btnSave);
		rightPanel.add(btnUndo);
		rightPanel.add(btnMenu);
		
		jPanel2.add(leftPanel);
		jPanel2.add(rightPanel);
		
		container.add(jPanel2);
		
    }

    private static void startGame() {
    	String currentPlayer = Cell.BLACK_VALUE;
		
		board.reset();
		board.setCurrentPlayer(currentPlayer);
		
		//Đếm thời gian
		sec = 0;
		lblTime.setText("00:00");
		timer.cancel();
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				sec++;
				String value = "";
				if(sec < 600) value = '0' + value;
				if(sec % 60 >= 10) value += sec / 60 + " : " + sec % 60;
				else value += sec / 60 + " : " + '0' + sec % 60;
				lblTime.setText(value);
			}
		}, 1000, 1000);
		
		btnStart.setText("Stop");
		
	}
	
	private static void stopGame() {
		btnStart.setText("Start");
		sec = 0;
		lblTime.setText("00:00");
		timer.cancel();
		timer = new Timer();
		board.reset();
	}

	 // Action listener
    public void actionPerformed(ActionEvent e) {
        cardLayout.next(container);
    }
    
}