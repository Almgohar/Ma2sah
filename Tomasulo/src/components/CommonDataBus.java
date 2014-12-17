package components;

import simulator.Simulator;

public class CommonDataBus {
	CDBTuple dataBusTuple;
	boolean busy;

	public CommonDataBus(boolean busy) {
		//this.dataBusTuple = dataBusTuple;
		this.busy = busy;
	}

	public CDBTuple getDataBusTuple() {
		return dataBusTuple;
	}

	public void setDataBusTuple(CDBTuple dataBusTuple) {
		this.dataBusTuple = dataBusTuple;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public void broadCast(String b, String result) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 11; i++) {
			if (Simulator.reservationStations.get(i) != null
					&& Simulator.reservationStations.get(i).getQj()!= null && Simulator.reservationStations.get(i).getQj().equals(b)) {
			Simulator.reservationStations.get(i).setQj(null);
			Simulator.reservationStations.get(i).setVj(result);
			}
			if (Simulator.reservationStations.get(i) != null
					&& Simulator.reservationStations.get(i).getQk()!= null && Simulator.reservationStations.get(i).getQk().equals(b)) {
			Simulator.reservationStations.get(i).setQk(null);
			Simulator.reservationStations.get(i).setVk(result);
			}

		}
	}

}
