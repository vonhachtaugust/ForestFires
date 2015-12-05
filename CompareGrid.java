package Homeproblem3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import Homeproblem3.Tree;

public class CompareGrid {

	final int row;
	final int col;
	final int gridSize;
	private double p = 0.005;
	private double f = 0.3;
	private double initialTreeProb;
	private double currentTreeDens;

	public static Random rand = new Random();
	private static Tree[][] grid;
	private List<Tree> burning = new ArrayList<>();
	// private List<Tree> treePositions = new ArrayList<>();

	public CompareGrid(int row, int col, double initialTreeProb, double p, double f) {
		this.row = row;
		this.col = col;
		this.gridSize = row * col;
		this.initialTreeProb = initialTreeProb;
		this.p = p;
		this.f = f;
		this.setGrid();
	}

	public void setGrid() {
		grid = new Tree[row][col];
		addTrees(row, col);
		this.grid = grid;
	}

	public Tree[][] getGrid() {
		return grid;
	}

	public Tree[][] addTrees(int row, int col) {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				double p = Math.random();
				if (p < initialTreeProb) {
					Tree tree = new Tree(i, j, 0);
					grid[i][j] = tree;
					updateGridDensity(1.0);
				} else {
					grid[i][j] = null;
				}
			}
		}
		return grid;
	}

	public Tree[][] regrowth() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				Tree tree = getTree(i, j);
				double r = Math.random();
				if (tree == null && r < p) {
					grid[i][j] = new Tree(i, j, 0);
					updateGridDensity(1.0);
				}
			}
		}
		return grid;
	}

	public List<Integer> guaranteedStrike() {
		List<Integer> pos = new ArrayList<>();
		int i = rand.nextInt(row);
		int j = rand.nextInt(col);

		//System.out.println("lightning strike in (" + i + "," + j + ")");
		//System.out.println("rec search");

		if (getTree(i, j) != null) {
			pos = (Arrays.asList(i, j));
			//System.out.println(pos);
		} else {
			pos = searchTree(grid, i, j);
			//System.out.println(pos);
		}
		return pos;
	}

	public boolean lightningStrike() { // occurs_at_random_site_with_prob_f
		double r = Math.random();
		int i = rand.nextInt(row);
		int j = rand.nextInt(col);

		if (r < f && grid[i][j] != null) {
			grid[i][j].setState(1);
			burning.add(grid[i][j]);
			return true;
		}
		return false;
	}

	private List<Integer> searchTree(Tree[][] grid, int i, int j) {
		List<List> toSearch = new ArrayList();
		List<List> searched = new ArrayList();

		toSearch.add(Arrays.asList(i, j));

		while (!toSearch.isEmpty()) {
			//System.out.println(toSearch);
			List<Integer> thisPos = toSearch.remove(0);
			int nextX = thisPos.get(0);
			int nextY = thisPos.get(1);

			//System.out.println("Currently checking" + "(" + nextX + "," + nextY + ")");
			if (grid[nextX][nextY] != null) {
				//System.out.println(grid[nextX][nextY].toString());
				return Arrays.asList(nextX, nextY);
			} else {
				searched.add(thisPos);

				// add neighbors to toSearch
				List<Integer> rightNeighbor = Arrays.asList(getIndRightOf(nextX), nextY);
				List<Integer> bottomNeighbor = Arrays.asList(nextX, getIndBelow(nextY));
				List<Integer> leftNeighbor = Arrays.asList(getIndLeftOf(nextX), nextY);
				List<Integer> topNeighbor = Arrays.asList(nextX, getIndAbove(nextY));
				List<List<Integer>> neighbors = Arrays.asList(rightNeighbor, bottomNeighbor, leftNeighbor, topNeighbor);

				for (int k = 0; k < neighbors.size(); k++) {
					List<Integer> thisNeighbor = neighbors.get(k);
					if (!searched.contains(thisNeighbor) && !toSearch.contains(thisNeighbor)) {
						toSearch.add(thisNeighbor);
					}
				}
			}
		}
		return Arrays.asList(-1, -1);
	}

	private static boolean cmp(List<?> l1, List<?> l2) {
		// make a copy of the list so the original list is not changed, and
		// remove() is supported
		ArrayList<?> cp = new ArrayList<>(l1);
		for (Object o : l2) {
			if (!cp.remove(o)) {
				return false;
			}
		}
		return cp.isEmpty();
	}

	public void setOnFire(int a) {
		int length = burning.size();
		for (int i = a - 1; i < length; i++) {
			Tree tree = burning.get(i);
			if (grid[getIndAbove(tree.getRow())][tree.getCol()] != null
					&& grid[getIndAbove(tree.getRow())][tree.getCol()].getState() != 1) {
				grid[getIndAbove(tree.getRow())][tree.getCol()].setState(1);
				burning.add(grid[getIndAbove(tree.getRow())][tree.getCol()]);

			}
			if (grid[getIndBelow(tree.getRow())][tree.getCol()] != null
					&& grid[getIndBelow(tree.getRow())][tree.getCol()].getState() != 1) {
				grid[getIndBelow(tree.getRow())][tree.getCol()].setState(1);
				burning.add(grid[getIndBelow(tree.getRow())][tree.getCol()]);
			}
			if (grid[tree.getRow()][getIndRightOf(tree.getCol())] != null
					&& grid[tree.getRow()][getIndRightOf(tree.getCol())].getState() != 1) {
				grid[tree.getRow()][getIndRightOf(tree.getCol())].setState(1);
				burning.add(grid[tree.getRow()][getIndRightOf(tree.getCol())]);
			}
			if (grid[tree.getRow()][getIndLeftOf(tree.getCol())] != null
					&& grid[tree.getRow()][getIndLeftOf(tree.getCol())].getState() != 1) {
				grid[tree.getRow()][getIndLeftOf(tree.getCol())].setState(1);
				burning.add(grid[tree.getRow()][getIndLeftOf(tree.getCol())]);
			}
		}
	}

	public void removeBurnt(List<Tree> burnt) {
		updateGridDensity((double) -burnt.size());
		for (Tree tree : burnt) {
			grid[tree.getRow()][tree.getCol()] = null;
		}
	}

	private void updateGridDensity(double numTreesDelta) {
		currentTreeDens += numTreesDelta / gridSize;
		if (currentTreeDens < Math.pow(10, -10)) {
			currentTreeDens = 0;
		}
	}

	public double getGridDensity() {
		return currentTreeDens;
	}

	public int getGridSize() {
		return gridSize;
	}

	public Tree getTree(int i, int j) {
		return grid[i][j];
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getIndAbove(int i) {
		return correctPos(i - 1, row);
	}

	public int getIndRightOf(int i) {
		return correctPos(i + 1, col);
	}

	public int getIndBelow(int i) {
		return correctPos(i + 1, row);
	}

	public int getIndLeftOf(int i) {
		return correctPos(i - 1, col);
	}

	private int correctPos(int i, int lim) {
		return (i + lim) % lim;
	}

	public List<Tree> getBurning() {
		return burning;
	}

}
