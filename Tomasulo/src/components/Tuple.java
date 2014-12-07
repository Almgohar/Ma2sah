package components;

public class Tuple {
	int s;
	int l;
	int m;
	boolean writeThrough; //false is write back
	boolean writeAround; // false is write allocate
	int cycles; 
	public Tuple() {
	}
	
	public boolean isWriteThrough() {
		return writeThrough;
	}

	public void setWriteThrough(boolean writeThrough) {
		this.writeThrough = writeThrough;
	}

	public boolean isWriteAround() {
		return writeAround;
	}

	public void setWriteAround(boolean writeAround) {
		this.writeAround = writeAround;
	}

	public int getCycles() {
		return cycles;
	}
	public void setCycles(int cycles) {
		this.cycles = cycles;
	}
	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}
}
