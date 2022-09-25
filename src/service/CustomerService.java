package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    public static Map<String, Customer> listOfCustomer = new HashMap<String, Customer>();

    public static void addCustomer(String email, String firstName, String lastName){
        Customer customer = new Customer(firstName, lastName, email);
        listOfCustomer.put(email, customer);
    }

    public static Customer getCustomer(String customerEmail){
        return listOfCustomer.get(customerEmail);
    }

    public static Collection<Customer> getAllCustomers(){
        return listOfCustomer.values();
    }


}
