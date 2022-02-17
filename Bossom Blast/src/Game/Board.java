package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

public class Board extends JPanel{
	private static final int N = 7;
	private static final int M = 7;
	
	private EndGameListener endGameListener;
	private Image imgBlack1, imgBlack2, imgBlack3;
	private Image imgBlue1, imgBlue2, imgBlue3;
	private Image imgRed1, imgRed2, imgRed3;
	private Image imgGreen1, imgGreen2, imgGreen3;
	private static Cell matrix[][] = new Cell[N][M];
	private static int ddBlack[][] = new int[N][M];
	private static int ddBlue[][] = new int[N][M];
	private static int ddRed[][] = new int[N][M];
	private static int ddGreen[][] = new int[N][M];
	private static int ddTruocBlack[][] = new int[N][M];
	private static int ddTruocBlue[][] = new int[N][M];
	private static int ddTruocRed[][] = new int[N][M];
	private static int ddTruocGreen[][] = new int[N][M];
	private static String currentPlayer = Cell.EMPTY_VALUE;
	private static String truocCurrentPlayer = Cell.EMPTY_VALUE;
	private static boolean OK = false;

	public Board() {
		this.initMatrix();
		this.initDegree();
		
		addMouseListener(new MouseAdapter() {		
			
			@Override
			public void mousePressed(MouseEvent e) {
			
				super.mousePressed(e);
				int x = e.getX();
				int y = e.getY();
				
				//Game chưa bắt đầu thì return
				if(currentPlayer.equals(Cell.EMPTY_VALUE)) {
					return;
				}

				//Phát ra âm thanh
				soundClick();
				
				//Vẽ bảng
				paintBoard(x, y);
				
			}
		});
		
		//Ảnh
		imageSource();
		
	}
	
