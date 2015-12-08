package Homeproblem3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class ForestFires extends JPanel implements ActionListener {

	// 100 träd per tidsteg multiplicerat med tid factor.

	final double timeFactor = 100;
	final int nrOfStrikesPerGrownTree = 10;

	final int width = 768;
	final int height = 768;
	final int timesteps = 1000000;
	public int timeStep = 0;
	public double initialTreeDens = 0.5;
	private double p = timeFactor * (1.0 / (row * col));
	private double f = timeFactor * (1.0 / nrOfStrikesPerGrownTree);

	public static final int row = 128;
	public static final int col = 128;
	private Grid grid = new Grid(row, col, initialTreeDens, p, f);

	public Random rand = new Random();

	public static void main(String[] args) {
		new ForestFires().program();
		// new XYLogAxes();
	}

	void program() {
		initEvent();
		initGraphics();
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		timeStep++;

		grid.getBurning().clear();
		grid.regrowth();

		if (grid.lightningStrike()) {
			int a = grid.getBurning().size();
			int b = 0;
			int skip = 0;
			while (a != b) {
				a = grid.getBurning().size();
				grid.setOnFire(a - skip);
				b = grid.getBurning().size();
				skip = b - a;
			}
			for (Tree tree : grid.getBurning()) {
				g.setColor(getStateColor(tree.getState()));
				g.fillRect(getTreePosX(tree, width / col), getTreePosY(tree, height / row), width / col, height / row);
			}
			grid.removeBurnt(grid.getBurning());
		}
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				Tree tree = grid.getTree(i, j);
				if (tree != null) {
					g.setColor(getStateColor(tree.getState()));
					g.fillRect(getTreePosX(tree, width / col), getTreePosY(tree, height / row), width / col,
							height / row);
				}
			}
		}

		if (grid.getGridDensity() * (row * col) < 0.4 * (row * col)) {
			t.stop();
			System.out.println(grid.getGridDensity() * (row * col) + ">" + 0.3 * (row * col));

		}

		if (timeStep > timesteps) {
			t.stop();
			// new XYLineChart(row * col);
		}

	}

	public Color getStateColor(int s) {
		Color c;
		if (s == 1) {
			c = new Color(255, 0, 0);
		} else {
			c = new Color(0, 255, 0);
		}
		return c;
	}

	private int getTreePosX(Tree p, int cellWidth) {
		return (p.getCol()) * cellWidth;
	}

	private int getTreePosY(Tree p, int cellHeight) {
		return (p.getRow()) * cellHeight;
	}

	void initGraphics() {

		setPreferredSize(new Dimension(width, height));
		JFrame window = new JFrame("Title");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
		window.add(this, BorderLayout.CENTER);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		JPanel p = new JPanel();

		a.addActionListener(this);
		p.add(a);

		b.addActionListener(this);
		p.add(b);

		window.add(p, BorderLayout.SOUTH);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == a) {
			System.out.println("Go");
			t.start();
		}

		if (e.getSource() == b) {
			System.out.println("Stop");
			t.stop();
			// new XYLineChart(row * col);
			System.out.println("Ended at timestep :\t" + timeStep);
		}

		repaint();
	}

	JButton a = new JButton("Start");
	JButton b = new JButton("Stop");
	Timer t = new Timer(10, this);

	private void initEvent() {
		t.setInitialDelay(500);
	}
}
