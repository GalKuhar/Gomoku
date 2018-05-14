package logika;

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
	
}