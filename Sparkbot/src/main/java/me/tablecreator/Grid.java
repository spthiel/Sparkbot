package me.tablecreator;

public class Grid {

	private Cell[][] grid;
	private int start;

	public Grid(int width, int height, int start) {
		grid = new Cell[width][height+1];
		this.start = start;
	}

	public void putCell(String cell) {
		String[] args = cell.split(";(?!%)");
		try {

			int x = Integer.parseInt(args[0]);
			int y = Integer.parseInt(args[1]);
			String message = args[2];
			String hover = null;
			if(args.length > 3) {
				hover = args[3];
			}

			Cell cellToPut = new Cell(message,hover);
			putCell(x,y,cellToPut);

		} catch(Exception ignored) {
			ignored.printStackTrace();
		}
	}

	public void putCell(int x, int y, Cell cell) {
		grid[x][y] = cell;
	}

	public Cell[][] getGrid() {
		return grid;
	}

	public int getStart() {
		return start;
	}
}
