package inteligenca;

import javax.swing.SwingWorker;

import gui.GlavnoOkno;
import logika.Igra;
import logika.Igralec;
import logika.Poteza;

public class Minimax extends SwingWorker<Poteza, Object>  {

	private GlavnoOkno master;
	
	private int globina;
	
	private Igralec jaz;
	
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
		}
		assert (naPotezi != null);
		// Nekdo je na potezi, ugotovimo, kaj se spla�a igrati
		if (k >= globina) {
			// dosegli smo najve�jo dovoljeno globino, zato
			// ne vrnemo poteze, ampak samo oceno pozicije
			return new OcenjenaPoteza(
					null,
					Ocena.oceniPozicijo(jaz, igra));
		}
		// Hranimo najbolj�o do sedaj videno potezo in njeno oceno.
		// Tu bi bilo bolje imeti seznam do sedaj videnih najbolj�ih potez, ker je lahko
		// v neki poziciji ve� enakovrednih najbolj�ih potez. Te bi lahko zbrali
		// v seznam, potem pa vrnili naklju�no izbrano izmed najbolj�ih potez, kar bi
		// popestrilo igro ra�unalnika.
		Poteza najboljsa = null;
		int ocenaNajboljse = 0;
		for (Poteza p : igra.moznePoteze()) {
			// V kopiji igre odigramo potezo p
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigrajPotezo(p);
			// Izra�unamo vrednost pozicije po odigrani potezi p
			int ocenaP = minimax(k+1, kopijaIgre).vrednost;
			// �e je p bolj�a poteza, si jo zabele�imo
			if (najboljsa == null // �e nimamo kandidata za najbolj�o potezo
				|| (naPotezi == jaz && ocenaP > ocenaNajboljse) // maksimiziramo
				|| (naPotezi != jaz && ocenaP < ocenaNajboljse) // minimiziramo
				) {
				najboljsa = p;
				ocenaNajboljse = ocenaP;
			}
		}
		// Vrnemo najbolj�o najdeno potezo in njeno oceno
		assert (najboljsa != null);
		return new OcenjenaPoteza(najboljsa, ocenaNajboljse);
	}
}
