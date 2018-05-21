package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Peterica;
import logika.Plosca;

public class Ocena {

	public static final int ZMAGA = 100000000; // vrednost zmage, veè kot vsaka druga ocena pozicije
	public static final int ZGUBA = -ZMAGA;  // vrednost izgube, mora biti -ZMAGA
	public static final int NEODLOCENO = 0; // vrednost neodloèene igre
	
//	public static final int VREDNOST0 = 1;
//	public static final int VREDNOST1 = 10;
//	public static final int VREDNOST2 = 100;
//	public static final int VREDNOST3 = 1000;
//	public static final int VREDNOST4 = 10000;
//	
//	public static int vrednostPeterice(int k) {
//		assert (k < Igra.getPET());
//		if (k == 1) {
//			return VREDNOST1;
//		} else if (k == 2) {
//			return VREDNOST2;
//		} else if (k == 3) {
//			return VREDNOST3;
//		} else if (k == 4) {
//			return VREDNOST4;
//		} else {
//			return VREDNOST0;
//		}
//	}
	
	public static int vrednostPeterice(int k) {
		assert (k < Igra.getPET());
		return (ZMAGA >> (4 * (Igra.getPET() - k))); // hevristièna ocena
	}
	
	public static int oceniPozicijo(Igralec jaz, Igra igra) {
		switch (igra.stanje()) {
		case CRNI_ZMAGA:
			return (jaz == Igralec.CRNI ? ZMAGA : ZGUBA);
		case BELI_ZMAGA:
			return (jaz == Igralec.BELI ? ZMAGA : ZGUBA);
		case NEODLOCENO:
			return NEODLOCENO;
		case CRNI_NA_POTEZI:
		case BELI_NA_POTEZI:
			// Preštejemo, koliko teric ima vsak igralec
			Plosca plosca = igra.getPlosca();
			int vrednostBELI = 0;
			int vrednostCRNI = 0;
			for (Peterica t : Igra.getPeterice()) {
				int poljaBELI = 0;
				int poljaCRNI = 0;
				for (int k = 0; k < Igra.getPET() && (poljaBELI == 0 || poljaCRNI == 0); k++) {
					switch (plosca.element(t.getX()[k], t.getY()[k])) {
					case CRNI: poljaCRNI += 1; break;
					case BELI: poljaBELI += 1; break;
					case PRAZNO: break;
					}
				}
				if (poljaBELI == 0 && poljaCRNI > 0) { vrednostCRNI += vrednostPeterice(poljaBELI); }
				if (poljaCRNI == 0 && poljaBELI > 0) { vrednostBELI += vrednostPeterice(poljaCRNI); }
			}
			// To deljenje z 2 je verjetno brez veze ali celo narobe
			return (jaz == Igralec.BELI ? (vrednostBELI - vrednostCRNI/2) : (vrednostCRNI - vrednostBELI/2));
		}
		assert false;
		return 42; // Java je blesava
	}
	
}
