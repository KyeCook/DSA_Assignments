package Assignment04;

import java.util.LinkedList;
import java.util.List;

public class Journey {
	private LinkedList<BoatTrip> boatTripList;
	
	public Journey() {
		boatTripList = new LinkedList<BoatTrip>();
	}
	public Journey(List<BoatTrip> trips) {
		this();
		boatTripList.addAll(trips);
	}
	public boolean addTrip(BoatTrip trip) {
		if(containsPort(trip.arrivalPort)) {
			return false;
		} else
			return boatTripList.add(trip);
	}
	public boolean removeLastTrip() {
		if(!boatTripList.isEmpty()) {
			boatTripList.removeLast();
			return true;
		} else 
			return false; 
	}
	public boolean containsPort(String port) {
		for(BoatTrip i:boatTripList) {
			if(i.departPort.equals(port) || i.arrivalPort.equals(port)) {
				return true;
			}
		}
		return false;
	}
	public String getStartPoint() {
		if(boatTripList == null || boatTripList.isEmpty()) {
			return null;
		} else
			return boatTripList.getFirst().departPort;
	}
	public String getEndPoint() {
		if(boatTripList == null || boatTripList.isEmpty()) {
			return null;
		} else
			return boatTripList.getLast().arrivalPort;
	}
	public String getEndDate() {
		if(boatTripList == null || boatTripList.isEmpty()) {
			return null;
		} else
			return boatTripList.getLast().arrivalDate;
	}
	public Journey createClone() {
		return new Journey(boatTripList);
	}
	public int getTotalJourneyCost() {
		int total = 0;
		for(BoatTrip i:boatTripList) {
			total += i.cost;
		}
		return total;
	}
	public String toString() {
		String output = "";
		for(BoatTrip i:boatTripList) {
			output += i +"\n";
		} output += "Total Cost: " + getTotalJourneyCost();
		return output;
	}
}
