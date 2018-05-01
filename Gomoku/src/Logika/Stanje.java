package Logika;

public enum Stanje {
	BELI_NA_POTEZI,
	CRNI_NA_POTEZI,
	CRNI_ZMAGA,
	BELI_ZMAGA,
	NEODLOCENO,
	IGRA_NI_VELJAVNA;
	
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
		
	/**
	 * @return Vrne stanje igre
	 */
	public Stanje stanje(Igra igra) {
		prestej(igra);
		int steviloPolnihPeteric = 0;
		Igralec zmagovalec = null;
		
		for (Peterica peterica : Igra.peterice) {
			if (peterica.vseBarve(igra.plosca, Polje.BELI)) {
				zmagovalec = Igralec.BELI;
				steviloPolnihPeteric++;
			} else if (peterica.vseBarve(igra.plosca, Polje.CRNI)) {
				zmagovalec = Igralec.CRNI;
				steviloPolnihPeteric++;
			}
		}

		// ce je prevec polnih peteric, je nekaj narobe
		if (steviloPolnihPeteric > 1) {
			return IGRA_NI_VELJAVNA;
		}
		
		if (zmagovalec == Igralec.BELI) {
			return BELI_ZMAGA;
		} else if (zmagovalec == Igralec.CRNI) {
			return CRNI_ZMAGA;
		}
		
		if (steviloBelih + steviloCrnih == Igra.N * Igra.N) {
			return NEODLOCENO;
		}
		
		if (steviloBelih == steviloCrnih) {
			if (igra.getPrviNaPotezi() == Igralec.BELI) {
				return BELI_NA_POTEZI;
			} else {
				return CRNI_NA_POTEZI;
			}
		} else if (steviloBelih == steviloCrnih + 1 && igra.getPrviNaPotezi() == Igralec.BELI){
			return CRNI_NA_POTEZI;
		} else if (steviloBelih + 1 == steviloCrnih && igra.getPrviNaPotezi() == Igralec.CRNI){
			return BELI_NA_POTEZI;
		} else {
			// ce pride do konca in ne najde kdo na potezi, mora biti nekaj narobe
			return IGRA_NI_VELJAVNA;
		}
	}	
}
