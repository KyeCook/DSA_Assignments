package Assignment02;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author sehall, Shane Birdsall
 */
public class SnakeGameGui extends JPanel implements ActionListener,KeyListener, ChangeListener {

	private static final long serialVersionUID = 1L;
	public final static int PANEL_WIDTH = 800;
	public final static int PANEL_HEIGHT = 800;
	public final static int NUM_FOOD = 15;
	private static int SLEEP = 50;
	public Timer timer;
	private DrawPanel drawPanel;
	private JButton restartButton, quitButton, pauseButton;
	private JSlider difficultySlider;
	private JLabel scoreLabel;
	private static JFrame frame;
	private DoublyLinkedDeque<Food> foodCollection;
	private Snake snake;
	private boolean paused = false;
	private int score;

	public SnakeGameGui() {
		super(new BorderLayout());
		drawPanel = new DrawPanel();
		timer = new Timer(SLEEP,this);

		JPanel sliderPanel = new JPanel();
		restartButton = new JButton("Restart");
		restartButton.addActionListener(this);
		quitButton = new JButton("Quit");
		quitButton.addActionListener(this);
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(this);
		difficultySlider = new JSlider(0,45,0);
		difficultySlider.addChangeListener(this);
		difficultySlider.setBorder(BorderFactory.createTitledBorder("Difficulty"));
		scoreLabel = new JLabel("Score: " +score+"     ");

		sliderPanel.add(scoreLabel);
		sliderPanel.add(restartButton);
		sliderPanel.add(quitButton);
		sliderPanel.add(pauseButton);
		sliderPanel.add(difficultySlider);
		add(drawPanel,BorderLayout.CENTER);
		add(sliderPanel,BorderLayout.SOUTH);

		foodCollection = new DoublyLinkedDeque<>();
		for(int i = 0; i < NUM_FOOD; i++) {
			foodCollection.enqueueRear(new Food(PANEL_WIDTH, PANEL_HEIGHT, i));
		}
		snake = new Snake(PANEL_WIDTH, PANEL_HEIGHT);
		Iterator<Food> foodIterator = foodCollection.iterator(true);
		while(foodIterator.hasNext()) {
			new Thread(foodIterator.next()).start();
		}
		new Thread(snake).start();
		score = 0;
		timer.start();
	}

	private class DrawPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		public DrawPanel() {
			super();
			setPreferredSize(new Dimension(PANEL_WIDTH ,PANEL_HEIGHT));
			setBackground(Color.WHITE);
		}
		//can be used to draw the snake and food
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Iterator<Food> food = foodCollection.iterator(true);
			while(food.hasNext()) {
				food.next().drawFood(g);
			}
			snake.drawSnake(g);
		}
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		Iterator<Food> foodIterator;
		Food currentFood;

		if(source == timer) {
			drawPanel.repaint();
			if(!foodCollection.isEmpty() && snake.isAlive()) {
				foodIterator = foodCollection.iterator(true); // Reset each time
				currentFood = foodIterator.next();
				snake.eatFoodIfInRange(currentFood); // Eat lowest rated food if it is in range
				if(!currentFood.isAlive()) {	// Remove food if it is eaten and add to score
					foodCollection.dequeueFront();
					score += 10;
					scoreLabel.setText("Score: " +score+"     ");
				} else { // Else enforcers that the first food wont be checked in the below statements
					while(foodIterator.hasNext()) { // loop through all food left alive
						currentFood = foodIterator.next();
						if(snake.checkIfSnakeHit(currentFood)) { //Checks if snake is hit
							snake.killSnake();
						}
					}
				}
			} else if(foodCollection.isEmpty() && snake.isAlive()) {
				JOptionPane.showMessageDialog(drawPanel, "Good Job! You Won!",
						"SNAKE GAME" , JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			} else if(!foodCollection.isEmpty() && !snake.isAlive()) {
				int result = JOptionPane.showConfirmDialog(drawPanel, "OH NO! THE SNAKE HAS DIED!\nDo you want to play again?", 
						"SNAKE GAME" , JOptionPane.INFORMATION_MESSAGE);
				if(result == 0) { // Yes
					restart();
				} else if(result == 1) { // No
					System.exit(0);
				} else {
					foodCollection.clear();
				}
				//JOptionPane.showMessageDialog(drawPanel, "OH NO! THE SNAKE HAS DIED!",
				//		"SNAKE GAME" , JOptionPane.INFORMATION_MESSAGE);

			}
		} 
		if(source == quitButton) {   
			System.exit(0);
		}
		if(source == restartButton) { 
			restart();
		}
		if(source == pauseButton) {
			pauseGame();
			frame.setFocusable(true);
			frame.requestFocusInWindow();
		}
	}
	private void restart() {
		Iterator<Food> foodIterator;
		foodCollection = new DoublyLinkedDeque<>();
		for(int i = 0; i < NUM_FOOD; i++) {
			foodCollection.enqueueRear(new Food(PANEL_WIDTH, PANEL_HEIGHT, i));
		}
		snake = new Snake(PANEL_WIDTH, PANEL_HEIGHT);
		foodIterator = foodCollection.iterator(true);
		while(foodIterator.hasNext()) {
			new Thread(foodIterator.next()).start();
		}
		new Thread(snake).start();
		difficultySlider.setValue(0);
		score = 0;
		scoreLabel.setText("Score: " +score+"     ");
		frame.setFocusable(true);
		frame.requestFocusInWindow();	
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if (source == difficultySlider) {
			snake.SLEEP = 50 - source.getValue();
		}
		frame.setFocusable(true);
		frame.requestFocusInWindow();
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {   
			snake.setDirection(Snake.Direction.U);
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {  
			snake.setDirection(Snake.Direction.D);
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) { 
			snake.setDirection(Snake.Direction.L);
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {  
			snake.setDirection(Snake.Direction.R);
		} else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			pauseGame();
		}
	}
	private void pauseGame() {
		paused = !paused;
		Iterator<Food> foodIterator;
		Food food;
		foodIterator = foodCollection.iterator(true);
		while(foodIterator.hasNext()) {
			food = foodIterator.next();
			food.alive = !food.alive;
		}
		snake.alive = !snake.alive;
		if(paused) {
			timer.stop();
		} else {
			timer.start();
			foodIterator = foodCollection.iterator(true);
			while(foodIterator.hasNext()) {
				new Thread(foodIterator.next()).start();
			}
			new Thread(snake).start();
		}
	}
	public void keyTyped(KeyEvent e) {
		//IGNORE
	}
	public void keyReleased(KeyEvent e) {
		//IGNORE
	}
	public static void main(String[] args) {
		//System.out.println("============SNAKE===============");
		SnakeGameGui game = new SnakeGameGui();
		frame = new JFrame("SNAKE GAME GUI");
		frame.setFocusable(true);
		//add a keylistener
		frame.addKeyListener(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(game);
		//gets the dimensions for screen width and height to calculate center
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		int screenHeight = dimension.height;
		int screenWidth = dimension.width;
		frame.pack(); //resize frame apropriately for its content
		//positions frame in center of screen
		frame.setLocation(new Point((screenWidth/2)-(frame.getWidth()/2),
				(screenHeight/2)-(frame.getHeight()/2)));
		frame.setVisible(true);
	}
}
