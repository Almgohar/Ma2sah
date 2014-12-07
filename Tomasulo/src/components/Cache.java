package components;

import java.util.HashMap;

public class Cache {
	int s; // how will this be inputed?
	int l; // size of one block
	int m; // associativity level
	int c; // number of blocks
	int t; // length of tag
	int i; // length of index
	int offset; // length of offset
	int hits;
	int entries;
	boolean writeThrough; // false is write back
	boolean writeAround; // false is write allocate
	int cycles;
	boolean fullyAssociative;
	public HashMap<String, Set> cache = new HashMap<String, Set>();

	// {dirtyBit, validityBit, key, offset, data}

	public Cache(int s, int l, int m, boolean writeThrough,
			boolean writeAround, int cycles) {
		this.s = s;
		this.l = l;
		this.m = m;
		this.c = s / l;
		this.i = (int) (Math.log(c / m) / Math.log(2));
		this.offset = (int) (Math.log(l) / Math.log(2));
		this.t = 16 - (i + offset);
		cache = new HashMap<String, Set>();
		if (i == 0)
			fullyAssociative = true;
		this.writeThrough = writeThrough; // false is write back
		this.writeAround = writeAround; // false is write allocate
		this.cycles = cycles;
	}

	public int getS() {
		return s;
	}

	public int getL() {
		return l;
	}

	public int getM() {
		return m;
	}

	public int getC() {
		return c;
	}

	public int getT() {
		return t;
	}

	public int getI() {
		return i;
	}

	public int getOffset() {
		return offset;
	}

	public int getEntries() {
		return entries;
	}

	public void incrementEntries() {
		entries++;
	}

	public HashMap<String, Set> getCache() {
		return cache;
	}

	public String load(String address) {
		String data = null;
		String[] parsedAddress = breakAddress(address);
		Set theSet = null;
		if (!fullyAssociative)
			theSet = this.getSet(parsedAddress[1]);
		else
			theSet = this.getSet(parsedAddress[0]);
		if (theSet != null) {
			Record record = theSet.getRecord(parsedAddress[0]);
			if (record != null) {
				data = record.getWord(parsedAddress[2]);
			}
		}
		return data;
	}

	public boolean store(String instruction) {
		// setWord(address, Register);
		return false;
	}

	// array = {tag, index, offset, key}
	// where is the address from? the decimal value before turning into binary.
	public String[] breakAddress(String address) {
		String[] array = new String[4];
		System.out.println(this.getT());
		array[0] = address.substring(0, this.getT());
		if (i != 0)
			array[1] = address.substring(t, t + i);
		else
			array[1] = "";
		array[2] = address.substring(t + i);
		array[3] = array[0] + array[1];

		return array;
	}

	public boolean canAddSet(int size) {
		return (cache.size() < size);
	}

	public Set getSet(String index) {
		return cache.get(index);
	}

	public double getHitRatio() {
		return hits / entries;
	}

	public void incrementHits() {
		hits++;

	}

	public int getHits() {
		return hits;

	}
	public int getCycles() {
		return cycles;

	}

	public void addEntry(String setIndex, String data) {
		
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
	public void print(){
		for (String index : cache.keySet()) {
			for(String tag : cache.get(index).records.keySet()){
				for(String offset: cache.get(index).records.get(tag).words.keySet()){
					System.out.print(" " + index + " " + tag + " " + offset + " " + cache.get(index).records.get(tag).words.get(offset));
				}
				
			}
			System.out.println(" ");
		}
	}
	
}
