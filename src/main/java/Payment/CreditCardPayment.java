package Payment;

public class CreditCardPayment implements PaymentStrategy {
    @Override
    public String makePayment(double amount) {
        return ("Payment made using Credit Card: " + amount + "€");
    }
}

