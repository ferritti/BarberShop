package Unit;

import Payment.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentFactoryTest {

    @Test
    void getPaypalPayment() {
        PaymentStrategy strategy = PaymentFactory.getPaymentMethod(PaymentMethod.PAYPAL);
        assertNotNull(strategy);
        assertInstanceOf(PaypalPayment.class, strategy);
    }

    @Test
    void getCreditCardPayment() {
        PaymentStrategy strategy = PaymentFactory.getPaymentMethod(PaymentMethod.CREDIT_CARD);
        assertNotNull(strategy);
        assertInstanceOf(CreditCardPayment.class, strategy);
    }

    @Test
    void getShopPayment() {
        PaymentStrategy strategy = PaymentFactory.getPaymentMethod(PaymentMethod.SHOP);
        assertNotNull(strategy);
        assertInstanceOf(ShopPayment.class, strategy);
    }

    @Test
    void nullPaymentTypeThrows() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            PaymentFactory.getPaymentMethod(null);
        });

        assertEquals("Payment method cannot be null", exception.getMessage());
    }
}