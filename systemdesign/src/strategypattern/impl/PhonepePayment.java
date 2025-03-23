package strategypattern.impl;

import strategypattern.PaymentStrategy;

public class PhonepePayment implements PaymentStrategy {

    @Override
    public void makePayment(int amount) {
        System.out.println("Amount spent via Phone Pay is " + amount + ".");
    }
}
