package Logika;

public enum Igralec {
	Beli,
	Črni;
	
	public Igralec nasprotnik() {
		return (this == Črni ? Beli : Črni);
	}
}
