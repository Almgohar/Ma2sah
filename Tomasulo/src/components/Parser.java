package components;

public class Parser {
	ALU alu;
	Cache cache;
	RegisterFile registerFile;

	public Parser(ALU alu, RegisterFile registerFile) {
		this.alu = alu;
		this.registerFile = registerFile;
	}

	public void parse(String instruction) {
		String[] instArray = instruction.split(" ");
		switch (instArray[0]) {
		case "JMP":
			jmpParser(instArray);
			break;
		case "BEQ":
			alu.beq(instruction);
			break;
		case "JALR":
			jalrParser(instArray);
			break;
		case "RET":
			retParser(instArray);
			break;
		case "ADD":
		case "SUB":
		case "ADDI":
		case "NAND":
		case "MUL":
			alu.arithmetic(instruction);
			break;
		default:
			System.out.println("Invalid instruction!!!!");
		}
	}

	public void jmpParser(String[] instArray) {
		String stringValue = getRegister(1, instArray).getValue();
		int value = binaryToDecimal(stringValue);
		int newValue = value + Integer.parseInt(instArray[2])
				+ binaryToDecimal(registerFile.PC.getValue());
		String binaryValue = decimalToBinary(newValue);
		registerFile.PC.setValue(binaryValue);
	}

	public void retParser(String[] instArray) {
		String stringValue = getRegister(1, instArray).getValue();
		int registerValue = binaryToDecimal(stringValue);
		String binaryValue = decimalToBinary(registerValue);
		registerFile.PC.setValue(binaryValue);
	}

	public void jalrParser(String[] instArray) {
		String pcValue = registerFile.PC.value;
		getRegister(1, instArray).setValue(pcValue);
		String regBValue = getRegister(2, instArray).getValue();
		registerFile.PC.setValue(regBValue);
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
