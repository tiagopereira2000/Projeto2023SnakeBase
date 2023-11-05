package environment;

/** Classe representing a position on the board, with some utilities
 * 
 * @author luismota
 *
 */

public class BoardPosition {
	public final int x;
	public final int y;

	public BoardPosition(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		BoardPosition other = (BoardPosition) obj;
		return other.x==x && other.y == y;
	}
	
	public double distanceTo(BoardPosition other) {
		double delta_x = y - other.y;
		double delta_y = x - other.x;
		return Math.sqrt(delta_x * delta_x + delta_y * delta_y);
	}

	public BoardPosition getCellAbove() {
		return new BoardPosition(x, y-1);
	}
	public BoardPosition getCellBelow() {
		return new BoardPosition(x, y+1);
	}
	public BoardPosition getCellLeft() {
		return new BoardPosition(x-1, y);
	}
	public BoardPosition getCellRight() {
		return new BoardPosition(x+1, y);
	}
}
