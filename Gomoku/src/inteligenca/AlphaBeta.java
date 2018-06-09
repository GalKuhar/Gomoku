package inteligenca;

import java.util.Collections;
import java.util.LinkedList;
import javax.swing.SwingWorker;
import gui.GlavnoOkno;
import logika.Igra;
import logika.Igralec;
import logika.Poteza;

public class AlphaBeta extends SwingWorker<Poteza, Object>  {

	private GlavnoOkno master;
	private int globina;
	private Igralec jaz;
	
	public AlphaBeta(GlavnoOkno master, int globina, Igralec jaz) {
		this.master = master;
		this.globina = globina;
		this.jaz = jaz;
	}
	
	@Override
	protected Poteza doInBackground() throws Exception {
		Igra igra = master.copyIgra();
		OcenjenaPoteza p = alphaBeta(igra, globina, -1000000000, 1000000000); // 1000000000 je približno infinity,
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
	
	private OcenjenaPoteza alphaBeta(Igra igra, int globina, int alpha, int beta) {
		Igralec naPotezi = null;
		switch (igra.stanje()) {
		case CRNI_NA_POTEZI:
			naPotezi = Igralec.CRNI; break;
		case BELI_NA_POTEZI:
			naPotezi = Igralec.BELI; break;
		case BELI_ZMAGA:
			return new OcenjenaPoteza(null, (jaz == Igralec.BELI ? Ocena.ZMAGA : Ocena.ZGUBA));
		case CRNI_ZMAGA:
			return new OcenjenaPoteza(null, (jaz == Igralec.CRNI ? Ocena.ZMAGA : Ocena.ZGUBA));
		case NEODLOCENO:
			return new OcenjenaPoteza(null, Ocena.NEODLOCENO);
		default:
			break;
		}
		
		if (globina == 0) {
			return new OcenjenaPoteza(null, Ocena.oceniPozicijo(jaz, igra));
		}
		
		Poteza najboljsaPoteza = null;
		LinkedList<Poteza> moznePoteze = igra.moznePoteze();
		Collections.shuffle(moznePoteze);
		
		if (naPotezi == jaz) {
			int najboljsaOcena = -1000000000;
			
			for (Poteza poteza : moznePoteze) {
				Igra kopijaIgre = new Igra(igra);
				kopijaIgre.odigrajPotezo(poteza);
				
				int ocenaPoteze = alphaBeta(kopijaIgre, globina - 1, alpha, beta).vrednost;
				
				// to pravzaprav najboljsaOcena = Math.max(najboljsaOcena, ocenjenaNovaPoteza.vrednost) ki še nastavi novo potezo
				if (najboljsaOcena < ocenaPoteze) {
					najboljsaOcena = ocenaPoteze;
					najboljsaPoteza = poteza;
				}
				alpha = Math.max(alpha, najboljsaOcena);
				if (beta <= alpha) {
					break;
				}
			}
			return new OcenjenaPoteza(najboljsaPoteza, najboljsaOcena);
		} else { //minimiziramo
			int najboljsaOcena = 1000000000;
			
			for (Poteza poteza : moznePoteze) {
				Igra kopijaIgre = new Igra(igra);
				kopijaIgre.odigrajPotezo(poteza);
				
				int ocenaPoteze = alphaBeta(kopijaIgre, globina - 1, alpha, beta).vrednost;
				
				if (najboljsaOcena > ocenaPoteze) {
					najboljsaOcena = ocenaPoteze;
					najboljsaPoteza = poteza;
				}
				beta = Math.min(beta, najboljsaOcena);
				if (beta <= alpha) {
					break;
				}
			}
			return new OcenjenaPoteza(najboljsaPoteza, najboljsaOcena);
		}
	}
	
}
