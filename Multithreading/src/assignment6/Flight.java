/* MULTITHREADING <Flight.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * <Kevin Tong>
 * <kyt259>
 * <17360>
 * Slip days used: <0>
 * Spring 2022
 */
package assignment6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import assignment6.Flight.SeatClass;

public class Flight {
    /**
     * the delay time you will use when print tickets
     */
    private int printDelay; // 50 ms. Use it to fix the delay time between prints.
    private SalesLogs log;
    private String flightNo;
    private Integer customer = 0;
    private List<Ticket> tickets = new ArrayList<>();
    private List<Seat> seats = new ArrayList<>();
    private Queue<Seat> availableFirst = new LinkedList<>();
    private Queue<Seat> availableBusiness = new LinkedList<>();
    private Queue<Seat> availableEconomy = new LinkedList<>();
    
    

    public Flight(String flightNo, int firstNumRows, int businessNumRows, int economyNumRows) {
    	this.printDelay = 50;// 50 ms. Use it to fix the delay time between
    	this.log = new SalesLogs();
        this.flightNo = flightNo;
        int businessRowStart = firstNumRows+1;
        int economyRowStart = firstNumRows+businessNumRows+1;
        int totalRows = firstNumRows + businessNumRows + economyNumRows;
        SeatLetter[] letterList = SeatLetter.values();
        for(int row = 1; row < businessRowStart; row++) {
        	for(int letter = 0; letter < 4; letter++) {
        		Seat seat = new Seat(SeatClass.FIRST, row, letterList[letter]);
        		availableFirst.add(seat);
        	}
        }
        for(int row = businessRowStart; row < economyRowStart; row++) {
        	for(int letter = 0; letter < 6; letter++) {
        		Seat seat = new Seat(SeatClass.BUSINESS, row, letterList[letter]);
        		availableBusiness.add(seat);
        	}
        }
        for(int row = economyRowStart; row <= totalRows; row++) {
        	for(int letter = 0; letter < 6; letter++) {
        		Seat seat = new Seat(SeatClass.ECONOMY, row, letterList[letter]);
        		availableEconomy.add(seat);
        	}
        }
        
    }
    
    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    /**
     * Returns the next available seat not yet reserved for a given class
     *
     * @param seatClass a seat class(FIRST, BUSINESS, ECONOMY)
     * @return the next available seat or null if flight is full
     */
	public synchronized Seat getNextAvailableSeat(SeatClass seatClass) {
		Seat nextSeat = null;
		if(seatClass == SeatClass.FIRST) {
			if(!availableFirst.isEmpty()) {
				nextSeat = availableFirst.poll();
			}
			else if(!availableBusiness.isEmpty()) {
				nextSeat = availableBusiness.poll();
			}
			else if(!availableEconomy.isEmpty()) {
				nextSeat = availableEconomy.poll();
			}
		}
		else if(seatClass == SeatClass.BUSINESS) {
			if(!availableBusiness.isEmpty()) {
				nextSeat = availableBusiness.poll();
			}
			else if(!availableEconomy.isEmpty()) {
				nextSeat = availableEconomy.poll();
			}
		}
		else if(seatClass == SeatClass.ECONOMY) {
			if(!availableEconomy.isEmpty()) {
				nextSeat = availableEconomy.poll();
			}
		}
		if(nextSeat != null) {
			seats.add(nextSeat);
		}
        return nextSeat;
	}

	/**
     * Prints a ticket to the console for the customer after they reserve a seat.
     *
     * @param seat a particular seat in the airplane
     * @return a flight ticket or null if a ticket office failed to reserve the seat
     */
	public synchronized Ticket printTicket(String officeId, Seat seat, int customer) {
        if(seat == null) {
        	return null;
        }
        
        Ticket newTicket = new Ticket(flightNo, officeId, seat, customer + 1);
        System.out.println(newTicket.toString());
        tickets.add(newTicket);
        
        if(availableFirst.peek() == null && availableBusiness.peek() == null && availableEconomy.peek() == null) {
        	System.out.print("Sorry, we are sold out!");
        	return null;
        }
        
        return newTicket;
    }

	/**
     * Lists all seats sold for this flight in order of purchase.
     *
     * @return list of seats sold
     */
    public List<Seat> getSeatLog() {
        
        return seats;
    }

