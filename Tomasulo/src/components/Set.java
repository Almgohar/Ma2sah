package components;

import java.util.HashMap;

public class Set {
	int size;
	public HashMap<String, Record> records; 
	public Set(int size) {
		this.size = size;
		records = new HashMap<String,Record>(); 
	}
	public boolean canAddRecord(){
		return(records.size()<size);
	}
	public void insertRecord(String tag, Record record){
		records.put(tag, record);
	}
	public Record getRecord(String tag){
		return records.get(tag);
	}
	
}
