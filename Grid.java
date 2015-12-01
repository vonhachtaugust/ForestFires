package Homeproblem3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Homeproblem3.Tree;

public class Grid {

	final int row;
	final int col;
	final double p = 0.005;
	final double f = 0.3;
	private static final double initialTreeProb = 0.5;
	public static Random rand = new Random();
	private static Tree[][] grid;
	private List<Tree> burning = new ArrayList<>();

	public Grid(int row, int col) {
		this.row = row;
		this.col = col;
		grid = new Tree[row][col];
		addTrees(row, col);
	}

	public static Tree[][] addTrees(int row, int col) {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				double p = Math.random();
				if (p < initialTreeProb) {
					Tree tree = new Tree(i, j, 0);
					grid[i][j] = tree;
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
				} else {
					;
				}
			}
		}
		return grid;
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
		for (Tree tree : burnt) {
			grid[tree.getRow()][tree.getCol()] = null;
		}
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
