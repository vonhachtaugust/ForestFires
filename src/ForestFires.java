package ForestFires;

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

import java.lang.Thread;

public class ForestFires extends JPanel implements ActionListener {

	final double timeFactor = 100;
	final int nrOfStrikesPerGrownTree = 10;
	final int width = 768;
	final int height = 768;
	final int timesteps = 1000000;

  public static int timeStep = 0;
  public static double initialTreeDens = 0.5;
  public static final int row = 128;
  public static final int col = 128;
  public static Random rand = new Random();

	private double p = timeFactor * (1.0 / (row * col));
	private double f = timeFactor * (1.0 / nrOfStrikesPerGrownTree);
	private Grid grid = new Grid(row, col, initialTreeDens, p, f);
  private JButton a = new JButton("Start");
  private JButton b = new JButton("Stop");
  private Timer t = new Timer(10, this);

  public static void main(String[] args) {
      new ForestFires().program();
      // new XYLogAxes(); // If library available
  }


	private void program() {
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

		//if (grid.getGridDensity() * (row * col) < 0.4 * (row * col)) {
		//	t.stop();
		//	System.out.println(grid.getGridDensity() * (row * col) + ">" + 0.3 * (row * col));
		//}

		if (timeStep > timesteps) {
			t.stop();
			// new XYLineChart(row * col);
		}
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      System.err.println("Caught InterruptedException: " + e.getMessage());
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

	private void initGraphics() {

		setPreferredSize(new Dimension(width, height));
		JFrame window = new JFrame("Title");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    window.setLayout(new BorderLayout());
		window.add(this, BorderLayout.CENTER);

		JPanel p = new JPanel();
    a.addActionListener(this);
		p.add(a);
		b.addActionListener(this);
		p.add(b);
    window.add(p, BorderLayout.SOUTH);

    window.pack();
    window.setLocationRelativeTo(null);
		window.setVisible(true);
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

	private void initEvent() {
		t.setInitialDelay(500);
	}
}

