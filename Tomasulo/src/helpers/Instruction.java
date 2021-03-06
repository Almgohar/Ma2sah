package helpers;

public class Instruction {
	// /String opcode; // add sub load etc
	// String type; // FP load store
	String status;
	boolean stall;
	int executeCycle;
	int executeCycleCount;
	String instruction; //instruction coming from the simulator
	boolean immediate; //checks if the instruction is an i-instrction
	String[] inst;
	String value;

	public Instruction(String instruction, String status, boolean stall,
			int executeCycle) {
		this.instruction = instruction;
		this.status = status; // will be sent from simulator
								// as status[0] which is init
		stall = false;
		this.executeCycle = executeCycle;
		this.inst = instruction.split(" ");
		if(inst[0].equalsIgnoreCase("addi"))
				immediate = true;
			
	}

	public String getInst() {
		return instruction;
	}

	public int getImm() {
		if(immediate)
			return Integer.parseInt(inst[3]);
		return 0;
	}

	public int getRS() {
		// need to get rid of R
		return Integer.parseInt(inst[2].substring(1));
	}

	public int getRT() {
		// need to get rid of R
		if(!immediate)
			return Integer.parseInt(inst[3].substring(1));
		return -1;
	}

	public boolean isImmediate() {
		return immediate;
	}

	public int getRD() {
		// need to get rid of R
		return Integer.parseInt(inst[1].substring(1));
	}

	public String getType() {
		switch (inst[0]) {
		case "ADD":
			return "FP";
		case "SUB":
			return "FP";
		case "ADDI":
			immediate = true;
			return "FP";
		case "MUL":
			return "FP";
		case "LW":
			return "load";
		case "SW":
			return "store";
			
		// added to get branch type of instruction
		case "BEQ":
			return "condBranch";
		case "JMP":
			return "uncondBranch";
		case "JAL":
			return "uncondBranch";
		case "RET" :
			return "return";
		//////////////////
		}
		return "null";
	}

	public String getOpcode() {
		return inst[0];
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

	public void setValue(String value) {
		this.value =value;
		
	}
	public String getValue() {
		return value;
		
	}

}
