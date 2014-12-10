package components;

public class CommonDataBus {
	CDBTuple dataBusTuple;
	boolean busy;

	public CommonDataBus(CDBTuple dataBusTuple, boolean busy) {
		this.dataBusTuple = dataBusTuple;
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
	

}
