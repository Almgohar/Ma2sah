package components;

import java.util.HashMap;

public class RegisterTable {
	// The register table maps each register to the reservation station using
	// it, otherwise null, needed to check availability of register
	RegisterFile regFile;
	HashMap<Register, ReservationStation> registerTable;

	public RegisterTable() {
		registerTable.put(regFile.R1, null);
		registerTable.put(regFile.R2, null);
		registerTable.put(regFile.R3, null);
		registerTable.put(regFile.R4, null);
		registerTable.put(regFile.R5, null);
		registerTable.put(regFile.R6, null);
		registerTable.put(regFile.R7, null);
	}

	public void setRSEntry(Register r, ReservationStation rs) {
		registerTable.put(r, rs);
	}

	public ReservationStation getRSEntry(Register r) {
		return registerTable.get(r);
	}

}