    /**
     * Lists all tickets sold for this flight in order of printing.
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        return tickets;
    }
    
    public class CurrentOffice implements Runnable {
        private String officeId;
        private SeatClass[] seatClasses;
        private Object lock = new Object();

        public CurrentOffice(String officeId, SeatClass[] seatClasses) {
            this.officeId = officeId;
            this.seatClasses = seatClasses;
        }

        // Runnable to iterate through
        @Override
        public void run() {
        	synchronized(customer) {
        		try {
            		for(SeatClass seatClass : seatClasses) {
        				Seat seat = getNextAvailableSeat(seatClass);
        				if(printTicket(officeId, seat, customer) == null) {
        					return;
        				}
       					customer++;
        			}
    	        	Thread.sleep(getPrintDelay());
    	        } catch (InterruptedException e) {
    	        	e.printStackTrace();
    	        }
        	}
        }
        
    }
    
    static enum SeatClass {
		FIRST(0), BUSINESS(1), ECONOMY(2);

		private Integer intValue;

		private SeatClass(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	static enum SeatLetter {
		A(0), B(1), C(2), D(3), E(4), F(5);

		private Integer intValue;

		private SeatLetter(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	/**
     * Represents a seat in the airplane
     * FIRST Class: 1A, 1B, 1E, 1F ... 
     * BUSINESS Class: 2A, 2B, 2C, 2D, 2E, 2F  ...
     * ECONOMY Class: 3A, 3B, 3C, 3D, 3E, 3F  ...
     * (Row numbers for each class are subject to change)
     */
	static class Seat {
		private SeatClass seatClass;
		private int row;
		private SeatLetter letter;

		public Seat(SeatClass seatClass, int row, SeatLetter letter) {
			this.seatClass = seatClass;
			this.row = row;
			this.letter = letter;
		}

		public SeatClass getSeatClass() {
			return seatClass;
		}

		public void setSeatClass(SeatClass seatClass) {
			this.seatClass = seatClass;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public SeatLetter getLetter() {
			return letter;
		}

		public void setLetter(SeatLetter letter) {
			this.letter = letter;
		}

		@Override
		public String toString() {
			return Integer.toString(row) + letter + " (" + seatClass.toString() + ")";
		}
	}

	/**
 	 * Represents a flight ticket purchased by a customer
 	 */
	static class Ticket {
		private String flightNo;
		private String officeId;
		private Seat seat;
		private int customer;
		public static final int TICKET_STRING_ROW_LENGTH = 31;

		public Ticket(String flightNo, String officeId, Seat seat, int customer) {
			this.flightNo = flightNo;
			this.officeId = officeId;
			this.seat = seat;
			this.customer = customer;
		}

		public int getCustomer() {
			return customer;
		}

		public void setCustomer(int customer) {
			this.customer = customer;
		}

		public String getOfficeId() {
			return officeId;
		}

		public void setOfficeId(String officeId) {
			this.officeId = officeId;
		}
		
		@Override
		public String toString() {
			String result, dashLine, flightLine, officeLine, seatLine, customerLine, eol;

			eol = System.getProperty("line.separator");

			dashLine = new String(new char[TICKET_STRING_ROW_LENGTH]).replace('\0', '-');

			flightLine = "| Flight Number: " + flightNo;
			for (int i = flightLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				flightLine += " ";
			}
			flightLine += "|";

			officeLine = "| Ticket Office ID: " + officeId;
			for (int i = officeLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				officeLine += " ";
			}
			officeLine += "|";

			seatLine = "| Seat: " + seat.toString();
			for (int i = seatLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				seatLine += " ";
			}
			seatLine += "|";

			customerLine = "| Client: " + customer;
			for (int i = customerLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				customerLine += " ";
			}
			customerLine += "|";

			result = dashLine + eol + flightLine + eol + officeLine + eol + seatLine + eol + customerLine + eol
					+ dashLine;

			return result;
		}
	}

	/**
	 * SalesLogs are security wrappers around an ArrayList of Seats and one of Tickets
	 * that cannot be altered, except for adding to them.
	 * getSeatLog returns a copy of the internal ArrayList of Seats.
	 * getTicketLog returns a copy of the internal ArrayList of Tickets.
	 */
	static class SalesLogs {
		private ArrayList<Seat> seatLog;
		private ArrayList<Ticket> ticketLog;

		private SalesLogs() {
			seatLog = new ArrayList<Seat>();
			ticketLog = new ArrayList<Ticket>();
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Seat> getSeatLog() {
			return (ArrayList<Seat>) seatLog.clone();
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Ticket> getTicketLog() {
			return (ArrayList<Ticket>) ticketLog.clone();
		}

		public void addSeat(Seat s) {
			seatLog.add(s);
		}

		public void addTicket(Ticket t) {
			ticketLog.add(t);
		}
	}
}
