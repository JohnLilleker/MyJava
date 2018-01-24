import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import jb.GPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

@SuppressWarnings("serial")
public class Window extends JFrame {
	
	private GPanel panel;
	private boolean pause = false;
	private SnakeGame game;
	private JLabel score;
	
	public Window() {
		getContentPane().setBackground(Color.GRAY);
		setBackground(Color.WHITE);
		setSize(new Dimension(615, 675));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Snake");
		
		getContentPane().setLayout(null);
		
		panel = new GPanel(600, 600);
		panel.setFont(new Font("Calibri", Font.PLAIN, 15));

		//requestFocusInWindow();
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch(e.getExtendedKeyCode()) {
				
				case KeyEvent.VK_SPACE:
					pause = !pause;
					break;
					
				case KeyEvent.VK_UP:
					game.control(SnakeGame.Move.UP);
					break;
					
				case KeyEvent.VK_DOWN:
					game.control(SnakeGame.Move.DOWN);
					break;
					
				case KeyEvent.VK_LEFT:
					game.control(SnakeGame.Move.LEFT);
					break;
					
				case KeyEvent.VK_RIGHT:
					game.control(SnakeGame.Move.RIGHT);
					break;
				}
			}
		});
		panel.setBounds(5, 30, 600, 600);
		getContentPane().add(panel);
		
		score = new JLabel("Score : 0");
		score.setForeground(Color.YELLOW);
		score.setBounds(5, 5, 154, 14);
		getContentPane().add(score);
		
		game = new SnakeGame();
		setVisible(true);
		
		play();
	}
	
	public void play() {
		while(!game.gameOver()) {
			if (!pause) {
				game.update();
				score.setText("Score : " + game.points());
				panel.drawImage(game.view());
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] argv) {
		new Window();
	}
}
