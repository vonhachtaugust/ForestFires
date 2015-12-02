package Homeproblem3;

public class Tree {

	private int row;
	private int col;
	private int state;
	private boolean visited;

	public Tree(int row, int col, int state) {
		this.setRow(row);
		this.setCol(col);
		this.setState(state);

	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String toString() {
		return "Tree"/*
						 * "Tree{" + "row=" + row + "," + "col=" + col +
						 * ", state=" + state + '}'
						 */;
	}

}
