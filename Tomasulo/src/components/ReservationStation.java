package components;

import helpers.FunctionalUnit;

public class ReservationStation {
	//FunctionalUnit unit; 
	String unit; 
	String operation; // esmha wa7da wa7da zai opcode
	String vj;
	String vk;
	String qj;
	String qk;
	String dest;
	String address;
	boolean busy;
	
	public String getUnit() {
		return unit;
	}

	public String getOperation() {
		return operation;
	}

	public String getVj() {
		return vj;
	}

	public String getVk() {
		return vk;
	}


	public String getQj() {
		return qj;
	}

	public String getQk() {
		return qk;
	}

	public String getDest() {
		return dest;
	}


	public String getAddress() {
		return address;
	}


	public boolean isBusy() {
		return busy;
	}


	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setVj(String vj) {
		this.vj = vj;
	}

	public void setVk(String vk) {
		this.vk = vk;
	}

	public void setQj(String qj) {
		this.qj = qj;
	}

	public void setQk(String qk) {
		this.qk = qk;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public ReservationStation(String unit) {
		this.unit = unit;
	}
	
	
}
