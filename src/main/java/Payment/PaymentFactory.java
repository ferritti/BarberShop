package Payment;

import Model.PaymentMethod;

public class PaymentFactory {
    public static PaymentStrategy getPaymentMethod(PaymentMethod paymentMethodType) {
        if (paymentMethodType == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }

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


