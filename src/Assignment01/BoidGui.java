package Assignment01;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * @author Shane Birdsall
 * Student ID: 14870204
 */

public class BoidGui extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private JButton addBoid, removeBoid, clearScreen;
	private JSlider radiusDetectionSlider, seperationSlider, cohesionSlider, allignmentWeightSlider;
	private DrawingCanvas canvas;
	private BoidFlock boids = new BoidFlock(500);
	private Timer timer;

	public BoidGui() {
		super();
		setLayout(new BorderLayout());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) {}
		
		canvas = new DrawingCanvas();
		addBoid = new JButton(" Add ");
		removeBoid = new JButton(" Remove ");
		clearScreen = new JButton(" Clear All ");
		addBoid.addActionListener(this);
		removeBoid.addActionListener(this);
		clearScreen.addActionListener(this);
		
		radiusDetectionSlider = new JSlider();
		radiusDetectionSlider.addChangeListener(this);
		radiusDetectionSlider.setBorder(BorderFactory.createTitledBorder("Radius Detection"));

		seperationSlider = new JSlider();
		seperationSlider.addChangeListener(this);
		seperationSlider.setBorder(BorderFactory.createTitledBorder("Seperation"));

		cohesionSlider = new JSlider();
		cohesionSlider.addChangeListener(this);
		cohesionSlider.setBorder(BorderFactory.createTitledBorder("Cohesion"));
		
		allignmentWeightSlider = new JSlider();
		allignmentWeightSlider.addChangeListener(this);
		allignmentWeightSlider.setBorder(BorderFactory.createTitledBorder("Allignment Weight"));
		//Buttons
		JPanel panel = new JPanel();
		panel.add(addBoid);
		panel.add(removeBoid);
		panel.add(clearScreen);
		//Sliders
		panel.add(radiusDetectionSlider);
		panel.add(seperationSlider);
		panel.add(cohesionSlider);
		panel.add(allignmentWeightSlider);
		
		timer = new Timer(50, this);
		timer.start();
		add(panel, BorderLayout.SOUTH);
		add(canvas, BorderLayout.NORTH);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == addBoid) {
			boids.addBoidToFlock();
			System.out.println("addBoid BUTTON PRESSED");
		} else if (source == removeBoid) {
			boids.removeBoidFromFlock();
			System.out.println("removeBoid BUTTON PRESSED");
		} else if (source == clearScreen) {
			boids.destroyAllBoids();
			System.out.println("clearScreen BUTTON PRESSED");
		} else if (source == timer)
			canvas.repaint();
	}
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		
		if (source == radiusDetectionSlider) {
			BoidFlock.DETECTRADIUS = source.getValue()*10;
		} else if (source == seperationSlider) {
			Boid.SEPARATION_WEIGHT = 0.03f * source.getValue();
		} else if (source == cohesionSlider) {
			Boid.COHESION_WEIGHT = 0.0002f * source.getValue(); 
		} else if (source == allignmentWeightSlider) {
			Boid.ALIGNMENT_WEIGHT = 0.001f * source.getValue();
		}
	}
	
	class DrawingCanvas extends JPanel {
		private static final long serialVersionUID = 1L;
		public DrawingCanvas() {
			setPreferredSize(new Dimension(600, 600));
			setBackground(Color.GRAY);
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			// Draw all boids using graphics object
			if (boids.getBoidList() != null) {
				boids.drawAllBoids(g);
			}
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Boid GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new BoidGui());
		frame.pack(); // pack frame
		frame.setVisible(true); // show the frame
	}
}