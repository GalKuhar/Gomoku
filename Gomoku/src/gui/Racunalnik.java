package gui;

import javax.swing.SwingWorker;

import inteligenca.NakljucnaInteligenca;
import logika.Poteza;

public class Racunalnik extends Strateg {
	private GlavnoOkno master;
	private SwingWorker<Poteza,Object> mislec;

	public Racunalnik(GlavnoOkno master) {
		this.master = master;
	}
	
	@Override
	public void na_potezi() {
		// Zaènemo razmišljati
		mislec = new NakljucnaInteligenca(master);
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