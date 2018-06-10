package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Plosca;
import logika.Smer;
import logika.Stanje;

public class Ocena {

	public static final int ZMAGA = 10000000; // vrednost zmage, več kot vsaka druga ocena pozicije
	public static final int ZGUBA = -ZMAGA;  // vrednost izgube, mora biti -ZMAGA
	public static final int NEODLOCENO = 0; // vrednost neodločene igre
	
	public static final int ENA_DO_ZMAGE = 100000;
	public static final int DVE_DO_ZMAGE = 50000;
	public static final int TRI_DO_ZMAGE = 100;
	public static final int PRISILJENA_POTEZA = 2000;
	public static final int ENA_DO_PRISILJENA_POTEZA = 200;
	public static final int DVE_DO_PRISILJENA_POTEZA = 20;
	public static final int TRI_DO_PRISILJENA_POTEZA = 2;
	
	public static int oceniPozicijo(Igralec jaz, Igra igra) {
		Igralec naPotezi = null;
		
		Stanje stanje = igra.stanje();
		switch (stanje) {
		case CRNI_NA_POTEZI:
			naPotezi = Igralec.CRNI; break;
		case BELI_NA_POTEZI:
			naPotezi = Igralec.BELI; break;
		default:
			break;
		}
		
		Plosca plosca = igra.getPlosca();
			
		int vrednostBELI = 0;
		int vrednostCRNI = 0;
			
		// po smereh
		for (Smer s: Igra.getSmeri()) {
			// pregledamo štirice v smeri
			for (int k = 0; k < s.dolzina() - Igra.getPET() + 2; k++) {
				int poljaCRNI = 0;
				int poljaBELI = 0;
					
				KonecStirice zacetek = null;
				KonecStirice konec = null;
					
				// pregledamo elemente štiric
				for (int i = k; i < k + Igra.getPET() - 1; i++) {
					switch (plosca.element(s.getX()[i], s.getY()[i])) {
					case CRNI: poljaCRNI += 1; break;
					case BELI: poljaBELI += 1; break;
					case PRAZNO: break;
					default:
						break;
					}
				}
					
				if (poljaBELI == 0 && poljaCRNI > 0) {
					if (k == 0) {
						// če smo na koncu polja, imamo zaprt konec štirice
						zacetek = KonecStirice.ZAPRT;
					} else {
						switch (plosca.element(s.getX()[k - 1], s.getY()[k - 1])) {
						case CRNI: zacetek = KonecStirice.ISTE_BARVE; break;
						case BELI: zacetek = KonecStirice.ZAPRT; break;
						case PRAZNO: zacetek = KonecStirice.ODPRT; break;
						default:
							break;
						}
					}
						
					if (k == s.dolzina() - Igra.getPET() + 1) {
						konec = KonecStirice.ZAPRT;
					} else {
						switch (plosca.element(s.getX()[k + Igra.getPET() - 1], s.getY()[k + Igra.getPET() - 1])) {
						case CRNI: konec = KonecStirice.ISTE_BARVE; break;
						case BELI: konec = KonecStirice.ZAPRT; break;
						case PRAZNO: konec = KonecStirice.ODPRT; break;
						default:
							break;
						}
					}
					
					vrednostCRNI += oceniStirico(poljaCRNI, zacetek, konec, naPotezi == Igralec.CRNI);
				}
				
				if (poljaCRNI == 0 && poljaBELI > 0) {
					if (k == 0) {
						// če smo na koncu polja, imamo zaprt konec štirice
						zacetek = KonecStirice.ZAPRT;
					} else {
						switch (plosca.element(s.getX()[k - 1], s.getY()[k - 1])) {
						case BELI: zacetek = KonecStirice.ISTE_BARVE; break;
						case CRNI: zacetek = KonecStirice.ZAPRT; break;
						case PRAZNO: zacetek = KonecStirice.ODPRT; break;
						default:
							break;
						}
					}
					
					if (k == s.dolzina() - Igra.getPET() + 1) {
						konec = KonecStirice.ZAPRT;
					} else {
						switch (plosca.element(s.getX()[k + Igra.getPET() - 1], s.getY()[k + Igra.getPET() - 1])) {
						case BELI: konec = KonecStirice.ISTE_BARVE; break;
						case CRNI: konec = KonecStirice.ZAPRT; break;
						case PRAZNO: konec = KonecStirice.ODPRT; break;
						default:
							break;
						}
					}
					
					vrednostBELI += oceniStirico(poljaBELI, zacetek, konec, naPotezi == Igralec.BELI);
				}
			}
		}
		return (jaz == Igralec.BELI ? vrednostBELI - vrednostCRNI : vrednostCRNI - vrednostBELI);
	}
	
