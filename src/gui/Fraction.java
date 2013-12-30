package gui;

public class Fraction implements Comparable<Fraction> {

	private int denum;

	public Fraction(int denum) {
		this.denum = denum;
	}

	@Override
	public int compareTo(Fraction f) {
		if (denum == f.denum) return 0;
		else if (denum < f.denum) return 1;
		else return -1;
	}
	
	public String toString() {
		return denum + "";
	}
}
