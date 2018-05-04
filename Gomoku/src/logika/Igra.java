package logika;

import java.util.LinkedList;
import java.util.List;

public class Igra {

	// velikost igralne plosce
	public static final int N = 15;
	
	public static final List<Peterica> peterice = new LinkedList<Peterica>();
	
	// dolzina peterice
	public static final int PET = 5;
	
	protected Plosca plosca;
	private static Igralec prviNaPotezi;
	
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
						int[] peterica_x = new int[PET];
						int[] peterica_y = new int[PET];
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
	
	public Igra() {
		plosca = new Plosca();
		setPrviNaPotezi(Igralec.CRNI);
	}

	public Igralec getPrviNaPotezi() {
		return prviNaPotezi;
	}

	public void setPrviNaPotezi(Igralec prviNaPotezi) {
		Igra.prviNaPotezi = prviNaPotezi;
	}

	/**
	 * @return Vrne stanje igre
	 */
	public Stanje stanje() {
		plosca.prestej();
		int steviloPolnihPeteric = 0;
		Igralec zmagovalec = null;
		
		for (Peterica peterica : Igra.peterice) {
			if (peterica.vseBarve(plosca, Polje.BELI)) {
				zmagovalec = Igralec.BELI;
				steviloPolnihPeteric++;
			} else if (peterica.vseBarve(plosca, Polje.CRNI)) {
				zmagovalec = Igralec.CRNI;
				steviloPolnihPeteric++;
			}
		}

		// ce je prevec polnih peteric, je nekaj narobe
		if (steviloPolnihPeteric > 1) {
			return Stanje.IGRA_NI_VELJAVNA;
		}
		
		if (zmagovalec == Igralec.BELI) {
			return Stanje.BELI_ZMAGA;
		} else if (zmagovalec == Igralec.CRNI) {
			return Stanje.CRNI_ZMAGA;
		}
		
		if (plosca.steviloBelih + plosca.steviloCrnih == Igra.N * Igra.N) {
			return Stanje.NEODLOCENO;
		}
		
		if (plosca.steviloBelih == plosca.steviloCrnih) {
			if (getPrviNaPotezi() == Igralec.BELI) {
				return Stanje.BELI_NA_POTEZI;
			} else {
				return Stanje.CRNI_NA_POTEZI;
			}
		} else if (plosca.steviloBelih == plosca.steviloCrnih + 1 && getPrviNaPotezi() == Igralec.BELI){
			return Stanje.CRNI_NA_POTEZI;
		} else if (plosca.steviloBelih + 1 == plosca.steviloCrnih && getPrviNaPotezi() == Igralec.CRNI){
			return Stanje.BELI_NA_POTEZI;
		} else {
			// ce pride do konca in ne najde kdo na potezi, mora biti nekaj narobe
			return Stanje.IGRA_NI_VELJAVNA;
		}
	}
	
	public LinkedList<Poteza> moznePoteze() {
		LinkedList<Poteza> moznosti = new LinkedList<Poteza>();
		for (int x = 0; x < Igra.N; x ++) {
			for (int y = 0; y < Igra.N; y ++) {
				if (plosca.element(x, y) == Polje.PRAZNO) {
					moznosti.add(new Poteza(x, y));
				}
			}
		}
		return moznosti;
	}

	public boolean odigrajPotezo(Poteza poteza) {
		int x = poteza.getX();
		int y = poteza.getY();
		if (plosca.element(x, y) == Polje.PRAZNO) {
			// ce se bo poklicala ta funkcija, bo nekdo od igralcev na vrsti
			Stanje trenutnoStanje = stanje();
			if (trenutnoStanje == Stanje.BELI_NA_POTEZI) {
				plosca.elementSet(x, y, Polje.BELI);
				return true;
			} else if (trenutnoStanje == Stanje.CRNI_NA_POTEZI) {
				plosca.elementSet(x, y, Polje.CRNI);
				return true;
			} 
		}
		return false;
	}

}