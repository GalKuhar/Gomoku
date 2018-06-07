package inteligenca;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.SwingWorker;
import gui.GlavnoOkno;
import logika.Igra;
import logika.Igralec;
import logika.Poteza;

public class Minimax extends SwingWorker<Poteza, Object>  {

	private GlavnoOkno master;
	private int globina;
	private Igralec jaz;
	
//	private List<Poteza> najboljsePoteze;
	
	public Minimax(GlavnoOkno master, int globina, Igralec jaz) {
		this.master = master;
		this.globina = globina;
		this.jaz = jaz;
	}
	
	@Override
	protected Poteza doInBackground() throws Exception {
		Igra igra = master.copyIgra();
		OcenjenaPoteza p = minimax(0, igra);
		assert (p.poteza != null);
		return p.poteza;
	}
	
	@Override
	public void done() {
		try {
			Poteza p = this.get();
			if (p != null) { master.odigraj(p); }
		} catch (Exception e) {
		}
	}

	private OcenjenaPoteza minimax(int k, Igra igra) {
		
		Igralec naPotezi = null;
		// Ugotovimo, ali je konec, ali je kdo na potezi?
		switch (igra.stanje()) {
		case CRNI_NA_POTEZI: naPotezi = Igralec.CRNI; break;
		case BELI_NA_POTEZI: naPotezi = Igralec.BELI; break;
		// Igre je konec, ne moremo vrniti poteze, vrnemo le vrednost pozicije
		case BELI_ZMAGA:
			return new OcenjenaPoteza(
					null,
					(jaz == Igralec.BELI ? Ocena.ZMAGA : Ocena.ZGUBA));
		case CRNI_ZMAGA:
			return new OcenjenaPoteza(
					null,
					(jaz == Igralec.CRNI ? Ocena.ZMAGA : Ocena.ZGUBA));
		case NEODLOCENO:
			return new OcenjenaPoteza(null, Ocena.NEODLOCENO);
		default:
			break;
		}
		
		assert (naPotezi != null);
		// Nekdo je na potezi, ugotovimo, kaj se splača igrati
		
		if (k >= globina) {
			// dosegli smo največjo dovoljeno globino, zato
			// ne vrnemo poteze, ampak samo oceno pozicije
			return new OcenjenaPoteza(
					null,
					Ocena.oceniPozicijo(jaz, igra));
		}
		
		Poteza najboljsa = null;
		int ocenaNajboljse = 0;
		
		// to premeša možne poteze - s tem dodamo random izbiro
		LinkedList<Poteza> moznePoteze = igra.moznePoteze();
		Collections.shuffle(moznePoteze);
		
		for (Poteza p : moznePoteze) {
			// V kopiji igre odigramo potezo p
			
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigrajPotezo(p);
			
			// Izračunamo vrednost pozicije po odigrani potezi p
			
			int ocenaP = minimax(k+1, kopijaIgre).vrednost;
			
			// če je p boljša poteza, si jo zabeležimo
			if (najboljsa == null // če še nimamo kandidatov za najboljše poteze
				|| (naPotezi == jaz && ocenaP > ocenaNajboljse) // maksimiziramo
				|| (naPotezi != jaz && ocenaP < ocenaNajboljse) // minimiziramo
				) {
				najboljsa = p;
				ocenaNajboljse = ocenaP;
			}
		}

		assert (najboljsa != null);
		return new OcenjenaPoteza(najboljsa, ocenaNajboljse);
	}
}
