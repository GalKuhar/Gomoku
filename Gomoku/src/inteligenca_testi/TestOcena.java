package inteligenca_testi;

import inteligenca.KonecStirice;
import junit.framework.TestCase;
import inteligenca.Ocena;

public class TestOcena extends TestCase{
	
	public void testOcene1() {
		assertEquals(Ocena.oceniStirico(4, KonecStirice.ODPRT, KonecStirice.ODPRT, true), Ocena.ENA_DO_ZMAGE);
	}
}
