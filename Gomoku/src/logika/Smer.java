package logika;

public class Smer {
	
	private static int[] x;
	private static int[] y;
	
	public int[] getX() {
		return x;
	}

	public int[] getY() {
		return y;
	}

	public Smer(int[] x, int[] y) {
		Smer.x = x;
		Smer.y = y;
	}
	
	public static int dolzina() {
		return x.length;
	}
}
