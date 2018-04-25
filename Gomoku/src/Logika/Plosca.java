package Logika;



public class Plosca {
	
	// NxN seznam polj
	
	private Polje[][] plosca;
	
	public Plosca () {
		super();
		this.plosca = new Polje[Igra.N][Igra.N];
		
		for (int i = 0 ; i < Igra.N ; i++) {
			for (int j = 0 ; j < Igra.N ; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
	}

}