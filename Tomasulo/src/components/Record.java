package components;

import java.util.HashMap;

public class Record {
	//3add el words = 2^offset
	private boolean valid;
	private boolean dirty;
	private boolean hit; //Do we need it? can remove record so what's the point?
	// {offset, data}
	public HashMap<String, String> words;

	// intializing el hashmap eh?
	public Record(HashMap<String, String> words) {
		valid = false;
		dirty = false;
		hit = false;
		this.words = words;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public String getWord(String offset) {
		return words.get(offset);
	}

	public void setWord(String offset, String word) {
		words.put(offset, word);
	}
	
	

}
