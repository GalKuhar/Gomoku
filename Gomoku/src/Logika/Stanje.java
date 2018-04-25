package Logika;

public enum Stanje {
	BELI_NA_POTEZI,
	CRNI_NA_POTEZI,
	CRNI_ZMAGA,
	BELI_ZMAGA,
	NEODLOCENO;
	
	private int steviloBelih;
	private int steviloCrnih;
	
	public void prestej(Igra igra) {
		steviloBelih = 0;
		steviloCrnih = 0;
		for (int i = 0 ; i < Igra.N ; i++) {
			for (int j = 0 ; j < Igra.N ; j++) {
				if (igra.plosca.element(i, j) == Polje.BELI) {
					steviloBelih++;
				} else if (igra.plosca.element(i, j) == Polje.CRNI) {
					steviloCrnih++;
				}
			}
		}
	}
		
	// še ni dokonèano
	// naredi funkcije: veljavnost igre (boolean), kdo zmagal ali neodloèeno, kdo na potezi
	public Stanje veljavnost(Igra igra) {
		if (steviloBelih + steviloCrnih == Igra.N * Igra.N) {
			return NEODLOCENO;
		}
		
		if (steviloBelih == steviloCrnih) {
			if (Igra.prviNaPotezi == Igralec.BELI) {
				return BELI_NA_POTEZI;
			} else {
				return CRNI_NA_POTEZI;
			}
		} else if (steviloBelih == steviloCrnih + 1 && Igra.prviNaPotezi == Igralec.BELI){
			return CRNI_NA_POTEZI;
		} else if (steviloBelih + 1 == steviloCrnih && Igra.prviNaPotezi == Igralec.CRNI){
			return BELI_NA_POTEZI;
		} else {
			// èe pride do konca in ne najde, kdo je na potezi, vrne null
			return null;
		}
	}	
}