	public static int oceniStirico(int stevilo, KonecStirice zacetek, KonecStirice konec, boolean jazNaPotezi) {
		// katerikoli primer O----O, ne sme dati nič pik
		assert(zacetek != null);
		assert(konec != null);
		
		if (zacetek == KonecStirice.ZAPRT && konec == KonecStirice.ZAPRT) {
			return 0;
		}
		
		// System.out.println(jazNaPotezi);
		
		switch (stevilo) {
		case 4:
			// to primer ko smo že zmagali, ne glede na to, ali smo na potezi -XXXX-
			if (zacetek == KonecStirice.ODPRT && konec == KonecStirice.ODPRT) {
				return ENA_DO_ZMAGE;
			// OXXXX-
			} else if (zacetek == KonecStirice.ZAPRT || konec == KonecStirice.ZAPRT) {
				// če smo mi na potezi, samo še zmagamo
				if (jazNaPotezi) {
					return ENA_DO_ZMAGE;
				// če drugi na potezi mora to igrati, ali pa izgubi
				} else {
					return PRISILJENA_POTEZA;
				}
			// primer XXXXX- ali XXXXXX
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
			// 3 s presledkom --XXX ali -XX-X ali X-X-X ali XX--X ,...
			if (zacetek == KonecStirice.ISTE_BARVE || konec == KonecStirice.ISTE_BARVE) {
				// če X igra notri mora tudi beli
				return ENA_DO_PRISILJENA_POTEZA;
			// O-XX-- ali O-X-X- ali  OX-X--
			} else if (zacetek == KonecStirice.ZAPRT || konec == KonecStirice.ZAPRT) {
				// X mora 2x igrati notri, da potem beli prisiljen v potezo
				return DVE_DO_PRISILJENA_POTEZA;
			// potem sta oba konca odprta --XX-- ali -X--X- ali --X-X-
			} else {
				// če lahko 3x igram notri zmagam
				if (jazNaPotezi) {
					return TRI_DO_ZMAGE;
				// če notri igram, bo to postala prisiljena poteza
				} else {
					return ENA_DO_PRISILJENA_POTEZA;
				}
			}
		case 1:
			// 2 s presledkom: X---X ali X--X- X-X-- XX---
			if (zacetek == KonecStirice.ISTE_BARVE || konec == KonecStirice.ISTE_BARVE) {
				// če X igra 2x notri je O prisiljen
				return DVE_DO_PRISILJENA_POTEZA;
			// OX---- O-X--- O--X-- O---X-
			} else if (zacetek == KonecStirice.ZAPRT || konec == KonecStirice.ZAPRT) {
				// če X igra 3x notri to prisiljena
				return TRI_DO_PRISILJENA_POTEZA;
			// -X---- --X---
			} else {
				// če X lahko 3x notri igra zmaga
				if (jazNaPotezi) {
					return TRI_DO_ZMAGE;
				// če X 2x notri igra, je naslednja poteza prisiljena
				} else {
					return DVE_DO_PRISILJENA_POTEZA;
				}
			}
		default:
			return 0; //ne pride do tega
		}
	}	
}
