package logika;

import java.util.LinkedList;
import java.util.List;

public class Igra {

	// velikost igralne plosce
	private static final int N = 15;
	
	public static int getN() {
		return N;
	}
	
	protected static final List<Peterica> peterice = new LinkedList<Peterica>();
	
	public static List<Peterica> getPeterice() {
		return peterice;
	}
	
	// dolzina peterice
	protected static final int PET = 5;
	
	public static int getPET() {
		return PET;
	}
	
	private Plosca plosca;
	private static Igralec prviNaPotezi;
	
	private Poteza zadnjaPoteza = null;
	
	private Peterica zmagovalnaPeterica = null;
	
	static {
		// Inicializiramo Peterice
		
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
							peterica_x[k] = x + dx * k;
							peterica_y[k] = y + dy * k;
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
		// najprej preveri ali je kdo že zmagal
		for (Peterica peterica : Igra.peterice) {
			if (peterica.vseBarve(plosca, Polje.BELI)) {
				zmagovalnaPeterica = peterica;
				return Stanje.BELI_ZMAGA;
			} else if (peterica.vseBarve(plosca, Polje.CRNI)) {
				zmagovalnaPeterica = peterica;
				return Stanje.CRNI_ZMAGA;
			}
		}
		
		// nato preveri kdo je na potezi
		plosca.prestej();
		
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
	
	/**
	 * @param igra nova kopija dane igre
	 */
	public Igra(Igra igra) {
		plosca = new Plosca();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca.elementSet(i, j, igra.plosca.element(i, j));
			}
		}
	}

	public Plosca getPlosca() {
		return plosca;
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
				zadnjaPoteza = poteza;
				return true;
			} else if (trenutnoStanje == Stanje.CRNI_NA_POTEZI) {
				plosca.elementSet(x, y, Polje.CRNI);
				zadnjaPoteza = poteza;
				return true;
			}
		}
		return false;
	}
	
	public Poteza getZadnjaPoteza(){
		return zadnjaPoteza;
	}
	
	public Peterica getZmagovalnaPeterica(){
		return zmagovalnaPeterica;
	}
	
	protected static final List<Smer> smeri = new LinkedList<Smer>();
	
	public static List<Smer> getSmeri() {
		return smeri;
	}
	
	static {
		// stolpci
		for (int x = 0; x < N; x++) {
			int[] seznamX = new int[N];
			int[] seznamY = new int[N];
			for (int y = 0; y < N; y++) {
				seznamX[y] = x;
				seznamY[y] = y;
			}
			smeri.add(new Smer(seznamX, seznamY));
		}
		
		// vrstice
		for (int y = 0; y < N; y++) {
			int[] seznamX = new int[N];
			int[] seznamY = new int[N];
			for (int x = 0; x < N; x++) {
				seznamX[x] = x;
				seznamY[x] = y;
			}
			smeri.add(new Smer(seznamX, seznamY));
		}
		
		// diagonale desno dol zgornji trikotnik
		for (int x = 0; x < (N - PET + 1); x++) {
			int[] seznamX = new int[N - x];
			int[] seznamY = new int[N - x];
			for (int i = 0; i < N - x; i++) {
				seznamX[i] = x + i;
				seznamY[i] = i;
			}
			smeri.add(new Smer(seznamX, seznamY));
		}
		
		// diagonale desno dol spodnji trikotnik
		for (int y = 1; y < (N - PET + 1); y++) {
			int[] seznamX = new int[N - y];
			int[] seznamY = new int[N - y];
			for (int i = 0; i < N - y; i++) {
				seznamX[i] = i;
				seznamY[i] = y + i;
			}
			smeri.add(new Smer(seznamX, seznamY));
		}
		
		// diagonale desno gor zgornji trikotnik
		for (int y = PET - 1; y < N; y++) {
			int[] seznamX = new int[y + 1];
			int[] seznamY = new int[y + 1];
			for (int i = 0; i < y + 1; i++) {
				seznamX[i] = i;
				seznamY[i] = y - i;
			}
			smeri.add(new Smer(seznamX, seznamY));
		}

		// diagonale desno gor spodnji trikotnik
		for (int x = 1; x < (N - PET + 1); x++) {
			int[] seznamX = new int[N - x];
			int[] seznamY = new int[N - x];
			for (int i = 0; i < N - x; i++) {
				seznamX[i] = i;
				seznamY[i] = N - 1 - i;
			}
			smeri.add(new Smer(seznamX, seznamY));
		}
	}

}