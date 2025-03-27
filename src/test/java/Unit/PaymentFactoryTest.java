package Unit;

import Payment.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentFactoryTest {

    @Test
    void testGetPaymentMethod_Paypal() {
        PaymentStrategy strategy = PaymentFactory.getPaymentMethod(PaymentMethod.PAYPAL);
        assertNotNull(strategy);
        assertInstanceOf(PaypalPayment.class, strategy);
    }

    @Test
    void testGetPaymentMethod_CreditCard() {
        PaymentStrategy strategy = PaymentFactory.getPaymentMethod(PaymentMethod.CREDIT_CARD);
        assertNotNull(strategy);
        assertInstanceOf(CreditCardPayment.class, strategy);
    }

    @Test
    void testGetPaymentMethod_Shop() {
        PaymentStrategy strategy = PaymentFactory.getPaymentMethod(PaymentMethod.SHOP);
        assertNotNull(strategy);
        assertInstanceOf(ShopPayment.class, strategy);
    }

    @Test
    void testGetPaymentMethod_NullType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            PaymentFactory.getPaymentMethod(null);
        });

        assertEquals("Payment method cannot be null", exception.getMessage());
    }
}
