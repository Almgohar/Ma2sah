package helpers;

public class Instruction {
	String opcode; // add sub load etc
	 String type; // FP load store
	String status;
	boolean stall;
	int executeCycle;
	int executeCycleCount;

	public Instruction(String type, String status, boolean stall,
			int executeCycle, String opcode) {
		this.opcode = opcode;
		this.status = status; //will be sent from simulator 
							  //as status[0] which is init
		stall = false;
		this.executeCycle = executeCycle;
		this.type = type;
	}

	public int getRS(){
		//TODO
		//break instructin nd get rs
		return 0;
	}
	public int getRT(){
		//TODO
		//break instructin nd get rt
		return 0;
	}
	public int getRD(){
		//TODO
		//break instructin nd get rd
		return 0;
	}
	public String getType() {
		return type;
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcde) {
		this.opcode = opcode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isStall() {
		return stall;
	}

	public void setStall(boolean stall) {
		this.stall = stall;
	}

	public int getExecuteCycle() {
		return executeCycle;
	}

	public void setExecuteCycle(int executeCycle) {
		this.executeCycle = executeCycle;
	}

	public int getExecuteCycleCount() {
		return executeCycleCount;
	}

	public void incrementExecCycleCount() {
		executeCycleCount++;
	}

	public boolean isFinished() {
		return executeCycle == executeCycleCount;
	}

}
