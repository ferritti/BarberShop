package Payment;

public class PaymentFactory {
    public static PaymentStrategy getPaymentMethod(PaymentMethod paymentMethodType) {
        switch (paymentMethodType) {
            case PAYPAL:
                return new PaypalPayment();
            case CREDIT_CARD:
                return new CreditCardPayment();
            case SHOP:
                return new ShopPayment();
            default:
                throw new IllegalArgumentException("Unknown payment type");
        }
    }
}


