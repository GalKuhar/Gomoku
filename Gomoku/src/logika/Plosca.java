package logika;

import java.util.LinkedList;

public class Plosca {
	
	// NxN seznam polj
	
	private Polje[][] plosca;
	
	protected int steviloBelih;
	protected int steviloCrnih;
	
	
	public Plosca() {
		super();
		this.plosca = new Polje[Igra.getN()][Igra.getN()];
		
		for (int i = 0 ; i < Igra.getN() ; i++) {
			for (int j = 0 ; j < Igra.getN() ; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
	}
	
	public Polje element(int x, int y) {
		return plosca[x][y];
	}
	
	public void elementSet(int x, int y, Polje polje) {
		plosca[x][y] = polje;
	}
	
	public void prestej() {
		steviloBelih = 0;
		steviloCrnih = 0;
		for (int i = 0 ; i < Igra.getN() ; i++) {
			for (int j = 0 ; j < Igra.getN() ; j++) {
				if (element(i, j) == Polje.BELI) {
					steviloBelih++;
				} else if (element(i, j) == Polje.CRNI) {
					steviloCrnih++;
				}
			}
		}
	}
	
	public Plosca(Plosca plosca2) {
		this.plosca = new Polje[Igra.getN()][Igra.getN()];
		for (int i = 0; i < Igra.getN(); i++) {
			for (int j = 0; j < Igra.getN(); j++) {
				elementSet(i, j, plosca2.element(i, j));
			}
		}
	}
	
	public void oznaciSosede(Plosca plosca) {
		for (int x = 0; x < Igra.getN(); x ++) {
			for (int y = 0; y < Igra.getN(); y ++) {
				if (plosca.element(x, y) != Polje.PRAZNO) {
					for (int dx = -1; dx <= 1; dx ++) {
						for (int dy = -1; dy <= 1; dy ++) {
							if (x + dx >= 0 && x + dx < Igra.getN() && y + dy >= 0 && y + dy < Igra.getN()) {
								if (plosca.element(x + dx, y + dy) == Polje.PRAZNO) 
									plosca.elementSet(x + dx, y + dy, Polje.SOSED);
							}
						}
					}
				}
			}
		}
	}
	
	// tako bomo dobili vsa polja, ki jih je smiselno gledati in ne bo pregledoval zmeraj cele plošče
	public LinkedList<Poteza> sosedi(int globina) {
		Plosca kopijaPlosce = new Plosca(this);
		LinkedList<Poteza> sosedi = new LinkedList<Poteza>();
		for (int i = 0; i < globina; i ++) {
			oznaciSosede(kopijaPlosce);
		}
		
		for (int x = 0; x < Igra.getN(); x ++) {
			for (int y = 0; y < Igra.getN(); y ++) {
				if (kopijaPlosce.element(x, y) == Polje.SOSED) {
					sosedi.add(new Poteza(x, y));
				}
			}
		}
		if (sosedi.size() == 0) sosedi.add(new Poteza(7, 7));
		return sosedi;
	}
	
}