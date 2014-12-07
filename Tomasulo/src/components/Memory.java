package components;

import java.util.HashMap;

public class Memory {
	// Accessing memory using decimal values
	public HashMap<String, String> iMemory;
	final int numberOfBlocks = 64 * 1024 / 2;

	public Memory(HashMap<String, String> iMemory) {
		this.iMemory = iMemory;
	}

	public String getValue(String address) {
		return iMemory.get(address);
	}

	public void setValue(String address, String value) {
		iMemory.put(address, value);
	}

}
