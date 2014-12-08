package components;

public class ROBTuple {
	String type; 
	String dest; 
	String value; 
	boolean ready; 
	public ROBTuple(String type, String dest){
		this.type=type;
		this.ready=false;
		this.dest=dest;
		this.value =""; 
		
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isReady() {
		return ready;
	}
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
}
