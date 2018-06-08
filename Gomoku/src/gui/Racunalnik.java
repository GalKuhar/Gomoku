package gui;

import javax.swing.SwingWorker;

import inteligenca.AlphaBeta;
import inteligenca.Minimax;
import logika.Igralec;
import logika.Poteza;

public class Racunalnik extends Strateg {
	private GlavnoOkno master;
	private Igralec jaz;
	private SwingWorker<Poteza,Object> mislec;
	
	private final static int GLOBINA = 3;

	public Racunalnik(GlavnoOkno master, Igralec jaz) {
		this.master = master;
		this.jaz = jaz;
	}
	
	@Override
	public void na_potezi() {
		// Začnemo razmišljati
		mislec = new AlphaBeta(master, GLOBINA, jaz);
		mislec.execute();
	}

	@Override
	public void prekini() {
		if (mislec != null) {
			mislec.cancel(true);
		}
	}

	@Override
	public void klik(int i, int j) {
	}

}