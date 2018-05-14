package inteligenca;

import java.util.List;
import java.util.Random;
import javax.swing.SwingWorker;

import gui.GlavnoOkno;
import logika.Igra;
import logika.Poteza;

public class NakljucnaInteligenca extends SwingWorker<Poteza, Object> {

	private GlavnoOkno master;
	
	public NakljucnaInteligenca(GlavnoOkno master) {
		this.master = master;
	}
	
	@Override
	protected Poteza doInBackground() throws Exception {
		Igra igra = master.copyIgra();
		for (int i = 0; i < 5; i++) {
			System.out.println("mislim...");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) { }
			if (this.isCancelled()) {
				System.out.println("Prekinili so me!");
				return null;
			}
		}
		System.out.println("Igram");
		Random r = new Random();
		List<Poteza> poteze = igra.moznePoteze();
		Poteza p = poteze.get(r.nextInt(poteze.size()));
		return p;
	}
	
	@Override
	public void done() {
		try {
			Poteza p = this.get();
			if (p != null) { master.odigraj(p); }
		} catch (Exception e) {
		}
	}

}