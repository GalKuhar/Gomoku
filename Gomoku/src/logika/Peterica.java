package logika;

public class Peterica {

	// Peterica predstavljena s seznamom koordinat
	public int[] x;
	public int[] y;
	
	public Peterica(int[] x, int y[]) {
		this.x = x;
		this.y = y;
	}
	
//	/*
//	 * Vrne barvo polj ce je peterica polna, drugace vrne null
//	 */
//	public Polje preveri(Plosca plosca) {
//		int steviloBelih = 0;
//		int steviloCrnih = 0;
//		for (int i = 0; i < Igra.PET; i++) {
//			if (plosca.element(x[i], y[i]) == Polje.BELI) {
//				steviloBelih++;
//			} else if (plosca.element(x[i], y[i]) == Polje.CRNI) {
//				steviloCrnih++;
//			}
//		}
//		if (steviloBelih == 5) {
//			return Polje.BELI;
//		} else if (steviloCrnih == 5) {
//			return Polje.CRNI;
//		} else {
//			return null;
//		}
//	}
	
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
