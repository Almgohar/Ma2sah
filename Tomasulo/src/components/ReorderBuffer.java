package components;

public class ReorderBuffer {
	ROBTuple[] tuples;
	int head;
	int tail;
	int numOfInstructions;
	int size;

	public ReorderBuffer(int size) {
		tuples = new ROBTuple[size];
		head = 0;
		tail = 0;
		numOfInstructions = 0;
		this.size = size;
	}

	public boolean canInsert() {
		return numOfInstructions < size;
	}

	public void insert(String instruction) {
		String[] instr = instruction.split(" ");
		String type = instr[0];
		String destination = instr[1];
		if (instr[0].equals("SD"))
			destination = "MEM";
		ROBTuple tuple = new ROBTuple(type, destination);
		tuples[tail] = tuple;
		tail = (tail + 1) % size;
		numOfInstructions++;

	}
	
	public void commit() {
		tuples[head] = null;
		head = (head + 1) % size;
		numOfInstructions--;
	}

	public ROBTuple getEntry(int index) {
		return tuples[index];
	}

	public void updateState(int index, boolean state) {
		tuples[index].setReady(state);
	}

	public void updateValue(int index, String value) {
		tuples[index].setValue(value);
	}
	public int getHead(){
		return head; 
	}
	public int getTail(){
		return tail; 
	}
	
	public void print(){
		System.out.println();
		System.out.println("Number \t Head \t Tail \t Type \t Dest \t Value \t   Ready \t");
		System.out.println("----------------------------------------------------------------------- ");
		for(int i =0; i<tuples.length; i++){
			ROBTuple tuple = tuples[i]; 
			System.out.println(" ");
			System.out.print(i+1);
			System.out.print("\t");
			if(head==i){
				System.out.print(" YES");
			}
			System.out.print("\t");
			if(tail ==i){	
				System.out.print(" YES");
			}
			if(tuple !=null){
			System.out.print("\t  "  +  tuple.getType()+ "\t  "  + tuple.getDest() + "\t "); 
			if(tuple.getValue()!=""){
				System.out.print(tuple.getValue() + "   " + tuple.isReady() );
			}
			else{
				System.out.print("\t   " + tuple.isReady());  
			}
			}
			}
		
			
		
	}
	public static void main(String[] args) {
		ReorderBuffer Rob = new ReorderBuffer(3);
		Rob.print();
		Rob.insert("LD R1");
		Rob.insert("SD R2");
		Rob.updateValue(1, "R2 + R3");
		Rob.updateState(1, true);
		Rob.print();
		Rob.commit();
		Rob.print();
		Rob.insert("LD R3");
		Rob.print();
		Rob.insert("ADD R1");
	
		Rob.print();
		
		
	}
}
