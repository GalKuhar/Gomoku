package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Peterica;
import logika.Plosca;
import logika.Stanje;

public class Ocena {

	public static final int ZMAGA = 100000000; // vrednost zmage, veè kot vsaka druga ocena pozicije
	public static final int ZGUBA = -100000000;  // vrednost izgube, mora biti -ZMAGA
	public static final int NEODLOCENO = 0; // vrednost neodloèene igre
	
	public static final int ENA_DO_ZMAGE = 10000000;
	public static final int DVE_DO_ZMAGE = 1000000;
	public static final int TRI_DO_ZMAGE = 100000;
	public static final int PRISILJENA_POTEZA = 1000;
	public static final int ENA_DO_PRISILJENA_POTEZA = 100;
	public static final int DVE_DO_PRISILJENA_POTEZA = 10;
	public static final int TRI_DO_PRISILJENA_POTEZA = 1;
	
	public static int vrednostPeterice(int k) {
		assert (k < Igra.getPET());
		return (ZMAGA >> (4 * (Igra.getPET() - k))); // hevristična ocena
	}
	
	public static int oceniPozicijo1(Igralec jaz, Igra igra) {
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
	
	public static int oceniPozicijo(Igralec jaz, Igra igra) {
		Stanje stanje = igra.stanje();
		switch (stanje) {
		case CRNI_ZMAGA:
			return (jaz == Igralec.CRNI ? ZMAGA : ZGUBA);
		case BELI_ZMAGA:
			return (jaz == Igralec.BELI ? ZMAGA : ZGUBA);
		case NEODLOCENO:
			return NEODLOCENO;
		case CRNI_NA_POTEZI:
		case BELI_NA_POTEZI:
			Plosca plosca = igra.getPlosca();
			
			int vrednostBELI = 0;
			int vrednostCRNI = 0;
			
			// najprej po stolpcih
			for (int x = 0; x < Igra.getN(); x++) {
				//pregledamo stirice
				for (int k = 0; k < (Igra.getN() - Igra.getPET() + 1); k++) {
					int poljaCRNI = 0;
					int poljaBELI = 0;
					
					KonecStirice zacetek = null;
					KonecStirice konec = null;
					
					//pregledamo elemente stiric
					for (int y = k; y < k + Igra.getPET() - 1; y++) {
						switch (plosca.element(x, y)) {
						case CRNI: poljaCRNI += 1; break;
						case BELI: poljaBELI += 1; break;
						case PRAZNO: break;
						}
					}
					
					if (poljaBELI == 0 && poljaCRNI > 0) {
						if (k == 0) {
							// ce smo na konu polja imamo zaprt konec stirice
							zacetek = KonecStirice.ZAPRT;
						} else {
							switch (plosca.element(x, k - 1)) {
							case CRNI: zacetek = KonecStirice.ISTE_BARVE; break;
							case BELI: zacetek = KonecStirice.ZAPRT; break;
							case PRAZNO: zacetek = KonecStirice.ODPRT; break;
							}
						}
						
						if (k == Igra.getN() - Igra.getPET() + 1) {
							konec = KonecStirice.ZAPRT;
						} else {
							switch (plosca.element(x, k + Igra.getPET() - 1)) {
							case CRNI: konec = KonecStirice.ISTE_BARVE; break;
							case BELI: konec = KonecStirice.ZAPRT; break;
							case PRAZNO: konec = KonecStirice.ODPRT; break;
							}
						}
						
						vrednostCRNI += oceniStirico(poljaCRNI, zacetek, konec, semJazNaPotezi(stanje, jaz));
					}
					
					if (poljaCRNI == 0 && poljaBELI > 0) {
						if (k == 0) {
							// ce smo na konu polja imamo zaprt konec stirice
							zacetek = KonecStirice.ZAPRT;
						} else {
							switch (plosca.element(x, k - 1)) {
							case BELI: zacetek = KonecStirice.ISTE_BARVE; break;
							case CRNI: zacetek = KonecStirice.ZAPRT; break;
							case PRAZNO: zacetek = KonecStirice.ODPRT; break;
							}
						}
						
						if (k == Igra.getN() - Igra.getPET() + 1) {
							konec = KonecStirice.ZAPRT;
						} else {
							switch (plosca.element(x, k + Igra.getPET() - 1)) {
							case BELI: konec = KonecStirice.ISTE_BARVE; break;
							case CRNI: konec = KonecStirice.ZAPRT; break;
							case PRAZNO: konec = KonecStirice.ODPRT; break;
							}
						}
						vrednostBELI += oceniStirico(poljaCRNI, zacetek, konec, semJazNaPotezi(stanje, jaz));
					}
				}
			}
			
			// še po vrsicah:
			for (int y = 0; y < Igra.getN(); y++) {
				//pregledamo stirice
				for (int k = 0; k < (Igra.getN() - Igra.getPET() + 1); k++) {
					int poljaCRNI = 0;
					int poljaBELI = 0;
					
					KonecStirice zacetek = null;
					KonecStirice konec = null;
					
					//pregledamo elemente stiric
					for (int x = k; x < k + Igra.getPET() - 1; x++) {
						switch (plosca.element(x, y)) {
						case CRNI: poljaCRNI += 1; break;
						case BELI: poljaBELI += 1; break;
						case PRAZNO: break;
						}
					}
					if (poljaBELI == 0 && poljaCRNI > 0) {
						if (k == 0) {
							// ce smo na konu polja imamo zaprt konec stirice
							zacetek = KonecStirice.ZAPRT;
						} else {
							switch (plosca.element(k - 1, y)) {
							case CRNI: zacetek = KonecStirice.ISTE_BARVE; break;
							case BELI: zacetek = KonecStirice.ZAPRT; break;
							case PRAZNO: zacetek = KonecStirice.ODPRT; break;
							}
						}
						
						if (k == Igra.getN() - Igra.getPET() + 1) {
							konec = KonecStirice.ZAPRT;
						} else {
							switch (plosca.element(k + Igra.getPET() - 1, y)) {
							case CRNI: konec = KonecStirice.ISTE_BARVE; break;
							case BELI: konec = KonecStirice.ZAPRT; break;
							case PRAZNO: konec = KonecStirice.ODPRT; break;
							}
						}
						vrednostCRNI += oceniStirico(poljaCRNI, zacetek, konec, semJazNaPotezi(stanje, jaz));
					}
					
					if (poljaCRNI == 0 && poljaBELI > 0) {
						if (k == 0) {
							// ce smo na konu polja imamo zaprt konec stirice
							zacetek = KonecStirice.ZAPRT;
						} else {
							switch (plosca.element(k - 1, y)) {
							case BELI: zacetek = KonecStirice.ISTE_BARVE; break;
							case CRNI: zacetek = KonecStirice.ZAPRT; break;
							case PRAZNO: zacetek = KonecStirice.ODPRT; break;
							}
						}
						
						if (k == Igra.getN() - Igra.getPET() + 1) {
							konec = KonecStirice.ZAPRT;
						} else {
							switch (plosca.element(k + Igra.getPET() - 1, y)) {
							case BELI: konec = KonecStirice.ISTE_BARVE; break;
							case CRNI: konec = KonecStirice.ZAPRT; break;
							case PRAZNO: konec = KonecStirice.ODPRT; break;
							}
						}
						vrednostBELI += oceniStirico(poljaCRNI, zacetek, konec, semJazNaPotezi(stanje, jaz));
					}
				}
			}
			System.out.println(jaz == Igralec.BELI ? (vrednostBELI - vrednostCRNI) : (vrednostCRNI - vrednostBELI));
			return (jaz == Igralec.BELI ? (vrednostBELI - vrednostCRNI) : (vrednostCRNI - vrednostBELI));
			
		case IGRA_NI_VELJAVNA:
			// to samo da eclipse in java ne tezita.
			return 413;
		}
		return 0;
	}
	
	public static boolean semJazNaPotezi(Stanje stanje, Igralec Jaz) {
		if (stanje == Stanje.BELI_NA_POTEZI) {
			return Jaz == Igralec.BELI;
		} else if (stanje == Stanje.CRNI_NA_POTEZI) {
			return Jaz == Igralec.CRNI;
		} else {
			return false;
		}
	}
	
	public static int oceniStirico(int stevilo, KonecStirice zacetek, KonecStirice konec, boolean jazNaPotezi) {
		System.out.println("ocenjujem" + stevilo + jazNaPotezi);
		// katerikoli primer O----O ne sme dati nič pik
		if (zacetek == KonecStirice.ZAPRT && konec == KonecStirice.ZAPRT) {
			return 0;
		}
		switch (stevilo) {
		case 4:
			// to primer ko smo že zmagali, ne glede na to, ali smo na potezi -XXXX-
			if (zacetek == KonecStirice.ODPRT && konec == KonecStirice.ODPRT) {
				return ENA_DO_ZMAGE;
			// OXXXX-
			} else if (zacetek == KonecStirice.ZAPRT || konec == KonecStirice.ZAPRT) {
				// če smo mi na potezi samo še zmagamo
				if (jazNaPotezi) {
					return ENA_DO_ZMAGE;
				// če drugi na potezi mora to igrati, ali pa izgubi
				} else {
					return PRISILJENA_POTEZA;
				}
			// do tega sicer ne sme priti ampak da bo popolno:
			} else {
				return ZMAGA;
			}
		case 3:
			// to pravzaprav primer 4 z presledkom: XXX-X ali XX-XX
			if (zacetek == KonecStirice.ISTE_BARVE || konec == KonecStirice.ISTE_BARVE) {
				// če smo mi na potezi samo še zmagamo
				if (jazNaPotezi) {
					return ENA_DO_ZMAGE;
				// če drugi na potezi mora to igrati, ali pa izgubi
				} else {
					return PRISILJENA_POTEZA;
				}
			// 3 ki zaprta na eni strani OXXX-- ali O-XXX- (vemo da na drugi strani odprt)
			} else if (zacetek == KonecStirice.ZAPRT || konec == KonecStirice.ZAPRT) {
				return ENA_DO_PRISILJENA_POTEZA;
			// 3 odprta na obeh straneh: --XXX- ali -XX-X-
			} else {
				// to zelo dobro, saj zmagam
				if (jazNaPotezi) {
					return DVE_DO_ZMAGE;
				// to prisili v potezo, ali pa naslednjo zmagam
				} else {
					return PRISILJENA_POTEZA;
				}
			}
		case 2:
			// 3 z presledkom --XXX ali -XX-X ali X-X-X ali XX--X ,...
			if (zacetek == KonecStirice.ISTE_BARVE || konec == KonecStirice.ISTE_BARVE) {
				// ce X igra notri more tudi beli
				return ENA_DO_PRISILJENA_POTEZA;
			// O-XX-- ali O-X-X- ali  OX-X--
			} else if (zacetek == KonecStirice.ZAPRT || konec == KonecStirice.ZAPRT) {
				// X mora 2x igrati notri, da potem beli prisiljen v potezo
				return DVE_DO_PRISILJENA_POTEZA;
			// Potem sta oba konca odprta --XX-- ali -X--X- ali --X-X-
			} else {
				// ce lahko 3x igram notri zmagam
				if (jazNaPotezi) {
					return TRI_DO_ZMAGE;
				// ce notri igram, bo to postala prisiljena poteza
				} else {
					return ENA_DO_PRISILJENA_POTEZA;
				}
			}
		case 1:
			// 2 z presledkom: X---X ali X--X- X-X-- XX---
			if (zacetek == KonecStirice.ISTE_BARVE || konec == KonecStirice.ISTE_BARVE) {
				// ce X igra 2x notri je O prisiljen
				return DVE_DO_PRISILJENA_POTEZA;
			// OX---- O-X--- O--X-- O---X-
			} else if (zacetek == KonecStirice.ZAPRT || konec == KonecStirice.ZAPRT) {
				// ce X igra 3x notri to prisiljena
				return TRI_DO_PRISILJENA_POTEZA;
			// -X---- --X---
			} else {
				// ce X lahko 3x notri igra zmaga
				if (jazNaPotezi) {
					return TRI_DO_ZMAGE;
				// ce X 2x notri igra, je naslednja poteza prisiljena
				} else {
					return DVE_DO_PRISILJENA_POTEZA;
				}
			}
		default:
			return 0;
		}
	}	
}
