package logika_testi;

import junit.framework.TestCase;
import logika.Igra;
import logika.Igralec;
import logika.Stanje;
import logika.Poteza;


public class TestIgra extends TestCase {

	public void testTabelePeterice() {
		// preverimo ce smo nasli dovolj peteric (11*15 vodoravnih + navpicnih, 11 * 11 posevnih)
		assertEquals(Igra.getPeterice().size(), 2 * (Igra.getN() - Igra.getPET() + 1) * Igra.getN() + 2 * (Igra.getN() - Igra.getPET() + 1) * (Igra.getN() - Igra.getPET() + 1));
	}
	
	public void testDolzinePeterice() {
		// preverimo ce so peterice dovolj dolge
		assertEquals(Igra.getPeterice().get(0).getX().length, Igra.getPET());
		assertEquals(Igra.getPeterice().get(0).getY().length, Igra.getPET());
	}
	
	public void testTabeleSmeri() {
		// preverimo ce so peterice dovolj dolge
		assertEquals(Igra.getSmeri().size(), 15 + 15 + 21 + 21);
	}
	
	public void testIgra() {
		Igra igra = new Igra();
		// ko naredimo novo igro, mora zaceti crni
		assertEquals(igra.getPrviNaPotezi(), Igralec.CRNI);
		// preverimo ce najde pravo stanje
		assertEquals(igra.stanje(), Stanje.CRNI_NA_POTEZI);
		// preverimo ce imamo n * n moznih potez
		assertEquals(Igra.getN() * Igra.getN(), igra.moznePoteze().size());
		
		// Naredimo eno potezo
		igra.odigrajPotezo(igra.moznePoteze().get(0));
		// Preverimo ce je se pravilono stanje
		assertEquals(Stanje.BELI_NA_POTEZI, igra.stanje());
		// more biti 1 manj moznih potez
		assertEquals(Igra.getN() * Igra.getN() - 1, igra.moznePoteze().size());
	}
	
	public void testIgra2() {
		// testira funkcijo stanje - ali lahko najde, ce kdo zmaga
		Igra igra = new Igra();
		igra.odigrajPotezo(new Poteza(0,0));
		assertEquals(Stanje.BELI_NA_POTEZI, igra.stanje());
		igra.odigrajPotezo(new Poteza(0,1));
		assertEquals(Stanje.CRNI_NA_POTEZI, igra.stanje());
		igra.odigrajPotezo(new Poteza(1,0));
		igra.odigrajPotezo(new Poteza(1,1));
		igra.odigrajPotezo(new Poteza(2,0));
		igra.odigrajPotezo(new Poteza(2,1));
		igra.odigrajPotezo(new Poteza(3,0));
		igra.odigrajPotezo(new Poteza(3,1));
		igra.odigrajPotezo(new Poteza(4,0));
		assertEquals(Stanje.CRNI_ZMAGA, igra.stanje());
	}
	
	public void testIgra3() {
		Igra igra = new Igra();
		igra.odigrajPotezo(new Poteza(0,0));
		assertEquals(Stanje.BELI_NA_POTEZI, igra.stanje());
		igra.odigrajPotezo(new Poteza(0,1));
		assertEquals(Stanje.CRNI_NA_POTEZI, igra.stanje());
		igra.odigrajPotezo(new Poteza(1,0));
		igra.odigrajPotezo(new Poteza(1,1));
		igra.odigrajPotezo(new Poteza(2,0));
		igra.odigrajPotezo(new Poteza(2,1));
		igra.odigrajPotezo(new Poteza(3,0));
		igra.odigrajPotezo(new Poteza(3,1));
		igra.odigrajPotezo(new Poteza(14,14));
		igra.odigrajPotezo(new Poteza(4,1));
		assertEquals(Stanje.BELI_ZMAGA, igra.stanje());
	}
	
	public void testIgra4() {
		// testira koliko èasa traja, èe zmagamo v zadnji peterici
		Igra igra = new Igra();
		igra.odigrajPotezo(new Poteza(10,14));
		igra.odigrajPotezo(new Poteza(0,0));
		igra.odigrajPotezo(new Poteza(11,13));
		igra.odigrajPotezo(new Poteza(0,1));
		igra.odigrajPotezo(new Poteza(12,12));
		igra.odigrajPotezo(new Poteza(0,3));
		igra.odigrajPotezo(new Poteza(13,11));
		igra.odigrajPotezo(new Poteza(0,4));
		igra.odigrajPotezo(new Poteza(14,10));
		assertEquals(Stanje.CRNI_ZMAGA, igra.stanje());
	}
}
