package simulator;

import helpers.Instruction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import components.ALU;
import components.Cache;
import components.Memory;
import components.Parser;
import components.Record;
import components.Register;
import components.RegisterFile;
import components.ReorderBuffer;
import components.ReservationStation;
import components.Set;
import components.Tuple;

public class Simulator {
	/*** new ***/
	ArrayList<ReservationStation> reservationStations = new ArrayList<ReservationStation>();
	ReorderBuffer ROB = new ReorderBuffer(7);
	/*** ***/
	ArrayList<Cache> caches = new ArrayList<Cache>();
	RegisterFile registerFile = new RegisterFile();
	Parser parser;
	Memory memory;
	ALU alu;
	Cache cache;

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

	public int binaryToDecimal(String binary) {
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
	
	public boolean canIssue(Instruction instruction){
		boolean freeResStat = false;
		String type = instruction.getInstructionType();
		for(int i = 0; i < reservationStations.size(); i++){
			ReservationStation resStation = reservationStations.get(i);
			if(resStation.getUnit().equals(type)){
				if(!resStation.isBusy()){
					freeResStat = true;
					break;
				}
			}
		}
		return (freeResStat && ROB.canInsert());
	}
	
	public boolean canExecute(ReservationStation resStation){
		if(resStation.getQj() == null && resStation.getQk() == null
				&& resStation.getVj() != null && resStation.getVk() != null)
			return true;
		return false;
	}
	
	public boolean canCommit(ReservationStation resStation, int index){
		return (ROB.getHead() == index && ROB.isReady(index));
	}
	
	/************************* *******************************************/
	public static void main(String[] args) throws NumberFormatException,
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
	}
}
