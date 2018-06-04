package logika;

public class Smer {
	
	private int[] x;
	private int[] y;
	
	public int[] getX() {
		return x;
	}

	public int[] getY() {
		return y;
	}

	public Smer(int[] x, int[] y) {
		this.x = x;
		this.y = y;
	}
	
	public static int dolzina() {
		return x.length;
	}
}
