package Payment;

public class PaymentContext {
    private PaymentStrategy paymentStrategy;

    // Imposta la strategia di pagamento
    public PaymentContext(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    // Esegue il pagamento e restituisce il messaggio
    public String executePayment(double amount) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment method not selected");
        }
        return paymentStrategy.makePayment(amount);
    }
}

