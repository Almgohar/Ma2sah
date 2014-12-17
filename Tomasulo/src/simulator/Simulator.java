package simulator;
import helpers.Instruction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.BinaryRefAddr;

import components.ALU;
import components.Cache;
import components.CommonDataBus;
import components.Memory;
import components.Parser;
import components.Register;
import components.RegisterFile;
import components.ReorderBuffer;
import components.ReservationStation;
import components.Tuple;

public class Simulator {
	/*** new ***/
	public static int cycles = 0;
	public static ArrayList<ReservationStation> reservationStations;
	ReorderBuffer ROB;
	CommonDataBus CDB;
	static String [] status = {"init","fetch","issue",
			"execute","write","commit"};
	int [] regROB;
	
	/*** ***/
	ArrayList<Cache> caches = new ArrayList<Cache>();
	RegisterFile registerFile = new RegisterFile();
	Parser parser;
	Memory memory;
	ALU alu;
	Cache cache;
	
	public Simulator(int size, HashMap<String,String> iMemory, ArrayList<ReservationStation>reservationStations){
		this.reservationStations = reservationStations;
		this.CDB= new CommonDataBus(false);
		this.regROB = new int [8];
		this.ROB = new ReorderBuffer(size); 
		this.alu = new ALU(registerFile);
		this.memory = new Memory(iMemory);
		this.parser = new Parser(alu, registerFile);
		
	}

