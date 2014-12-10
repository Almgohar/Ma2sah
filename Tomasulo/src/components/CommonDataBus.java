package components;

public class CommonDataBus {
	// Each piece of data on the bus has a source FU represented by the RS
	// and has a destination Register that it needs to be written to
	ReservationStation source;
	String data;
	Register destination;

	public CommonDataBus(ReservationStation source, String data,
			Register destination) {
		this.source = source;
		this.data = data;
		this.destination = destination;
	}

	public ReservationStation getSource() {
		return source;
	}
	// in write phase?
	public void setSource(ReservationStation source) {
		this.source = source;
	}

	public String getData() {
		return data;
	}
	// in write phase?
	public void setData(String data) {
		this.data = data;
	}

	public Register getDestination() {
		return destination;
	}
	// in write phase?
	public void setDestination(Register destination) {
		this.destination = destination;
	}

}
