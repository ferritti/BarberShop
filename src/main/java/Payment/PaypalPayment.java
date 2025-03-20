package Payment;

public class PaypalPayment implements PaymentStrategy {
    @Override
    public String makePayment(double amount) {
       return ("Payment made using PayPal: " + amount + "€");
    }
}

