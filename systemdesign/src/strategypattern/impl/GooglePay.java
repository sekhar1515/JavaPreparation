package strategypattern.impl;

import strategypattern.PaymentStrategy;

public class GooglePay implements PaymentStrategy {

    @Override
    public void makePayment(int amount) {
        System.out.println("Amount spent via Google Pay is " + amount + ".");
    }
}