	public Simulator(int numOfCaches, ArrayList<Tuple> tuples,
			HashMap<String, String> iMemory, int memoryCycles) {
		for (int i = 0; i < numOfCaches; i++) {
			Tuple tuple = tuples.get(i);
			caches.add(new Cache(tuple.getS(), tuple.getL(), tuple.getM(),
					tuple.isWriteThrough(), tuple.isWriteAround(), tuple
							.getCycles()));
		}
		alu = new ALU(registerFile);
		memory = new Memory(iMemory);
		parser = new Parser(alu, registerFile);
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public static int binaryToDecimal(String binary) {
		int decimal = 0;
		for (int i = binary.length() - 1; i >= 0; i--) {
			decimal += (binary.charAt(i) - '0')
					* Math.pow(2, binary.length() - i - 1);
		}
		return decimal;
	}

	public static String decimalToBinary(int decimal) {
		String binary = "";
		while (decimal != 0) {
			if (decimal % 2 == 0)
				binary = "0" + binary;
			else
				binary = "1" + binary;
			decimal = decimal / 2;
		}
		for (int i = binary.length(); i < 16; i++) {
			binary = "0" + binary;
		}
		return binary;
	}

	public void getHitRatios() {

		// for (int i = 0; i < caches.size(); i++) {
		// double hitRatio = caches.get(i).getHitRatio();
		// System.out.println("Hit ratio of cache " + (i + 1) + hitRatio);
		// }
	}

	public Register getRegister(int index, String[] instArray) {
		switch (instArray[index]) {
		case "R0":
			return registerFile.R0;
		case "R1":
			return registerFile.R1;
		case "R2":
			return registerFile.R2;
		case "R3":
			return registerFile.R3;
		case "R4":
			return registerFile.R4;
		case "R5":
			return registerFile.R5;
		case "R6":
			return registerFile.R6;
		case "R7":
			return registerFile.R7;
		}
		return null;
	}

	public void wBWallStore() {

	}

	public void wTWarrStore() {

	}
	
	
/*
	public void wTWallStore(String address, String newData) {
		int i = 0;
		Set theSet = null;
		Record theRecord = null;
		Record newRecord = null;
		for (i = 0; i < caches.size(); i++) {
			String[] brokenAddress = this.caches.get(i).breakAddress(address);
			theSet = this.caches.get(i).getSet(brokenAddress[1]);
			if (theSet != null) {
				theRecord = theSet.getRecord(brokenAddress[0]);
				if (theRecord != null)
					break;
			}
		}
		if (data == null) {
			data = simulator.memory.getValue("" +registerAddress);
			int offsetSize = simulator.caches.get(simulator.caches.size() - 1).getOffset();
			int wordsToFetch = (int) Math.pow(2, offsetSize);
			String newAddress = decimalToBinary(registerAddress).substring(0, decimalToBinary(registerAddress).length()
					- offsetSize);
			for (int k = 0; k < offsetSize; k++) {
				newAddress += "0";
			}
			String[] brokenAddress = simulator.caches.get(simulator.caches.size()-1).breakAddress(newAddress);
			int decimalAddress = simulator.binaryToDecimal(newAddress);
			
			HashMap<String, String> newRecordM = new HashMap<String, String>();
			
			int startOffset = 0;
			for (int k = 0; k < wordsToFetch;k++) {
				String newData = simulator.memory.getValue(""+ decimalAddress);
				if(decimalAddress == registerAddress)
					savedOffset = decimalToBinary(startOffset).substring(16-offsetSize, 16);
				newRecordM.put(decimalToBinary(startOffset).substring(16-offsetSize, 16), newData);
				System.out.println("some offset " + decimalToBinary(startOffset).substring(16-offsetSize, 16));
				startOffset++; 
				decimalAddress++;
			}
			
			Record newRecord = new Record(newRecordM);
			Set newSet = new Set(9); 
			if(simulator.caches.get(simulator.caches.size()-1).cache.get(brokenAddress[1])==null){
				System.out.println(brokenAddress[1] + "broken");
				simulator.caches.get(simulator.caches.size()-1).cache.put(brokenAddress[1],newSet);
				simulator.caches.get(simulator.caches.size()-1).cache.get(brokenAddress[1]).records.put(brokenAddress[0], newRecord);
			}
			i = simulator.caches.size() - 1;	
		}
		Register destination = simulator.getRegister(1, instArray);
		destination.setValue(decimalToBinary(Integer.parseInt(data)));		
	    Set theSet = null;
		Record theRecord = null;
		Record newRecord = null;
		for (int j = i - 1; j >= 0; j--) {
			int offSetLength = simulator.caches.get(j).getOffset();
			String [] broken = simulator.caches.get(j + 1).breakAddress(decimalToBinary(registerAddress));
			char offset = broken[2].charAt(0);
			HashMap<String, String> words = new HashMap<String, String>(
					(int) Math.pow(2, offSetLength));
			System.out.println("the offset is " + offset);
			int startIndex = 0;
			int endIndex = 0;
			if (offset == '1') {
				System.out.println(offSetLength + "legnth");
				startIndex = offSetLength*2 / 2;
				endIndex = simulator.caches.get(j).getL()-1;
			} else {
				startIndex = 0;
				endIndex = (offSetLength*2 / 2) - 1;
			}
			theRecord = simulator.caches.get(j+1).cache.get(broken[1]).records.get(broken[0]);
			HashMap<String, String> oldWords = theRecord.words;
			int counter=0; 
			for (String key : oldWords.keySet()) {
//				System.out.println(counter);
//				System.out.println("Start index "  + startIndex) ;
//				System.out.println("End index "  + endIndex) ;
//				if (counter >= startIndex && counter <= endIndex) {
					String newOffset = simulator.caches.get(j).breakAddress(decimalToBinary(registerAddress))[2];
					registerAddress++;
					words.put(newOffset, oldWords.get(key));
//				}
				counter++;
			}
			newRecord = new Record(words);
			String newIndex = simulator.caches.get(j).breakAddress(address)[1];
			String newTag = simulator.caches.get(j).breakAddress(address)[0];
			Set newSet = simulator.caches.get(j).cache.get(newIndex);
			if(newSet == null){
				newSet = new Set(9);
				simulator.caches.get(j).cache.put(newIndex, newSet);
			}
			if (!newSet.canAddRecord()) {
				HashMap<String, Record> records = simulator.caches.get(j).cache
						.get(newIndex).records;
				int count = 0;
				for (String key : records.keySet()) {
					if (count == 0) {
						records.remove(records.get(key));
						count++;
						break;
					}
				}
			}
			simulator.caches.get(j).cache.get(newIndex).insertRecord(newTag, newRecord);
		}
		for (int j = 0; j < caches.size(); j++) {
			String[] brokenAddress = caches.get(j).breakAddress(address);
			String elOffset = brokenAddress[2];
			caches.get(j).getSet(brokenAddress[1]).records.put(
					brokenAddress[0], newRecord).words.put(elOffset, newData);
		}
		this.memory.iMemory.put(address, newData);

	}
*/
	public void wBWallUpdate() {

	}

	public void wTWarrUpdate() {

	}
	
	/************************* TOMASULO 7AGAT *******************************************/
	//if any of these returned false, set stall with true
	//if i can't issue, don't issue anything after
	public boolean canIssue(Instruction instruction){
		boolean freeResStat = false;
		String opcode = instruction.getOpcode();
		for(int i = 0; i < reservationStations.size(); i++){
			ReservationStation resStation = reservationStations.get(i);
			if(resStation.getUnit().equals(opcode)){
				if(!resStation.isBusy()){
					freeResStat = true;
					break;
				}
			}
		}
		return (freeResStat && ROB.canInsert());
	}
	
	public boolean canExecute(ReservationStation resStation, Instruction instruction){
		if(instruction.getOpcode().equals("LW")){
			if(instruction.getExecuteCycle()==0){
				if(resStation.getQj()==null && !ROB.hasStore())
					return true; 
				return false; 
			}
			else{
				String address =resStation.getAddress(); 
				ArrayList<String> addresses = ROB.returnStoreAddresses(); 
				if(addresses.size()== 0){
					return true; 
				}
				else{
					for(int i=0; i<addresses.size(); i++){
						if(address.equals(addresses.get(i)))
							return false;
					}
					return true; 
				}
			}
		}
		// check if typo
		if(instruction.getOpcode().equals("SW")){
			if(resStation.getQj()==null){
				//&& ROB.getEntry(ROB.getHead()).getType()=="SD"){
				//can only have one store
				return true; 
			}
			return false; 
		}
		if(resStation.getQj() == null && resStation.getQk() == null
				&& resStation.getVj() != null && resStation.getVk() != null)
			return true;
		return false;
	}

	
	public boolean canCommit(ReservationStation resStation){
		//where index is the index of the required ROB
		int index = Integer.parseInt(resStation.getDest());
		return (ROB.getHead() == index && ROB.isReady(index));
	}
	
	public boolean canWrite(ReservationStation resStation){
		return !(CDB.isBusy());
	}
	
	public void issue(Instruction instruction, ReservationStation resStation, int ROBIndex){
		int rs = instruction.getRS();
		Register regVj;
		Register regVk;
		int rt = instruction.getRT();
		int rd = instruction.getRD();
		String inst = instruction.getInst();
		if(regROB[rs] == -1){
			int h = regROB[rs];
			if(ROB.isReady(h)){
				resStation.setVj(ROB.getValue(h));
				resStation.setQj(null);
			}else{
				resStation.setQj(h+"");
			}
			
		}else{
			
			switch (rs) {
			case 0:
			regVj = registerFile.R0;break;
			case 1:
			regVj = registerFile.R1;break;
			case 2:
			regVj = registerFile.R2;break;
			case 3:
			regVj = registerFile.R3;break;
			case 4:
			regVj = registerFile.R4;break;
			case 5:
			regVj = registerFile.R5;break;
			case 6:
			regVj = registerFile.R6;break;
			case 7:
			regVj = registerFile.R7;break;
			default : regVj = null;
			}
			if(regVj != null)
			resStation.setVj(regVj.getValue()); 
			else resStation.setVj(null); 
			resStation.setQj(null);
			
		}
		resStation.setBusy(true);
		resStation.setDest(ROBIndex+"");
		ROB.insert(inst);
		
		if(instruction.getType().equals("FP") || instruction.getType().equals("store")){
			if(regROB[rt] == -1){
				int h = regROB[rt];
				if(ROB.isReady(h)){
					resStation.setVk(ROB.getValue(h));
					resStation.setQk(null);
				}else{
					resStation.setQk(h+"");
				}
				
			}else{
				switch (rt) {
				case 0:
				regVk = registerFile.R0;break;
				case 1:
				regVk = registerFile.R1;break;
				case 2:
				regVk = registerFile.R2;break;
				case 3:
				regVk = registerFile.R3;break;
				case 4:
				regVk = registerFile.R4;break;
				case 5:
				regVk = registerFile.R5;break;
				case 6:
				regVk = registerFile.R6;break;
				case 7:
				regVk = registerFile.R7;break;
				default : regVk = null;
				}
				if(regVk != null)
				resStation.setVk(regVk.getValue()); 
				else resStation.setVk(null);
				resStation.setQk(null);
			}
		}
		if(instruction.getType().equals("store")){
			resStation.setAddress(instruction.getImm()+"");
		}
		if(instruction.getType().equals("load")){
			resStation.setAddress(instruction.getImm()+"");
			rt = instruction.getRD();
			regROB[rt] = ROBIndex;
		}
		if(instruction.getType().equals("FP")){
			regROB[rd] = ROBIndex;
		}
	}
	
	public String execute(Instruction instruction){
		ReservationStation station=null;
		String opcode = instruction.getOpcode();
		for(int i = 0; i < reservationStations.size(); i++){
			ReservationStation resStation = reservationStations.get(i);
			if(resStation.getUnit().equals(opcode)){
					station = resStation; 
					break;
				}
		}
		switch(opcode){
		case "LW":
			if(instruction.getExecuteCycleCount()==0){
				String value = decimalToBinary(binaryToDecimal(station.getVj()) + binaryToDecimal(station.getAddress()));
				station.setAddress(value);
				return value; 
			}
			else{
				return memory.getValue(station.getAddress());
			}
		case "SW":
			int index = ROB.getIndex("SW");
			String value  = decimalToBinary(binaryToDecimal(station.getVj()) + binaryToDecimal(station.getAddress()));
			ROB.updateValue(index, value);
			return value;  
		case "JMP":
			 int calc = binaryToDecimal(station.getVj()) + 1 + binaryToDecimal(instruction.getImm()+"");
			registerFile.PC.setValue(decimalToBinary(calc));
			int ROBindex = ROB.getIndex("JMP");
			ROB.jFlush(ROBindex);
			return decimalToBinary(calc);
		case "JALR": 
			calc = binaryToDecimal(station.getVk());
			registerFile.PC.setValue(decimalToBinary(calc));
			ROBindex = ROB.getIndex("JALR");
			Register link = registerFile.getRegister(instruction.getRS());
			link.setValue(decimalToBinary(binaryToDecimal(registerFile.PC.getValue())+1));
			ROB.jFlush(ROBindex);
			return decimalToBinary(calc);
		case "RET": 
			calc = binaryToDecimal(station.getVj());
			registerFile.PC.setValue(decimalToBinary(calc));
			ROBindex = ROB.getIndex("JMP");
			ROB.jFlush(ROBindex);
			return decimalToBinary(calc);
		default: return alu.arithmetic(instruction.getInst());
		}
		
	}

	/*public void executeLoad(){
		
	}
	public void executeStore(){
		
	}*/
	public void write(ReservationStation resStation, String result){
		String b = resStation.getDest();
		CDB.broadCast(b,result);
		
		ROB.updateState(Integer.parseInt(b), true);
		ROB.updateValue(Integer.parseInt(b), result );
		
	}
	public boolean writeStore(ReservationStation resStation, String result){
		if(resStation.getQk() == null){
			String b = resStation.getDest();
			ROB.updateValue(Integer.parseInt(b), result );
			return true;
		}
			return false;
	}

	public void commit(Instruction instruction) {
		String d = ROB.getEntry(ROB.getHead()).getDest();
		String value = ROB.getValue(ROB.getHead());
		String type = instruction.getOpcode();
		if (type.equals("BEQ")) {

		} else if (type.equals("SW")) {
			String regVal = getRegister(d).getValue();
			memory.iMemory.put(regVal, value);
		} else {
			Register reg = getRegister(d);
			reg.setValue(value);

		}
		ROB.getEntry(ROB.getHead()).setReady(true);
		ROB.commit();
		for(int i =0; i<regROB.length;i++){
			if(regROB[i]==getRegisterInt(d)){
				regROB[i]=-1; 
			}
		}
		for(int i=0; i<reservationStations.size();i++){
			if(reservationStations.get(i).equals("" +getRegisterInt(d))){
				reservationStations.get(i).setBusy(false);
			}
		}

	}
	
	public Register getRegister(String register) {
		switch (register) {
		case "R0":
			return registerFile.R0;
		case "R1":
			return registerFile.R1;
		case "R2":
			return registerFile.R2;
		case "R3":
			return registerFile.R3;
		case "R4":
			return registerFile.R4;
		case "R5":
			return registerFile.R5;
		case "R6":
			return registerFile.R6;
		case "R7":
			return registerFile.R7;
		}
		return null;
	}
	
	public int getRegisterInt(String register) {
		switch (register) {
		case "R0":
			return 0;
		case "R1":
			return 1;
		case "R2":
			return 2;
		case "R3":
			return 3;
		case "R4":
			return 4;
		case "R5":
			return 5;
		case "R6":
			return 6;
		case "R7":
			return 7;
		}
		return -1;
	}
	
	
	/**
	 * @throws IOException 
	 * @throws NumberFormatException *********************** *******************************************/
	/*public static void main(String[] args) throws NumberFormatException,

			IOException {
		int latency = 0;

		
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		int numberOfCaches = Integer.parseInt(bf.readLine());
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		int n = numberOfCaches;
		while (n > 0) {
			Tuple tuple = new Tuple();
			tuple.setS(Integer.parseInt(bf.readLine()));
			tuple.setL(Integer.parseInt(bf.readLine()));
			tuple.setM(Integer.parseInt(bf.readLine()));
			tuple.setCycles(Integer.parseInt(bf.readLine()));
			tuple.setWriteThrough(Boolean.parseBoolean(bf.readLine()));
			tuple.setWriteAround(Boolean.parseBoolean(bf.readLine()));
			tuples.add(tuple);
			n--;
		}

		int memoryCycles = Integer.parseInt(bf.readLine());
		HashMap<String, String> iMemory = new HashMap<String, String>();
		int numOfDataEntries = Integer.parseInt(bf.readLine());
		while (numOfDataEntries > 0) {
			String memoryData = bf.readLine();
			String[] splitData = memoryData.split(" ");
			iMemory.put(splitData[0], splitData[1]);
			numOfDataEntries--;
		}

		String theAddress = bf.readLine();
		String startAddress = decimalToBinary(Integer.parseInt(theAddress));
		Simulator simulator = new Simulator(numberOfCaches, tuples, iMemory,
				memoryCycles);

		simulator.registerFile.PC.setValue(decimalToBinary(Integer
				.parseInt(startAddress)));
		Arrays.fill(simulator.regROB,-1);
		String address = startAddress;
		int numberOfInstructions = Integer.parseInt(bf.readLine());
		int numOfInstructions = numberOfInstructions;
		while (numberOfInstructions > 0) {
			String instruction = bf.readLine();
			simulator.memory.iMemory.put(address, instruction);
			int decimalAddress = simulator.binaryToDecimal(address);
			decimalAddress++;
			address = decimalToBinary(decimalAddress);
			numberOfInstructions--;

		}
		while (numOfInstructions > 0) {
			String instruction = simulator.memory.iMemory.get(startAddress);
			String[] instArray = instruction.split(" ");
			int registerAddress;
			switch (instArray[0]) {
			case "LW":
				simulator.setCache(simulator.caches.get(0));
				registerAddress = Integer.parseInt(simulator.getRegister(2,
						instArray).getValue())
						+ Integer.parseInt(instArray[3]);
				String data = simulator.cache.load(decimalToBinary(registerAddress));
				
				//latency += simulator.caches.get(0).getCycles();
				int i = 1;
				while (data == null && i < simulator.caches.size()) {
					simulator.setCache(simulator.caches.get(i));
					latency += simulator.caches.get(i).getCycles();
					String theRegisterAddress = simulator.decimalToBinary(registerAddress);
					data = simulator.cache.load(theRegisterAddress);
					simulator.cache.incrementEntries();
					if (data != null){
						simulator.cache.incrementHits();
					break;
					}
					i++;
				}
				String savedOffset=""; 
				//get data from memory and put it in the first cache level
				if (data == null) {
					data = simulator.memory.getValue("" +registerAddress);
					int offsetSize = simulator.caches.get(simulator.caches.size() - 1).getOffset();
					int wordsToFetch = (int) Math.pow(2, offsetSize);
					String newAddress = decimalToBinary(registerAddress).substring(0, decimalToBinary(registerAddress).length()
							- offsetSize);
					for (int k = 0; k < offsetSize; k++) {
						newAddress += "0";
					}
					String[] brokenAddress = simulator.caches.get(simulator.caches.size()-1).breakAddress(newAddress);
					int decimalAddress = simulator.binaryToDecimal(newAddress);
					
					HashMap<String, String> newRecordM = new HashMap<String, String>();
					
					int startOffset = 0;
					for (int k = 0; k < wordsToFetch;k++) {
						String newData = simulator.memory.getValue(""+ decimalAddress);
						if(decimalAddress == registerAddress)
							savedOffset = decimalToBinary(startOffset).substring(16-offsetSize, 16);
						newRecordM.put(decimalToBinary(startOffset).substring(16-offsetSize, 16), newData);
						System.out.println("some offset " + decimalToBinary(startOffset).substring(16-offsetSize, 16));
						startOffset++; 
						decimalAddress++;
					}
					
					Record newRecord = new Record(newRecordM);
					Set newSet = new Set(9); 
					if(simulator.caches.get(simulator.caches.size()-1).cache.get(brokenAddress[1])==null){
						System.out.println(brokenAddress[1] + "broken");
						simulator.caches.get(simulator.caches.size()-1).cache.put(brokenAddress[1],newSet);
						simulator.caches.get(simulator.caches.size()-1).cache.get(brokenAddress[1]).records.put(brokenAddress[0], newRecord);
					}
					i = simulator.caches.size() - 1;	
				}
				Register destination = simulator.getRegister(1, instArray);
				destination.setValue(decimalToBinary(Integer.parseInt(data)));		
			    Set theSet = null;
				Record theRecord = null;
				Record newRecord = null;
				for (int j = i - 1; j >= 0; j--) {
					int offSetLength = simulator.caches.get(j).getOffset();
					String [] broken = simulator.caches.get(j + 1).breakAddress(decimalToBinary(registerAddress));
					char offset = broken[2].charAt(0);
					HashMap<String, String> words = new HashMap<String, String>(
							(int) Math.pow(2, offSetLength));
					System.out.println("the offset is " + offset);
					int startIndex = 0;
					int endIndex = 0;
					if (offset == '1') {
						System.out.println(offSetLength + "legnth");
						startIndex = offSetLength*2 / 2;
						endIndex = simulator.caches.get(j).getL()-1;
					} else {
						startIndex = 0;
						endIndex = (offSetLength*2 / 2) - 1;
					}
					theRecord = simulator.caches.get(j+1).cache.get(broken[1]).records.get(broken[0]);
					HashMap<String, String> oldWords = theRecord.words;
					int counter=0; 
					for (String key : oldWords.keySet()) {
//						System.out.println(counter);
//						System.out.println("Start index "  + startIndex) ;
//						System.out.println("End index "  + endIndex) ;
//						if (counter >= startIndex && counter <= endIndex) {
							String newOffset = simulator.caches.get(j).breakAddress(decimalToBinary(registerAddress))[2];
							registerAddress++;
							words.put(newOffset, oldWords.get(key));
//						}
						counter++;
					}
					newRecord = new Record(words);
					String newIndex = simulator.caches.get(j).breakAddress(address)[1];
					String newTag = simulator.caches.get(j).breakAddress(address)[0];
					Set newSet = simulator.caches.get(j).cache.get(newIndex);
					if(newSet == null){
						newSet = new Set(9);
						simulator.caches.get(j).cache.put(newIndex, newSet);
					}
					if (!newSet.canAddRecord()) {
						HashMap<String, Record> records = simulator.caches.get(j).cache
								.get(newIndex).records;
						int count = 0;
						for (String key : records.keySet()) {
							if (count == 0) {
								records.remove(records.get(key));
								count++;
								break;
							}
						}
					}
					simulator.caches.get(j).cache.get(newIndex).insertRecord(newTag, newRecord);
				}
				for(int l =0; l<simulator.caches.size(); l++){
					System.out.println("CACHE NUMBER " + (l+1));
					simulator.caches.get(l).print();
					System.out.println(" ");
				}
				
				break;		
			case "SW":
				 i = 0;
				 simulator.setCache(simulator.caches.get(0));
					registerAddress = Integer.parseInt(simulator.getRegister(2,
							instArray).getValue())
							+ Integer.parseInt(instArray[3]);
				data = simulator.cache.load(decimalToBinary(registerAddress));
				theSet = null;
				theRecord = null;
				newRecord = null;
				while (data == null && i < simulator.caches.size()) {
					simulator.setCache(simulator.caches.get(i));
					latency += simulator.caches.get(i).getCycles();
					String theRegisterAddress = simulator.decimalToBinary(registerAddress);
					data = simulator.cache.load(theRegisterAddress);
					simulator.cache.incrementEntries();
					if (data != null){
						simulator.cache.incrementHits();
					break;
					}
					i++;
				}
				for (i = 0; i < simulator.caches.size(); i++) {
					String[] brokenAddress = simulator.caches.get(i).breakAddress(address);
					theSet = simulator.caches.get(i).getSet(brokenAddress[1]);
					if (theSet != null) {
						theRecord = theSet.getRecord(brokenAddress[0]);
						if (theRecord != null)
							break;
					}
				}
				if (data == null) {
					data = simulator.memory.getValue("" +registerAddress);
					int offsetSize = simulator.caches.get(simulator.caches.size() - 1).getOffset();
					int wordsToFetch = (int) Math.pow(2, offsetSize);
					String newAddress = decimalToBinary(registerAddress).substring(0, decimalToBinary(registerAddress).length()
							- offsetSize);
					for (int k = 0; k < offsetSize; k++) {
						newAddress += "0";
					}
					String[] brokenAddress = simulator.caches.get(simulator.caches.size()-1).breakAddress(newAddress);
					int decimalAddress = simulator.binaryToDecimal(newAddress);
					
					HashMap<String, String> newRecordM = new HashMap<String, String>();
					
					int startOffset = 0;
					for (int k = 0; k < wordsToFetch;k++) {
						String newData = simulator.memory.getValue(""+ decimalAddress);
						if(decimalAddress == registerAddress)
							savedOffset = decimalToBinary(startOffset).substring(16-offsetSize, 16);
						newRecordM.put(decimalToBinary(startOffset).substring(16-offsetSize, 16), newData);
						System.out.println("some offset " + decimalToBinary(startOffset).substring(16-offsetSize, 16));
						startOffset++; 
						decimalAddress++;
					}
					
					 newRecord = new Record(newRecordM);
					Set newSet = new Set(9); 
					if(simulator.caches.get(simulator.caches.size()-1).cache.get(brokenAddress[1])==null){
						System.out.println(brokenAddress[1] + "broken");
						simulator.caches.get(simulator.caches.size()-1).cache.put(brokenAddress[1],newSet);
						simulator.caches.get(simulator.caches.size()-1).cache.get(brokenAddress[1]).records.put(brokenAddress[0], newRecord);
					}
					i = simulator.caches.size() - 1;	
				}
				destination = simulator.getRegister(1, instArray);
				destination.setValue(decimalToBinary(Integer.parseInt(data)));		
			    theSet = null;
				theRecord = null;
				newRecord = null;
				for (int j = i - 1; j >= 0; j--) {
					int offSetLength = simulator.caches.get(j).getOffset();
					String [] broken = simulator.caches.get(j + 1).breakAddress(decimalToBinary(registerAddress));
					char offset = broken[2].charAt(0);
					HashMap<String, String> words = new HashMap<String, String>(
							(int) Math.pow(2, offSetLength));
					System.out.println("the offset is " + offset);
					int startIndex = 0;
					int endIndex = 0;
					if (offset == '1') {
						System.out.println(offSetLength + "legnth");
						startIndex = offSetLength*2 / 2;
						endIndex = simulator.caches.get(j).getL()-1;
					} else {
						startIndex = 0;
						endIndex = (offSetLength*2 / 2) - 1;
					}
					theRecord = simulator.caches.get(j+1).cache.get(broken[1]).records.get(broken[0]);
					HashMap<String, String> oldWords = theRecord.words;
					int counter=0; 
					for (String key : oldWords.keySet()) {
//						System.out.println(counter);
//						System.out.println("Start index "  + startIndex) ;
//						System.out.println("End index "  + endIndex) ;
//						if (counter >= startIndex && counter <= endIndex) {
							String newOffset = simulator.caches.get(j).breakAddress(decimalToBinary(registerAddress))[2];
							registerAddress++;
							words.put(newOffset, oldWords.get(key));
//						}
						counter++;
					}
					newRecord = new Record(words);
					String newIndex = simulator.caches.get(j).breakAddress(address)[1];
					String newTag = simulator.caches.get(j).breakAddress(address)[0];
					Set newSet = simulator.caches.get(j).cache.get(newIndex);
					if(newSet == null){
						newSet = new Set(9);
						simulator.caches.get(j).cache.put(newIndex, newSet);
					}
					if (!newSet.canAddRecord()) {
						HashMap<String, Record> records = simulator.caches.get(j).cache
								.get(newIndex).records;
						int count = 0;
						for (String key : records.keySet()) {
							if (count == 0) {
								records.remove(records.get(key));
								count++;
								break;
							}
						}
					}
					simulator.caches.get(j).cache.get(newIndex).insertRecord(newTag, newRecord);
				}
				for (int j = 0; j < simulator.caches.size(); j++) {
					String[] brokenAddress = simulator.caches.get(j).breakAddress(address);
					String elOffset = brokenAddress[2];
					simulator.caches.get(j).getSet(brokenAddress[1]).records.put(
							brokenAddress[0], newRecord).words.put(elOffset, data);
				}
				//this.memory.iMemory.put(address, data);
				break;
			default:
				simulator.parser.parse(instruction);
			}
			int decimalAddress = Integer.parseInt(startAddress);
			decimalAddress++;
			startAddress = "" + decimalAddress;
			numOfInstructions--;
			//simulator.caches.get(0).print();
		}
		simulator.getHitRatios();
		System.out.println(latency);
	}*/
	public static double getIPC(int instructions) {
		return (double)instructions/(double)cycles;
	}
	public static int getExecutionCycles(String instruction, int [] latencies){
		String [] inst = instruction.split(" "); 
		switch(inst[0]){
		case "LW":
			return latencies[0]; 
		case "SW":
			return latencies[1]; 
		case "JMP":
			return latencies[2]; 
		case "BEQ":
			return latencies[3]; 
		case "JALR":
			return latencies[4]; 
		case "RET":
			return latencies[5]; 
		case "ADD":
			return latencies[6]; 
		case "SUB":
			return latencies[7]; 
		case "ADDI":
			return latencies[8]; 
		case "NAND":
			return latencies[9]; 
		case "MUL":
			return latencies[10];
		}
		return 0;
	}
	public static boolean checkDone(boolean[] doneArray){
		boolean done =true; 
		for(int i=0; i<doneArray.length; i++){
			if(doneArray[i]==false){
				done =false; 
			break;
			}
		}
		return done; 
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
	BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
	
	// getting start address
	String theAddress = bf.readLine();
	// memory accessed using binary indices?
	String startAddress = decimalToBinary(Integer.parseInt(theAddress));
	// just to preserve the start Address for initializing pc
	String addressCounter = startAddress;
	
	int num = Integer.parseInt(bf.readLine());
	int[] latencies = new int [11]; 
	Instruction [] instructions = new Instruction[num];
	boolean [] done = new boolean[num];
	HashMap<String,String> iMemory = new HashMap<String, String>();
	int ROBSize = Integer.parseInt(bf.readLine()); 
	
	for(int i =0; i<11; i++){
		latencies[i] = Integer.parseInt(bf.readLine());
	}
	int currentAddress;
	for(int i=0; i<num; i++){
		String instruction =bf.readLine();
		
		// store instructions in memory
		iMemory.put(addressCounter, instruction);
		currentAddress = binaryToDecimal(startAddress);
		currentAddress++;
		addressCounter = decimalToBinary(currentAddress);
		
		instructions[i]=new Instruction(instruction, status[0],false,getExecutionCycles(instruction, latencies));
	}
	 
	ArrayList<ReservationStation> resStation = new ArrayList<ReservationStation>(11); 
	String [] araf = {"LW","SW","BEQ","JALR","JMP","RET","ADD","ADDI","SUB","MUL","NAND"};
	for(int i = 0 ; i <11; i++){
		resStation.add(new ReservationStation(araf[i]));
	}
	Simulator simulator = new Simulator(ROBSize,iMemory, resStation);
	
	// set initial value of pc
	simulator.registerFile.PC.setValue(decimalToBinary(Integer
			.parseInt(startAddress)));
	
	while(!checkDone(done)){
		cycles ++;
		for(int i=0; i<instructions.length; i++){
			if(!done[i]){
				ReservationStation station = null;
				String opcode = instructions[i].getOpcode();
				//switch ha ha ha -.-
				switch(opcode) {
				case "LW": station = resStation.get(0);
					break;
				case "SW": station = resStation.get(1);
					break;
				case "BEQ": station = resStation.get(2);
					break;
				case "JALR": station = resStation.get(3);
					break;
				case "JMP": station = resStation.get(4);
					break;
				case "RET": station = resStation.get(5);
					break;
				case "ADD": station = resStation.get(6);
					break;
				case "ADDI": station = resStation.get(7);
					break;
				case "SUB": station = resStation.get(8);
					break;
				case "MUL": station = resStation.get(9);
					break;
				case "NAND": station = resStation.get(10);
					break;	
				}
				// calculate the index of the instruction by subtracting 
				// the current pc from origin?
				// howa keda keda el issuing bl tarteeb fa mmkin n-issue bl pc
				// problem with new iteration, will deal with the instructions fl nos as not issued?
				// or because to get the new pc address we need to finish execution of branch?
				// or at least part of calculating address?
				int calc;
				int currentinst = binaryToDecimal(simulator.registerFile.PC.getValue())-Integer.parseInt(theAddress);
				System.out.println(currentinst);
				if(currentinst<instructions.length && instructions[currentinst].getStatus().equals(status[0])){
					if(simulator.canIssue(instructions[currentinst])){
						instructions[currentinst].setStall(false);
						simulator.issue(instructions[currentinst],station ,simulator.ROB.getTail());
						instructions[currentinst].setStatus(status[1]);
						calc = binaryToDecimal(simulator.registerFile.PC.getValue());
						calc ++;
						simulator.registerFile.PC.setValue(decimalToBinary(calc));
						
					} 
					else{
						instructions[currentinst].setStall(true);
						break;
					}
					}
				else{
					if(instructions[i].getStatus().equals(status[1])){
						if(instructions[i].isFinished()){
							instructions[i].setStatus(status[2]);
						}
						if(simulator.canExecute(station, instructions[i])){
							instructions[i].setStall(false);
							instructions[i].setValue(simulator.execute(instructions[i]));
							instructions[i].incrementExecCycleCount();
						}
						else{
							instructions[i].setStall(true);
						}
				}
					else{
						if(instructions[i].getStatus().equals(status[2])){
							if(simulator.canWrite(station)){
								instructions[i].setStall(false);
								instructions[i].setStatus(status[3]);
								simulator.write(station, instructions[i].getValue());
								}
							else{
								instructions[i].setStall(true);
							}
							}
						else{
							if(simulator.canCommit(station)){
								instructions[i].setStall(false);
								simulator.commit(instructions[i]);
								done[i]=true;
							}
							else{
								instructions[i].setStall(true);
							}
						}
						}
					}
				}
			}
		}
	System.out.println(getIPC(num));
	}
	
	}

