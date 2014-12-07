package components;

public class ALU {
	RegisterFile registerFile;

	public ALU(RegisterFile registerFile) {
		this.registerFile = registerFile;
	}

	public void arithmetic(String instruction) {
		String[] instArray = instruction.split(" ");
		Register dest = getRegister(1, instArray);
		Register source1 = getRegister(2, instArray);
		Register source2 = getRegister(3, instArray);
		switch (instArray[0]) {
		case "ADD":
			add(dest, source1, source2);
			break;
		case "SUB":
			sub(dest, source1, source2);
			break;
		case "ADDI":
			addi(dest, source1, instArray[3]);
			break;
		case "MUL":
			mul(dest, source1, source2);
			break;
		case "NAND":
			nand(dest, source1, source2);
			break;

		}
		registerFile.print();
		System.out.println(" ");
	}

	private void nand(Register dest, Register source1, Register source2) {
		int source1Value = binaryToDecimal(source1.getValue());
		int source2Value = binaryToDecimal(source2.getValue());
		int newValue = (source1Value & source2Value);
		String theNewValue = decimalToBinary(newValue); 
		String returnString=""; 
		for(int i=0; i<16;i++){
			if (theNewValue.charAt(i)=='0')
				returnString+="1"; 
			else
				returnString+="0";
		}
		
		dest.setValue(returnString);
	}

	private void mul(Register dest, Register source1, Register source2) {
		int source1Value = binaryToDecimal(source1.getValue());
		int source2Value = binaryToDecimal(source2.getValue());
		int newValue = source1Value * source2Value;
		dest.setValue(decimalToBinary(newValue));

	}

	private void addi(Register dest, Register source1, String immediate) {
		int source1Value = binaryToDecimal(source1.getValue());
		int source2Value = Integer.parseInt(immediate);
		int newValue = source1Value + source2Value;
		dest.setValue(decimalToBinary(newValue));

	}

	private void sub(Register dest, Register source1, Register source2) {
		int source1Value = binaryToDecimal(source1.getValue());
		int source2Value = binaryToDecimal(source2.getValue());
		int newValue = source1Value - source2Value;
		dest.setValue(decimalToBinary(newValue));

	}

	private void add(Register dest, Register source1, Register source2) {
		int source1Value = binaryToDecimal(source1.getValue());
		int source2Value = binaryToDecimal(source2.getValue());
		int newValue = source1Value + source2Value;
		
		dest.setValue(decimalToBinary(newValue));

	}

	public void beq(String instruction) {
		String[] instArray = instruction.split(" ");
		Register source1 = getRegister(1, instArray);
		Register source2 = getRegister(2, instArray);
		int imm = Integer.parseInt(instArray[3]);
		if (source1.getValue().equals(source2.getValue())) {
			int pcValue = binaryToDecimal(registerFile.PC.getValue());
			int newValue = pcValue + imm;
			registerFile.PC.setValue(decimalToBinary(newValue));
		}
	}

	public int binaryToDecimal(String binary) {
		int decimal = 0;
		for (int i = binary.length() - 1; i >= 0; i--) {
			decimal += (binary.charAt(i) - '0')
					* Math.pow(2, binary.length() - i - 1);
		}
		return decimal;
	}

	public static String decimalToBinary(int decimal) {
		String binary = "";
		while (decimal != 0) {
			if (decimal % 2 == 0)
				binary = "0" + binary;
			else
				binary = "1" + binary;
			decimal = decimal / 2;
		}
		for (int i = binary.length(); i < 16; i++) {
			binary = "0" + binary;
		}
		return binary;
	}

	public Register getRegister(int index, String[] instArray) {
		switch (instArray[index]) {
		case "R0":
			return registerFile.R0;
		case "R1":
			return registerFile.R1;
		case "R2":
			return registerFile.R2;
		case "R3":
			return registerFile.R3;
		case "R4":
			return registerFile.R4;
		case "R5":
			return registerFile.R5;
		case "R6":
			return registerFile.R6;
		case "R7":
			return registerFile.R7;
		}
		return null;
	}
}
