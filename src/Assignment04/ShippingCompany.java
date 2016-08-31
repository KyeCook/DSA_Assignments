package Assignment04;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ShippingCompany {
	private Connection conn;
	private Statement stmt;
	private List<Journey> journeyList;
	
	public ShippingCompany(String dbUser, String dbPassword) {
		try {
			conn = DriverManager.getConnection("jdbc:REMOVED_FOR_PRIVACY_REASONS", dbUser, dbPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<String> readAllPorts() {
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("Select * From Ports");
			List<String> portList = new ArrayList<String>();
			while(rs.next()) {
				portList.add(rs.getString(2));
			}
			stmt.close();
			return portList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<Journey> getAllJourneys(String startPort, String startDate, String endPort) {
		journeyList = new ArrayList<Journey>();
		findPaths(new Journey(), startDate, startPort, endPort);
		return journeyList;
	}
	private void findPaths(Journey currentJourney, String startDate, String startPoint, String endPoint) {
		BoatTrip b;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Shipping WHERE departPort LIKE '"+startPoint+"' AND departDate >= " +startDate);
			Journey temp;
			while(rs.next()) {
				temp = currentJourney.createClone();
				b = new BoatTrip(rs.getString(1),rs.getString(2),rs.getString(4), rs.getString(3), rs.getString(5), rs.getInt(6));
				if(temp.addTrip(b)) {
					if(b.arrivalPort.equals(endPoint)) {
						journeyList.add(temp);
					} else
						findPaths(temp, b.arrivalDate, b.arrivalPort, endPoint);
				}
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public String toString() {
		if(journeyList.isEmpty()) {
			return "No Possible Journeys!";
		}
		String output = "";
		for(Journey i:journeyList) {
			output += (i + "\n\n");
		}
		return output;
	}
	public static void main(String[] args) {
		ShippingCompany a = new ShippingCompany("student", "fpn871");
		System.out.println("Listig of all ports: ");
		List<String> b = a.readAllPorts();
		for(String i:b) {
			System.out.println(i);
		} System.out.println();
		
		System.out.println("Get all trips from Auckland to Samoa: ");
		a.getAllJourneys("Auckland", "01/01/2017", "Samoa");
		System.out.println(a);
		
		System.out.println("\n\nGet all trips from Samoa to Fiji: ");
		a.getAllJourneys("Samoa", "01/01/2017", "Fiji");
		System.out.println(a);
		
		System.out.println("\n\nGet all trips from Auckland to Singapore: ");
		a.getAllJourneys("Auckland", "01/01/2017", "Singapore");
		System.out.println(a);
		a.close();
	}
}
