package strategypattern.impl;

import strategypattern.PaymentStrategy;

public class CreditCardPayment implements PaymentStrategy {

    @Override
    public void makePayment(int amount) {
        System.out.println("Amount spent via cc is " + amount + ".");
    }
}
