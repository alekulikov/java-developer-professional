package ru.otus;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    private final NavigableMap<Customer, String> customerMap =
            new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> customerEntry = customerMap.firstEntry();
        return customerEntry != null ? customerEntryCopy(customerEntry) : null;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> customerEntry = customerMap.higherEntry(customer);
        return customerEntry != null ? customerEntryCopy(customerEntry) : null;
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer.getCopy(), data);
    }

    private Map.Entry<Customer, String> customerEntryCopy(Map.Entry<Customer, String> customerEntry) {
        return Map.entry(customerEntry.getKey().getCopy(), customerEntry.getValue());
    }
}
