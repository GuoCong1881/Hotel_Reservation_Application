import api.AdminResource;
import api.HotelResource;
import model.*;
import service.CustomerService;
import service.ReservationService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class MainMenu {

    public static void main(String[] args) throws ParseException {

        mainMenuEngine();
    }

    public static void printMainMenu(){
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
    }

    public static int getMainMenuOption(){
        boolean validInput = false;
        int mainMenuOption = 0;
        while (!validInput) {
            Scanner mainMenuInput = new Scanner(System.in);
            try{
                mainMenuOption = mainMenuInput.nextInt();
                if (mainMenuOption < 6){
                    validInput = true;
                } else {
                    System.out.println("Please choose from 1 to 5.");
                }
            } catch (Exception e) {
                System.out.println("invalid input for option, please type in an integer.");
            }
        }
        return mainMenuOption;
    }

    public static void mainMenuEngine(){
        printMainMenu();
        int mainMenuOption = getMainMenuOption();
        switch (mainMenuOption) {
            case 1:
                do {
                    findAndReservService();
                } while (!backToMain());
                mainMenuEngine();
                break;
            case 2:
                do {
                    getReservation();
                } while (!backToMain());
                mainMenuEngine();
                break;
            case 3:
                do {
                    createAccount();;
                } while (!backToMain());
                mainMenuEngine();
                break;
            case 4:
                AdminMenu.adminMenuEngine();
                break;
            case 5:
                System.out.println("Thank you for using. ");
                break;
        }
    }

    public static void findAndReservService(){
        Date checkInDate = getCheckInDate();
        Date checkOutDate = getCheckOutDate();
        SimpleDateFormat fmt = new SimpleDateFormat ("yyyy/MM/dd");
        Boolean valid = false;
        while (!valid){
            if (checkOutDate.after(checkInDate)){
                valid = true;
            } else {
                System.out.println("Check out date should be no earlier than check-in date. ");
                checkOutDate = getCheckOutDate();
            }
        }
        Collection <IRoom> availRooms = findARoom(checkInDate, checkOutDate);
        if (availRooms.isEmpty()) {
            System.out.println("There is no room available during the period chosen. ");
            checkInDate = offsetDate(checkInDate, 7);
            checkOutDate = offsetDate(checkOutDate, 7);
            System.out.println("The system is searching the room available from " +
                    fmt.format(checkInDate) + " to " + fmt.format(checkOutDate));
            availRooms = findARoom(checkInDate, checkOutDate);
            if (availRooms.isEmpty()) {
                System.out.println("There is no room available during the period chosen. ");
            }
        }

        if (!availRooms.isEmpty()){
            System.out.println("Would you like to make a reservation? (y/n) ");
            boolean continueReserv = getBooleanOption();
            if (continueReserv) {
                System.out.println("Do you have an account? (y/n) ");
                boolean haveAccount = getBooleanOption();
                if (haveAccount) {
                    String email = getEmail();
                    Customer customer = HotelResource.getCustomer(email);
                    if (customer != null) {
                        System.out.println("Please input the room number: ");
                        IRoom room = getRoom(availRooms);
                        System.out.println("Please confirm (y/n) : You will reserve Room No. " + room.getRoomNumber() + " from "
                                + fmt.format(checkInDate) + " to " + fmt.format(checkOutDate) + ".");
                        Boolean confirm = getBooleanOption();
                        if (confirm) {
                            Reservation reservation = reserveARoom(customer, room, checkInDate, checkOutDate);
                        }
                    } else {
                        System.out.println("The account doesn't exist.");
                        System.out.println("Would you like to create an account? (y/n)");
                        boolean createAccount = getBooleanOption();
                        if (createAccount) {
                            createAccount();
                        }
                    }
                } else {
                    System.out.println("Would you like to create an account? (y/n)");
                    boolean createAccount = getBooleanOption();
                    if (createAccount) {
                        createAccount();
                    }
                }
            }
        }
    }

    public static Collection<IRoom> findARoom(Date checkInDate, Date checkOutDate){
        Collection <IRoom> availRooms = ReservationService.findRooms(checkInDate,checkOutDate);
        if (!availRooms.isEmpty()) {
            System.out.println("Please find the available rooms: ");
            for (IRoom room : availRooms) {
                System.out.println(room);
            }
        }
        return availRooms;
    }

    public static Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        Reservation reservation = ReservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
        System.out.println("You have successfully made a reservation: " + reservation);
        return reservation;
    }

    public static void createAccount(){
        String email = getEmail();
        if (HotelResource.getCustomer(email) == null) {
            System.out.println("Please input your first name: ");
            Scanner firstNameInput = new Scanner(System.in);
            String firstName = firstNameInput.nextLine();
            System.out.println("Please input your last name: ");
            Scanner lastNameInput = new Scanner(System.in);
            String lastName = lastNameInput.nextLine();
            HotelResource.createACustomer(email, firstName, lastName);
            Customer customer = HotelResource.getCustomer(email);
            System.out.println("You have successfully created your account: " + customer);
        } else {
            System.out.println("The account has already existed. ");
        }
    }


    public static Collection<Reservation> getReservation(){
        String email = getEmail();
        Collection<Reservation> reservations = HotelResource.getCustomersReservations(email);
        if (reservations.isEmpty()){
            System.out.println("There is no reservation information currently. ");
        } else {
            for (Reservation reserv: reservations) {
                System.out.println(reserv);
            }
        }
        return reservations;
    }


    public static IRoom getRoom(Collection<IRoom> availRooms){
        boolean valid = false;
        String roomNumber = null;
        IRoom room = null;
        while (!valid) {
            Scanner numberInput = new Scanner(System.in);
            roomNumber = numberInput.nextLine();
            try {
                room = ReservationService.getARoom(roomNumber);
                if (availRooms.contains(room)){
                    valid = true;
                } else {
                    System.out.println("The room chosen is not available, please input another room ID: ");
                }
            } catch (Exception ex){
                System.out.println("Please input valid room number.");
            }
        }
        return room;
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

    public static Date getCheckInDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.setLenient(false);
        boolean validDate = false;
        Date checkInDate = new Date();
        System.out.println("Please input your checkInDate (YYYY/MM/DD): ");
        while (!validDate) {
            Scanner checkInDateInput = new Scanner(System.in);
            try{
                checkInDate = dateFormat.parse(checkInDateInput.nextLine());
                validDate = true;
            } catch (Exception e) {
                System.out.println("invalid input for date, please follow the format YYYY/MM/DD");
            }
        }
        return checkInDate;
    }

    public static Date getCheckOutDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.setLenient(false);
        boolean validDate = false;
        Date checkOutDate = new Date();
        System.out.println("Please input your checkOutDate (YYYY/MM/DD): ");
        while (!validDate) {
            Scanner checkOutDateInput = new Scanner(System.in);
            try{
                checkOutDate = dateFormat.parse(checkOutDateInput.nextLine());
                validDate = true;
            } catch (Exception e) {
                System.out.println("invalid input for date, please follow the format YYYY/MM/DD");
            }
        }
        return checkOutDate;
    }

    public static Date offsetDate(Date date, int days){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date offsetDate = cal.getTime();
        return offsetDate;
    }

    public static String getEmail(){
        System.out.println("Please input your email address: ");
        boolean valid = false;
        String email = null;
        Pattern pattern = Customer.getPattern();
        while (!valid) {
            Scanner emailInput = new Scanner(System.in);
            email = emailInput.nextLine();
            if (pattern.matcher(email).matches()) {
                valid = true;
            } else {
                System.out.println("Please input valid email address (xxx@xx.com): ");
            }
        }
        return email;
    }

    public static Boolean backToMain(){
        System.out.println("Would you like to go back to main menu? ('y' to main menu; 'n' to continue) ");
        return getBooleanOption();
    }
}
