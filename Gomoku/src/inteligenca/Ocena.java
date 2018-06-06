package inteligenca;

import java.util.LinkedList;
import java.util.List;

import logika.Igra;
import logika.Igralec;
import logika.Peterica;
import logika.Plosca;
import logika.Polje;
import logika.Smer;
import logika.Stanje;

public class Ocena {

	public static final int ZMAGA = 100000000; // vrednost zmage, veè kot vsaka druga ocena pozicije
	public static final int ZGUBA = -100000000;  // vrednost izgube, mora biti -ZMAGA
	public static final int NEODLOCENO = 0; // vrednost neodloèene igre
	
	public static final int ENA_DO_ZMAGE = 100000;
	public static final int DVE_DO_ZMAGE = 10000;
	public static final int TRI_DO_ZMAGE = 1000;
	public static final int PRISILJENA_POTEZA = 50000;
	public static final int ENA_DO_PRISILJENA_POTEZA = 5000;
	public static final int DVE_DO_PRISILJENA_POTEZA = 500;
	public static final int TRI_DO_PRISILJENA_POTEZA = 50;
	
	
	
	public static int oceniPozicijo(Igralec jaz, Igra igra) {
		Igralec naPotezi = null;
		Stanje stanje = igra.stanje();
		switch (stanje) {
		case CRNI_ZMAGA:
			return (jaz == Igralec.CRNI ? ZMAGA : ZGUBA);
		case BELI_ZMAGA:
			return (jaz == Igralec.BELI ? ZMAGA : ZGUBA);
		case NEODLOCENO:
			return NEODLOCENO;
		case CRNI_NA_POTEZI:
			naPotezi = Igralec.CRNI;
		case BELI_NA_POTEZI:
			naPotezi = Igralec.BELI;
			Plosca plosca = igra.getPlosca();
			
			int vrednostBELI = 0;
			int vrednostCRNI = 0;
			
			// po smereh
			for (Smer s: Igra.getSmeri()) {
				// pregledamo stirice v smeri
				for (int k = 0; k < s.dolzina() - Igra.getPET() + 2; k++) {
					int poljaCRNI = 0;
					int poljaBELI = 0;
					
					KonecStirice zacetek = null;
					KonecStirice konec = null;
					
					//pregledamo elemente stiric
					for (int i = k; i < k + Igra.getPET() - 1; i++) {
						switch (plosca.element(s.getX()[i], s.getY()[i])) {
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
							switch (plosca.element(s.getX()[k - 1], s.getY()[k - 1])) {
							case CRNI: zacetek = KonecStirice.ISTE_BARVE; break;
							case BELI: zacetek = KonecStirice.ZAPRT; break;
							case PRAZNO: zacetek = KonecStirice.ODPRT; break;
							}
						}
						
						if (k == s.dolzina() - Igra.getPET() + 1) {
							konec = KonecStirice.ZAPRT;
						} else {
							switch (plosca.element(s.getX()[k + Igra.getPET() - 1], s.getY()[k + Igra.getPET() - 1])) {
							case CRNI: konec = KonecStirice.ISTE_BARVE; break;
							case BELI: konec = KonecStirice.ZAPRT; break;
							case PRAZNO: konec = KonecStirice.ODPRT; break;
							}
						}
						
						vrednostCRNI += oceniStirico(poljaCRNI, zacetek, konec, naPotezi == Igralec.CRNI);
					}
					
					if (poljaCRNI == 0 && poljaBELI > 0) {
						if (k == 0) {
							// ce smo na konu polja imamo zaprt konec stirice
							zacetek = KonecStirice.ZAPRT;
						} else {
							switch (plosca.element(s.getX()[k - 1], s.getY()[k - 1])) {
							case BELI: zacetek = KonecStirice.ISTE_BARVE; break;
							case CRNI: zacetek = KonecStirice.ZAPRT; break;
							case PRAZNO: zacetek = KonecStirice.ODPRT; break;
							}
						}
						
						if (k == s.dolzina() - Igra.getPET() + 1) {
							konec = KonecStirice.ZAPRT;
						} else {
							switch (plosca.element(s.getX()[k + Igra.getPET() - 1], s.getY()[k + Igra.getPET() - 1])) {
							case BELI: konec = KonecStirice.ISTE_BARVE; break;
							case CRNI: konec = KonecStirice.ZAPRT; break;
							case PRAZNO: konec = KonecStirice.ODPRT; break;
							}
						}
						
						vrednostBELI += oceniStirico(poljaBELI, zacetek, konec, naPotezi == Igralec.BELI);
					}
					
					// System.out.println(oceniStirico(poljaCRNI, zacetek, konec, semJazNaPotezi(stanje, naPotezi)));
//					if (poljaBELI == 0 && poljaCRNI > 0) { vrednostCRNI += oceniStirico(poljaCRNI, zacetek, konec, semJazNaPotezi(stanje, naPotezi)); }
//					if (poljaCRNI == 0 && poljaBELI > 0) { vrednostBELI += oceniStirico(poljaBELI, zacetek, konec, semJazNaPotezi(stanje, naPotezi)); }
				}
			}
			System.out.println("BELI: " + vrednostBELI);
			System.out.println("CRNI: " + vrednostCRNI);
			if (naPotezi == Igralec.BELI) { vrednostCRNI /= 2; }
			if (naPotezi == Igralec.CRNI) { vrednostBELI /= 2; }
			System.out.println(jaz == Igralec.BELI ? (vrednostBELI - vrednostCRNI) : (vrednostCRNI - vrednostBELI));
			return (jaz == Igralec.BELI ? vrednostBELI - vrednostCRNI : vrednostCRNI - vrednostBELI);
			
		case IGRA_NI_VELJAVNA:
			// to samo da eclipse in java ne tezita.
			return 413;
		}
		return 0;
	}
	
//	public static boolean semJazNaPotezi(Stanje stanje, Igralec Jaz) {
//		if (stanje == Stanje.BELI_NA_POTEZI) {
//			return Jaz == Igralec.BELI;
//		} else if (stanje == Stanje.CRNI_NA_POTEZI) {
//			return Jaz == Igralec.CRNI;
//		} else {
//			return false;
//		}
//	}
	
	public static int oceniStirico(int stevilo, KonecStirice zacetek, KonecStirice konec, boolean jazNaPotezi) {
		// katerikoli primer O----O ne sme dati nič pik
		assert(zacetek != null);
		assert(konec != null);
		
		if (zacetek == KonecStirice.ZAPRT && konec == KonecStirice.ZAPRT) {
			return 0;
		}
		
		System.out.println(jazNaPotezi);
		
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
			return 0; //ne pride do tega
		}
	}	
}
