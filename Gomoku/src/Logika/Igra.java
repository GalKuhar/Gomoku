package Logika;

import java.util.LinkedList;
import java.util.List;

public class Igra {

	// velikost igralne plo��e
	public static final int N = 15;
	
	private static final List<Peterica> peterice = new LinkedList<Peterica>();
	
	// dol�ina peterice
	public static final int PET = 5;
	
	private Polje[][] plosca;
	private Igralec prviNaPotezi;
	
	{	// Inicializiramo Peterice
		
		// Naredimo tabelo peteric
		// smer navzdol desno +
		int[][] smer = {{1,0}, {0,1}, {1,1}, {1,-1}};
		for (int x = 0; x < N; x++) {
			for (int y = 0; y < N; y++) {
				for (int[] s : smer) {
					int dx = s[0];
					int dy = s[1];
					if ((0 <= x + (PET-1) * dx) && (x + (PET-1) * dx < N) &&
						(0 <= y + (PET-1) * dy) && (y + (PET-1) * dy < N)) {
						int[] peterica_x = new int[5];
						int[] peterica_y = new int[5];
						for (int k = 0; k < PET; k++) {
							peterica_x[0] = x + dx * k;
							peterica_y[1] = y + dy * k;
						}
						peterice.add(new Peterica(peterica_x, peterica_y));
					}
				}
			}
		}
	}
	
	public Igralec naPotezi() {
		int steviloBelih = 0;
		int steviloCrnih = 0;
		for (int i = 0 ; i < Igra.N ; i++) {
			for (int j = 0 ; j < Igra.N ; j++) {
				if (plosca[i][j] == Polje.BELI) {
					steviloBelih++;
				} else if (plosca[i][j] == Polje.CRNI) {
					steviloCrnih++;
				}
			}
		}
		if (steviloBelih == steviloCrnih) {
			return prviNaPotezi;
		} else if (steviloBelih == steviloCrnih + 1 && prviNaPotezi == Igralec.BELI){
			return Igralec.CRNI;
		} else if (steviloBelih + 1 == steviloCrnih && prviNaPotezi == Igralec.CRNI){
			return Igralec.BELI;
		} else {
		// �e pride do konca in ne najde, kdo je na potezi, vrne null
			return null;
		}
	}
}
