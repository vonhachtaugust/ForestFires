package Homeproblem3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class rankFreqPlot {
	final int timesteps = 40000;
	public int timeStep = 0;
	public double initialTreeDens = 0.5;
	private List<Double> coords1 = new ArrayList();
	private List<Double> coords2 = new ArrayList();
	private double p = 0.01;
	private double f = 0.3;

	public static final int row = 128;
	public static final int col = 128;
	private Grid grid = new Grid(row, col, initialTreeDens, p, f);

	public Random rand = new Random();

	public static void main(String[] args) {
		new rankFreqPlot().program();
	}

	void program() {
		while (timeStep < timesteps) {
			rankFreq();
		}
		new XYScatterLogAxes(coords1, coords2);
	}

	public void rankFreq() {
		timeStep++;

		grid.getBurning().clear();
		grid.regrowth();

		if (grid.lightningStrike()) {
			double densBeforeFire = grid.getGridDensity();

			int a = grid.getBurning().size();
			int b = 0;
			int skip = 0;
			while (a != b) {
				a = grid.getBurning().size();
				grid.setOnFire(a - skip);
				b = grid.getBurning().size();
				skip = b - a;
			}
			coords1.add((double) grid.getBurning().size() / grid.getGridSize());
			grid.removeBurnt(grid.getBurning());
			grid.getBurning().clear();
			double val = comparedFreqPlot(densBeforeFire);
			coords2.add(val / grid.getGridSize());
		}
	}

	private double comparedFreqPlot(double dens) {

		CompareGrid lattice = new CompareGrid(row, col, dens, p, f);

		List<Integer> pos = lattice.guaranteedStrike();
		Tree tree = lattice.getTree(pos.get(0), pos.get(1));
		tree.setState(1);
		lattice.getBurning().add(tree);

		int a = lattice.getBurning().size();
		int b = 0;
		int skip = 0;
		while (a != b) {
			a = lattice.getBurning().size();
			lattice.setOnFire(a - skip);
			b = lattice.getBurning().size();
			skip = b - a;
		}
		return lattice.getBurning().size();
	}
}
