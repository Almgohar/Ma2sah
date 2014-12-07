package components;

public class Register {
	String value;

	public Register(String value) {
		this.value = value;
	}
	public void setValue(String value){
		this.value=value;
	}
	public String getValue(){
		return value;
	}
	public void clear(){
		this.value= "0000000000000000";
	}
	public void incrementPC(){
		int decimalValue = binaryToDecimal(this.value);
		int newValue=decimalValue+1;
		this.value = decimalToBinary(newValue);
	}
	public int binaryToDecimal(String binary){
		//TODO
		return 0; 
	}
	public String decimalToBinary(int decimal){
		return ""; 
	}
}
