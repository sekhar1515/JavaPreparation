package strategypattern;

import strategypattern.impl.CreditCardPayment;
import strategypattern.impl.PhonepePayment;

public class StrategyMain {

    // Strategy pattern is mainly used to elimintate if-else based logic and instantiate
    // at runtime environments
    // It is allowed to define a family of algorithms
    // Encapsulating each one and making them interchangable at run time.
    public static void main(String[] args) {
        PaymentContext paymentContext = new PaymentContext(new CreditCardPayment());
        paymentContext.makePayment(10);

        PaymentContext paymentContext2 = new PaymentContext(new PhonepePayment());
        paymentContext2.makePayment(10);
    }
}
