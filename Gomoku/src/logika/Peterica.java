package logika;

public class Peterica {

	// Peterica predstavljena s seznamom koordinat
	public int[] x;
	public int[] y;
	
	public Peterica(int[] x, int y[]) {
		this.x = x;
		this.y = y;
	}
	
	// ta metoda hitrejsa, saj ni treba do konca gledati peterice - takoj ko najdes eno polje drugacno lahko koncas.
	/**
	 * 
	 * @param plosca
	 * @param polje
	 * @return Vrne ali je peterica v celoti dane barve.
	 */
	public boolean vseBarve(Plosca plosca, Polje polje) {
		for (int i = 0; i < Igra.PET; i++) {
			if (plosca.element(x[i], y[i]) != polje) {
				return false;
			}
		}
		return true;
	}
	
}
