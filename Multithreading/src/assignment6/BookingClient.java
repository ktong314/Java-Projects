/* MULTITHREADING <BookingClient.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * <Kevin Tong>
 * <kyt259>
 * <17360>
 * Slip days used: <0>
 * Spring 2022
 */
package assignment6;

import java.util.Map;

import assignment6.Flight.CurrentOffice;
import assignment6.Flight.Seat;
import assignment6.Flight.SeatClass;
import assignment6.Flight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.lang.Thread;

public class BookingClient {
	
	public Map<String, SeatClass[]> offices;
	public Flight flight;
	public List<String> officeList = new ArrayList<>();
	public List<SeatClass[]> seatList = new ArrayList<>();

	/**
     * @param offices maps ticket office id to seat class preferences of customers in line
     * @param flight the flight for which tickets are sold for
     */
    public BookingClient(Map<String, SeatClass[]> offices, Flight flight) {
    	this.offices = offices;
    	this.flight = flight;
    	for(String office : offices.keySet()) {
    		officeList.add(office);
    	}
    }

    /**
     * Starts the ticket office simulation by creating (and starting) threads
     * for each ticket office to sell tickets for the given flight
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are ticket offices
     */
    public List<Thread> simulate() {
    	List<Thread> threads = new ArrayList<>();
    	
    	Iterator it = offices.entrySet().iterator();
    	while(it.hasNext()) {
    		Map.Entry pair = (Map.Entry)it.next();
    		CurrentOffice current = flight.new CurrentOffice((String)pair.getKey(), (SeatClass[])pair.getValue());
    		Thread currentThread = new Thread(current);
    		currentThread.start();
    		threads.add(currentThread);
    	}
    	
        return threads;
    }

    public static void main(String[] args) {
    	Map<String, SeatClass[]> test_office = new HashMap<>();
    	SeatClass[] testSeat1 = {SeatClass.FIRST, SeatClass.FIRST, SeatClass.FIRST, SeatClass.FIRST, SeatClass.FIRST, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS};
    	SeatClass[] testSeat2 = {SeatClass.ECONOMY, SeatClass.FIRST, SeatClass.ECONOMY, SeatClass.FIRST, SeatClass.ECONOMY};
    	SeatClass[] testSeat3 = {SeatClass.BUSINESS, SeatClass.ECONOMY, SeatClass.ECONOMY, SeatClass.FIRST, SeatClass.BUSINESS};
    	SeatClass[] testSeat4 = {SeatClass.BUSINESS, SeatClass.FIRST, SeatClass.ECONOMY, SeatClass.FIRST, SeatClass.BUSINESS};
    	SeatClass[] testSeat5 = {SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS};
    	
    	

        test_office.put("office1", testSeat1);
        test_office.put("office2", testSeat2);
        test_office.put("office3", testSeat3);
        test_office.put("office4", testSeat4);
        test_office.put("office5", testSeat5);

        Flight test_flight = new Flight("1234", 2, 2, 2);
        BookingClient bc = new BookingClient(test_office, test_flight);


        // Create box offices
        List<Thread> threads = bc.simulate();


        // Join them
        for(Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }
}
