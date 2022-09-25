package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.util.*;

public class ReservationService {

    public static Map<String, IRoom> listOfRoom = new HashMap<String, IRoom>();
    public static Map<String, List<Reservation>> listOfRoomReservation
            = new HashMap<String, List<Reservation>>();

    public static void addRoom(IRoom room){
        listOfRoom.put(room.getRoomNumber(), room);
    }

    public static IRoom getARoom(String roomID){
        return listOfRoom.get(roomID);
    }

    public static Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        Reservation reserv = new Reservation(customer, room, checkInDate, checkOutDate);
        if (listOfRoomReservation.containsKey(room.getRoomNumber())){
            listOfRoomReservation.get(room.getRoomNumber()).add(reserv);
        } else {
            List<Reservation> listOfReserv = new ArrayList<Reservation>();
            listOfReserv.add(reserv);
            listOfRoomReservation.put(room.getRoomNumber(), listOfReserv);
        }
        return reserv;
    }

    public static Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate){
        List<IRoom> availRoom = new ArrayList<IRoom>();
        for (String roomNum: listOfRoom.keySet()){
            boolean availability = false;
            if (!listOfRoomReservation.containsKey(roomNum)){
                availability = true;
            } else {
                for (Reservation reserv: listOfRoomReservation.get(roomNum)){
                    if (((checkInDate.after(reserv.getCheckInDate()) || checkInDate.equals(reserv.getCheckInDate()))&&
                            checkInDate.before(reserv.getCheckOutDate()))||
                    (checkOutDate.after(reserv.getCheckInDate()) && (checkOutDate.before(reserv.getCheckOutDate())
                            || checkOutDate.equals(reserv.getCheckOutDate())))){
                        availability = false;
                        break;
                    }
                    availability = true;
                }
            }
            if (availability){
                availRoom.add(listOfRoom.get(roomNum));
            }
        }
        return availRoom;
    }

    public static Collection<Reservation> getCustomersReservation(Customer customer){
        List<Reservation> customerReserv = new ArrayList<Reservation>();
        for (List<Reservation> roomReserv: listOfRoomReservation.values()){
            for (Reservation reserv: roomReserv){
                if (reserv.getCustomer() == customer){
                    customerReserv.add(reserv);
                }
            }
        }
        return customerReserv;
    }

    public static void printAllReservation(){
        if (listOfRoomReservation.values().isEmpty()){
            System.out.println("There is no reservation information currently. ");
        } else {
            for (List<Reservation> roomReserv : listOfRoomReservation.values()) {
                for (Reservation reserv : roomReserv) {
                    System.out.println(reserv);
                }
            }
        }
    }
}
