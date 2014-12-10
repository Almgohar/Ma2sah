package helpers;

public class Instruction {
	String type;
	String status;
	boolean stall;
	int executeCycle;
	int executeCycleCount;

	public Instruction(String type, String status, boolean stall,
			int executeCycle) {
		this.type = type;
		this.status = status; //will be sent from simulator 
							  //as status[0] which is init
		stall = false;
		this.executeCycle = executeCycle;
		
	}

	public String getInstructionType() {
		return type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
