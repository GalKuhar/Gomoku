package gui;

import logika.Igralec;
import logika.Poteza;

public class Clovek extends Strateg {
	private GlavnoOkno master;
	
	public Clovek(GlavnoOkno master, Igralec jaz) {
		this.master = master;
	}
	
	@Override
	public void na_potezi() {
	}

	@Override
	public void prekini() {
	}

	@Override
	public void klik(int i, int j) {
		master.odigraj(new Poteza(i, j));
	}

}