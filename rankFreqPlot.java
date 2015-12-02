package Homeproblem3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class rankFreqPlot {
	final int timesteps = 1000;
	public int timeStep = 0;
	public double initialTreeDens = 0.5;
	private List<Double> coords1 = new ArrayList();
	private List<Double> coords2 = new ArrayList();
	private double p = 0.05;
	private double f = 1;

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
	}

	public void rankFreq() {
		timeStep++;
		
		grid.getBurning().clear();
		grid.regrowth();

		if (grid.lightningStrike(false)) {
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
			coords1.add((double) grid.getBurning().size()/grid.getGridSize());
			coords2.add((double) comparedFreqPlot(densBeforeFire)/grid.getGridSize());
			grid.removeBurnt(grid.getBurning());
		}
	}
	
	private int comparedFreqPlot(double dens) {
		Grid lattice = new Grid(row,col,dens,p,f);
		lattice.lightningStrike(true);

		int a = grid.getBurning().size();
		int b = 0;
		int skip = 0;
		while (a != b) {
			a = grid.getBurning().size();
			grid.setOnFire(a - skip);
			b = grid.getBurning().size();
			skip = b - a;
		}
		
		return grid.getBurning().size();
	}
}
