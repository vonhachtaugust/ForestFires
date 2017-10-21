package ForestFires;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RankFrequencyPlot {
	final int timesteps = 10000;
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
      new RankFrequencyPlot().program();
  }

	void program() {
		while (timeStep < timesteps) {
			double density = rankFreq();
      if (density != -1) {
        comparedRankFreqPlot(density);
      }
		}
    // If this third-library is available ...
		//new XYScatterLogAxes(coords1, coords2);
	}

  // (TODO) General improvements can be made here
	public double rankFreq() {
		timeStep++;

		grid.getBurning().clear();
		grid.regrowth();
    double densBeforeFire = -1;

		if (grid.lightningStrike()) {
			densBeforeFire = grid.getGridDensity();

			int a = 1;
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
		}
    return densBeforeFire;
	}

	private void comparedRankFreqPlot(double density) {
    // (TODO) We dont need to construct a new grid every timestep
		Grid compareGrid = new Grid(row, col, density, p, f);

		List<Integer> pos = compareGrid.guaranteedStrike();
		Tree tree = compareGrid.getTree(pos.get(0), pos.get(1));
		tree.setState(1);
		compareGrid.getBurning().add(tree);

		int a = 1;
		int b = 0;
		int skip = 0;
		while (a != b) {
			a = compareGrid.getBurning().size();
			compareGrid.setOnFire(a - skip);
			b = compareGrid.getBurning().size();
			skip = b - a;
    }
    coords2.add((double) grid.getBurning().size() / grid.getGridSize());
    compareGrid.removeBurnt(compareGrid.getBurning());
    compareGrid.getBurning().clear();
	}
}
