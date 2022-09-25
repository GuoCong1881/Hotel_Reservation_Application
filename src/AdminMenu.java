import api.AdminResource;
import api.HotelResource;
import model.*;
import service.ReservationService;

import java.util.Collection;
import java.util.Scanner;

public class AdminMenu {

    public static void printAdminMenu(){
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a Room");
        System.out.println("5. Back to Main Menu");
    }

    public static int getAdminMenuOption(){
        boolean validInput = false;
        int adminMenuOption = 0;
        while (!validInput) {
            Scanner adminMenuInput = new Scanner(System.in);
            try{
                adminMenuOption = adminMenuInput.nextInt();
                if (adminMenuOption < 6){
                    validInput = true;
                } else {
                    System.out.println("Please choose from 1 to 5.");
                }
            } catch (Exception e) {
                System.out.println("invalid input for option, please type in an integer.");
            }
        }
        return adminMenuOption;
    }

    public static void adminMenuEngine(){
        printAdminMenu();
        int adminMenuOption = getAdminMenuOption();
        switch (adminMenuOption) {
            case 1:
                do {
                    getAllCustomers();
                } while (!backToMain());
                adminMenuEngine();

            case 2:
                do {
                    getAllRooms();
                } while (!backToMain());
                adminMenuEngine();

            case 3:
                do {
                    getAllReservations();
                } while (!backToMain());
                adminMenuEngine();

            case 4:
                do {
                    addARoom();
                } while (!backToMain());
                adminMenuEngine();

            case 5:
                MainMenu.mainMenuEngine();
        }
    }

    public static void getAllCustomers(){
        Collection<Customer> allCustomer = AdminResource.getAllCustomers();
        if (allCustomer.isEmpty()){
            System.out.println("There is no customer information currently. ");
        } else {
            for (Customer customer : allCustomer) {
                System.out.println(customer);
            }
        }
    }

    public static void getAllRooms(){
        Collection<IRoom> allRooms = AdminResource.getAllRooms();
        if (allRooms.isEmpty()){
            System.out.println("There is no room information currently. ");
        } else {
            for (IRoom room : allRooms) {
                System.out.println(room);
            }
        }
    }

    public static void getAllReservations(){
        AdminResource.displayAllReservations();
    }

    public static void addARoom(){
        System.out.println("Please input the room number: ");
        Scanner roomNumInput = new Scanner(System.in);
        String roomNum = roomNumInput.nextLine();

        if (HotelResource.getRoom(roomNum) == null) {
            double roomPrice = getRoomPrice();
            RoomType roomType = getRoomType();
            IRoom room = new Room(roomNum, roomPrice, roomType);
            ReservationService.addRoom(room);
        } else {
            System.out.println("The room number has already existed. ");
        }
    }

    public static double getRoomPrice(){
        System.out.println("Please input the price of the room: ");
        boolean valid = false;
        double roomPrice = 0.0;
        while (!valid) {
            Scanner roomPriceInput = new Scanner(System.in);
            try {
                roomPrice = roomPriceInput.nextDouble();
                valid = true;
            } catch (Exception ex) {
                System.out.println("Please input a number for room price: ");
            }
        }
        return roomPrice;
    }

    public static RoomType getRoomType(){
        System.out.println("Please input the room type (1: Single/2: Double): ");
        boolean valid = false;
        RoomType roomType = null;
        while (!valid) {
            Scanner roomTypeInput = new Scanner(System.in);
            try {
                int roomTypeNum = roomTypeInput.nextInt();
                if (roomTypeNum == 1){
                    roomType = RoomType.SINGLE;
                    valid = true;
                } else if (roomTypeNum == 2) {
                    roomType = RoomType.DOUBLE;
                    valid = true;
                } else {
                    System.out.println("Please input 1 or 2 for room type (1: Single/2: Double): ");
                }
            } catch (Exception ex) {
                System.out.println("Please input 1 or 2 for room type (1: Single/2: Double): ");
            }
        }
        return roomType;
    }

    public static Boolean backToMain(){
        System.out.println("Would you like to go back to admin menu? (y/n)  ('y' to admin menu; 'n' to continue)");
        return getBooleanOption();
    }

    public static boolean getBooleanOption(){
        Scanner continueInput = new Scanner(System.in);
        String continueOption = continueInput.nextLine();
        if (continueOption.equals("y") || continueOption.equals("Y")){
            return true;
        } else if(continueOption.equals("n") || continueOption.equals("N")) {
            return false;
        } else {
            System.out.println("Please response with 'y' or 'n'.");
            return getBooleanOption();
        }
    }
}
