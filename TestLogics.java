package Homeproblem3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestLogics {

	final static int row = 5;
	final static int col = 5;
	private static double p = 0.005;
	private static double f = 0.3;
	final static Random rand = new Random();
	private static Grid grid = new Grid(row, col, 0.5, p, f);

	public static void main(String[] args) {

		grid.regrowth();

		if (grid.lightningStrike()) {
			int a = grid.getBurning().size();
			int b = 0;
			int skip = 0;
			while (a != b) {

				for (int i = 0; i < row; i++) {
					for (int j = 0; j < col; j++) {
						Tree tree = grid.getTree(i, j);
						if (tree == null) {
							System.out.print("null" + "\t");
						} else {
							System.out.print(tree.getState() + "\t");
						}
					}
					System.out.println();
				}

				System.out.println();

				a = grid.getBurning().size();

				grid.setOnFire(a - skip);

				b = grid.getBurning().size();
				skip = b - a;
			}
			grid.removeBurnt(grid.getBurning());
		}

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				Tree tree = grid.getTree(i, j);
				if (tree == null) {
					System.out.print("null" + "\t");
				} else {
					System.out.print(tree.getState() + "\t");
				}
			}
			System.out.println();
		}

	}

}
