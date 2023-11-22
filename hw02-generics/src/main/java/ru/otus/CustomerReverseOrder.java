package ru.otus;

import java.util.Deque;
import java.util.LinkedList;

public class CustomerReverseOrder {

    private final Deque<Customer> customerDeque = new LinkedList<>();

    public void add(Customer customer) {
        customerDeque.addLast(customer);
    }

    public Customer take() {
        return customerDeque.removeLast();
    }
}
