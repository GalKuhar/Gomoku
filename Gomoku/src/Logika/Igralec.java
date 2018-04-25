package Logika;

public enum Igralec {
	BELI,
	CRNI;
	
	public Igralec nasprotnik() {
		return (this == CRNI ? BELI : CRNI);
	}
}
