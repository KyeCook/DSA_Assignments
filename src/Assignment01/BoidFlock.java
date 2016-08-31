package Assignment01;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * @author Shane Birdsall
 * Student ID: 14870204
 */
public class BoidFlock {
	public static int DETECTRADIUS = 100;
	public static Random rand = new Random();
	private List<Boid> boidList;
	
	public BoidFlock() {
		this(0);
	}
	public BoidFlock(int numBoidsToStart) {
		boidList = new ArrayList<Boid>();
		for(int i = 0; i < numBoidsToStart; i++) {
			addBoidToFlock();
		}
	}
	public void addBoidToFlock() {
		int boundX = 600; //not sure on bounds yet
		int boundY = 600;
		float xPos = rand.nextInt(boundX);	
		float yPos = rand.nextInt(boundY);
		Boid newBoid = new Boid(this, xPos, yPos, Boid.MAX_SPEED, Boid.MAX_SPEED, boundX, boundY);
		boidList.add(newBoid);
		new Thread(newBoid).start();
	}
	public void removeBoidFromFlock() {
		int num = getNumberOfBoids();
		if(!boidList.isEmpty()) {
			boidList.get(num-1).requestStop(); //Stop thread
			boidList.remove(num-1); //Remove boid from list
			System.out.println("removed");
		}
	}
	public void drawAllBoids(Graphics g) {
		for(int i = 0; i < boidList.size(); i++) {
			boidList.get(i).drawShip(g); //Loop until all boids are drawn
		}
	}
	public int getNumberOfBoids() {
		return boidList.size();
	}
	public void destroyAllBoids() {
		while(!boidList.isEmpty()) {
			removeBoidFromFlock();
		}
	}
	public List<Boid> getNeighbours(Boid boidToTest) {
		//Create a list to contain any neighbouring boids
		List<Boid> neighbours = new ArrayList<>();

		Boid tempBoid;
		//Loop through all existing boids to check if they are neighbours
		for(int i = 0; i < boidList.size(); i++) {
			tempBoid = boidList.get(i);
			float expression = (tempBoid.getxPos() - boidToTest.getxPos()) + (tempBoid.getyPos() - boidToTest.getyPos());
			if(expression < DETECTRADIUS*DETECTRADIUS && expression != 0) {	// Not equal to zero stops a boid neighbouring itself.
				neighbours.add(tempBoid);
			}
		}
		return neighbours;
	}
	public List<Boid> getBoidList() {
		return boidList;
	}
}
