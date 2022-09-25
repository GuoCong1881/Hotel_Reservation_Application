package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {
    protected Customer customer;
    protected IRoom room;
    protected Date checkInDate;
    protected Date checkOutDate;

    protected static SimpleDateFormat fmt = new SimpleDateFormat ("yyyy/MM/dd");

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public IRoom getRoom() {
        return room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setRoom(IRoom room) {
        this.room = room;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString(){
        return customer.firstName + " " + customer.lastName + " has a reservation on Room " + room.getRoomNumber()
                + " from " + fmt.format(checkInDate) + " to " + fmt.format(checkOutDate);
    }
}
