package components;

public class RegisterFile {
	public final Register R0;
	public Register R1;
	public Register R2;
	public Register R3;
	public Register R4;
	public Register R5;
	public Register R6;
	public Register R7;
	public Register PC;
	public RegisterFile() {
	    R0 = new Register("0000000000000000");
		R1 = new Register("0000000000000000");
		R2 = new Register("0000000000000000");
	    R3 = new Register("0000000000000000");
	    R4 = new Register("0000000000000000");
		R5 = new Register("0000000000000000");
		R6 = new Register("0000000000000000");
		R7 = new Register("0000000000000000");
		PC = new Register("0000000000000000");
	}
	public void print(){
		System.out.println("R0" + R0.getValue());
		System.out.println("R1" + R1.getValue());
		System.out.println("R2" + R2.getValue());
		System.out.println("R3" + R3.getValue());
		System.out.println("R4" + R4.getValue());
		System.out.println("R5" + R5.getValue());
		System.out.println("R6" + R6.getValue());
		System.out.println("R7" + R7.getValue());
		System.out.println("PC" + PC.getValue());
	}
}
