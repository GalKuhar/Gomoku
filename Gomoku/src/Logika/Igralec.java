package Logika;

public enum Igralec {
	BELI,
	CRNI;
	
	// ne rabiva
	public Igralec nasprotnik() {
		return (this == CRNI ? BELI : CRNI);
	}
}