	private synchronized void soundClick() {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("Mousclik.wav"));
					clip.open(audioInputStream);
					clip.start();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	
	//Khởi tạo ma trận
	private void initMatrix(){
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				Cell cell = new Cell();
				matrix[i][j] = cell;
			}
		}
	
		matrix[0][0].setValue(Cell.BLACK_VALUE);
		matrix[0][M-1].setValue(Cell.BLUE_VALUE);
		matrix[N-1][M-1].setValue(Cell.RED_VALUE);
		matrix[N-1][0].setValue(Cell.GREEN_VALUE);
		
	}
		
	//Khởi tạo bậc của ma trận
	private void initDegree(){
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {	
				ddBlack[i][j] = 0;
				ddBlue[i][j] = 0;
				ddRed[i][j] = 0;
				ddGreen[i][j] = 0;
			}
		}
		
		ddBlack[0][0] = 1;
		ddBlue[0][M-1] = 1;
		ddRed[N-1][M-1] = 1;
		ddGreen[N-1][0] = 1;	
		
	}
	
	//Reset
	public void reset() {
		this.initMatrix();
		this.initDegree();
		this.setCurrentPlayer(Cell.EMPTY_VALUE);
		repaint();
	}
	
	//Check win
	public static boolean isWin(String player) {
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				Cell cell = matrix[i][j];
					if(!cell.getValue().equals(player) && !cell.getValue().equals(Cell.EMPTY_VALUE)) return false;
			}
		}
		return true;
	}
		
	//Check lose
	public static boolean isLose(String player) {
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				Cell cell = matrix[i][j];
				if(cell.getValue().equals(player)) return false;
			}
		}
		return true;
	}
	
	//Xem x, y rơi vào ô nào trong board rồi vẽ hình 
	public void paintBoard(int x, int y) {
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				Cell cell = matrix[i][j];
				int cXStart = cell.getX();
				int cYStart = cell.getY();	
				int cXEnd = cXStart + cell.getW();
				int cYEnd = cYStart + cell.getH();
					
				if(x>=cXStart && x<=cXEnd && y>=cYStart && y<=cYEnd) {
					if(cell.getValue().equals(currentPlayer)) {
						//Update mảng truocMatrix
						if(OK) updateMatrix();
						else OK = true;
						
						//Vẽ hình
						loang(i, j, currentPlayer);
						repaint();
						
						//Xét end game chưa
						boolean result = isWin(currentPlayer);
						endGameListener.end(currentPlayer, result);
						
						//Xét lượt player tiếp theo
						if(!result) {
							if(currentPlayer.equals(Cell.BLACK_VALUE)) {
								if(!isLose(Cell.BLUE_VALUE)) currentPlayer = Cell.BLUE_VALUE;
								else if(!isLose(Cell.RED_VALUE)) currentPlayer = Cell.RED_VALUE;
								else if(!isLose(Cell.GREEN_VALUE)) currentPlayer = Cell.GREEN_VALUE;
							}
							else if(currentPlayer.equals(Cell.BLUE_VALUE)) {
								if(!isLose(Cell.RED_VALUE)) currentPlayer = Cell.RED_VALUE;
								else if(!isLose(Cell.GREEN_VALUE)) currentPlayer = Cell.GREEN_VALUE;
								else if(!isLose(Cell.BLACK_VALUE)) currentPlayer = Cell.BLACK_VALUE;
							}
							else if(currentPlayer.equals(Cell.RED_VALUE)) {
								if(!isLose(Cell.GREEN_VALUE)) currentPlayer = Cell.GREEN_VALUE;
								else if(!isLose(Cell.BLACK_VALUE)) currentPlayer = Cell.BLACK_VALUE;
								else if(!isLose(Cell.BLUE_VALUE)) currentPlayer = Cell.BLUE_VALUE;
							}
							else if(currentPlayer.equals(Cell.GREEN_VALUE)) {
								if(!isLose(Cell.BLACK_VALUE)) currentPlayer = Cell.BLACK_VALUE;
								else if(!isLose(Cell.BLUE_VALUE)) currentPlayer = Cell.BLUE_VALUE;
								else if(!isLose(Cell.RED_VALUE)) currentPlayer = Cell.RED_VALUE;
							}
						}
					}
					break;
				}
			}
		}
	}
		
	//Loang ma trận
	private void loang(int x, int y, String currenPlayer) {
		int maxDegree = Math.max(Math.max(ddBlack[x][y], ddBlue[x][y]), Math.max(ddRed[x][y], ddGreen[x][y]));
		
		ddBlack[x][y] = 0;
		ddBlue[x][y] = 0;
		ddRed[x][y] = 0;
		ddGreen[x][y] = 0;
		
		if(currenPlayer == Cell.BLACK_VALUE) ddBlack[x][y] = maxDegree + 1;
		else if (currenPlayer == Cell.BLUE_VALUE) ddBlue[x][y] = maxDegree + 1;
		else if (currenPlayer == Cell.RED_VALUE) ddRed[x][y] = maxDegree + 1;
		else if (currenPlayer == Cell.GREEN_VALUE) ddGreen[x][y] = maxDegree + 1;
		
		Cell cell = matrix[x][y];
		cell.setValue(currenPlayer);
		
		//Nếu bậc = 4 thì đặt lại empty_value, bậc = 0, đệ quy 4 phía
		if(ddBlack[x][y] == 4 || ddBlue[x][y] == 4 || ddRed[x][y] == 4 || ddGreen[x][y] == 4) {
			cell.setValue(Cell.EMPTY_VALUE);
			
			if(ddBlack[x][y] == 4) ddBlack[x][y] = 0;
			else if(ddBlue[x][y] == 4) ddBlue[x][y] = 0;
			else if(ddRed[x][y] == 4) ddRed[x][y] = 0;
			else if(ddGreen[x][y] == 4) ddGreen[x][y] = 0;
			
			if(x >= 1) loang(x-1, y, currenPlayer);
			if(x <= M-2) loang(x+1, y, currenPlayer);
			if(y >= 1) loang(x, y-1, currenPlayer);
			if(y <= N-2) loang(x, y+1, currenPlayer);
		}
	}
	
	public static void updateMatrix() {
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				ddTruocBlack[i][j] = ddBlack[i][j];
				ddTruocBlue[i][j] = ddBlue[i][j];
				ddTruocRed[i][j] = ddRed[i][j];
				ddTruocGreen[i][j] = ddGreen[i][j];
			}
		}
		
		truocCurrentPlayer = currentPlayer;
	}
	
	public  void undoMatrix() {
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				if(ddTruocBlack[i][j] > 0) matrix[i][j].setValue(Cell.BLACK_VALUE);
				else if(ddTruocBlue[i][j] > 0) matrix[i][j].setValue(Cell.BLUE_VALUE);
				else if(ddTruocRed[i][j] > 0) matrix[i][j].setValue(Cell.RED_VALUE);
				else if(ddTruocGreen[i][j] > 0) matrix[i][j].setValue(Cell.GREEN_VALUE);
				
				ddBlack[i][j] = ddTruocBlack[i][j];
				ddBlue[i][j] = ddTruocBlue[i][j];
				ddRed[i][j] = ddTruocRed[i][j];
				ddGreen[i][j] = ddTruocGreen[i][j];
			}
		}
		
		currentPlayer = truocCurrentPlayer;
		repaint();
	}
	
	//Ảnh
	public void imageSource() {
		try{
			imgBlack1 = ImageIO.read(getClass().getResource("../Image/black1.png"));
			imgBlack2 = ImageIO.read(getClass().getResource("../Image/black2.png"));
			imgBlack3 = ImageIO.read(getClass().getResource("../Image/black3.png"));
			
			imgBlue1 = ImageIO.read(getClass().getResource("../Image/blue1.png"));
			imgBlue2 = ImageIO.read(getClass().getResource("../Image/blue2.png"));
			imgBlue3 = ImageIO.read(getClass().getResource("../Image/blue3.png"));
			
			imgRed1 = ImageIO.read(getClass().getResource("../Image/red1.png"));
			imgRed2 = ImageIO.read(getClass().getResource("../Image/red2.png"));
			imgRed3 = ImageIO.read(getClass().getResource("../Image/red3.png"));
			
			imgGreen1 = ImageIO.read(getClass().getResource("../Image/green1.png"));
			imgGreen2 = ImageIO.read(getClass().getResource("../Image/green2.png"));
			imgGreen3 = ImageIO.read(getClass().getResource("../Image/green3.png"));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		int w = getWidth()/M;
		int h = getHeight()/N;
		
		Graphics2D graphics2d = (Graphics2D) g;
		
		int k=0;
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				int x = j*w;
				int y = i*h;
				
				//Cập nhật lại ma trận
				Cell cell = matrix[i][j];
				cell.setX(x);
				cell.setY(y);
				cell.setW(w);
				cell.setH(h);
				
				Color color = k%2 == 0 ? Color.ORANGE : Color.LIGHT_GRAY;
				graphics2d.setColor(color);
				graphics2d.fillRect(x, y, w, h);
				
				if (ddBlack[i][j] == 1) graphics2d.drawImage(imgBlack1, x, y, w, h, this);
				else if (ddBlack[i][j] == 2) graphics2d.drawImage(imgBlack2, x, y, w, h, this);
				else if (ddBlack[i][j] == 3) graphics2d.drawImage(imgBlack3, x, y, w, h, this);

				if (ddBlue[i][j] == 1) graphics2d.drawImage(imgBlue1, x, y, w, h, this);
				else if (ddBlue[i][j] == 2) graphics2d.drawImage(imgBlue2, x, y, w, h, this);
				else if (ddBlue[i][j] == 3) graphics2d.drawImage(imgBlue3, x, y, w, h, this);
				
				if (ddRed[i][j] == 1) graphics2d.drawImage(imgRed1, x, y, w, h, this);
				else if (ddRed[i][j] == 2) graphics2d.drawImage(imgRed2, x, y, w, h, this);
				else if (ddRed[i][j] == 3) graphics2d.drawImage(imgRed3, x, y, w, h, this);
				
				if (ddGreen[i][j] == 1) graphics2d.drawImage(imgGreen1, x, y, w, h, this);
				else if (ddGreen[i][j] == 2) graphics2d.drawImage(imgGreen2, x, y, w, h, this);
				else if (ddGreen[i][j] == 3) graphics2d.drawImage(imgGreen3, x, y, w, h, this);
				
				k++;
			}
		}
	}

	public void setEndGameListener(EndGameListener endGameListener) {
		this.endGameListener = endGameListener;
	}
	
	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
}