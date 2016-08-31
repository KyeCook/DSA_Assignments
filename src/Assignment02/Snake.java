package Assignment02;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
/**
 * @author Shane Birdsall
 */
public class Snake implements Runnable {
	private DequeADT<Segment> segments;
	public boolean alive;
	private int x, y;
	private static final int SIZE = 20; // might want to change (size of snake)
	private int movement;
	public enum Direction{U, D, L, R}
	private Direction direction;
	int currentRating = 0; // Added this based on my own choice
	public int SLEEP = 50;

	public Snake(int panelWidth, int panelHeight) {
		alive = true;
		segments = new DoublyLinkedDeque<>();
		direction = Direction.U; // Start going up by default
		movement = 5; // Might want to play with this value
		x = panelWidth/2;
		y = panelHeight/2;
		segments.enqueueFront(new Segment(x,y,SIZE,Color.BLUE));
	}

	public void run() {
		alive = true;
		while(isAlive()) {
			try {
				moveSnake();
				Thread.sleep(SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private synchronized void moveSnake() {
		if(x < SnakeGameGui.PANEL_WIDTH && x > 0 && y < SnakeGameGui.PANEL_HEIGHT && y > 0) {
			Iterator<Segment> iterator = segments.iterator(false);
			Segment current = iterator.next(), next;
			while(iterator.hasNext()) {
				next = iterator.next();
				current.setX(next.getX());
				current.setY(next.getY());
				current = next;
			}
			if(direction == Direction.U) {
				y -= movement;
				segments.first().setY(y);
			} else if(direction == Direction.D) {
				y += movement;
				segments.first().setY(y);
			} else if(direction == Direction.L) {
				x -= movement;
				segments.first().setX(x);
			} else if(direction == Direction.R) {
				x += movement;
				segments.first().setX(x);
			}
		} else {
			killSnake();
		}
	}
	public boolean isAlive(){
		return alive;
	}
	public void killSnake() {
		alive = false;
	}
	public int getXPosition() {
		return x;
	}
	public int getYPosition() {
		return y;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public boolean checkIfSnakeHit(Food food) {
		Iterator<Segment> iterator = segments.iterator(true);
		Segment current;
		int range;
		while(iterator.hasNext()) {
			current = iterator.next();
			range = ((current.getX() - food.getX())*(current.getX() - food.getX())) 
					+ ((current.getY() - food.getY())*(current.getY() - food.getY()));
			if(range < ((food.getSize()*food.getSize()) + (Snake.SIZE*Snake.SIZE))/3) {
				return true;
			}
		} return false;	// Not within range with any segments
	}
	public synchronized void eatFoodIfInRange(Food food) {
		int range = ((segments.first().getX() - food.getX())*(segments.first().getX() - food.getX())) 
				+ ((segments.first().getY() - food.getY())*(segments.first().getY() - food.getY()));
		if(range < ((food.getSize()*food.getSize()) + (Snake.SIZE*Snake.SIZE))/3) {
			for(int i = 1; i <= food.getRating()+1; i++) {
				segments.enqueueFront(new Segment(x,y,SIZE,food.getColour()));
				food.killFood();
			} currentRating = food.getRating()+1;
		}		
	}
	public synchronized void drawSnake(Graphics g) {
		Iterator<Segment> iterator = segments.iterator(false);
		while(iterator.hasNext()) {
			iterator.next().drawSegment(g);
		}
	}

}
