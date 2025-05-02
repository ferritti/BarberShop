package Unit;

import Payment.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentTest {

    private PaymentContext paymentContext;
    private PaymentStrategy mockStrategy;

    @BeforeEach
    void setUp() {
        mockStrategy = Mockito.mock(PaymentStrategy.class);
        paymentContext = new PaymentContext(mockStrategy);
    }

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

    @Test
    void paypalPaymentMessage() {
        PaymentStrategy strategy = new PaypalPayment();
        String message = strategy.makePayment(50.0);
        assertEquals("Payment made using PayPal: 50.0€", message);
    }

    @Test
    void creditCardPaymentMessage() {
        PaymentStrategy strategy = new CreditCardPayment();
        String message = strategy.makePayment(100.0);
        assertEquals("Payment made using Credit Card: 100.0€", message);
    }

    @Test
    void shopPaymentMessage() {
        PaymentStrategy strategy = new ShopPayment();
        String message = strategy.makePayment(30.0);
        assertEquals("Payment will be made at the shop: 30.0€", message);
    }

    @Test
    void paymentContextUsesStrategy() {
        when(mockStrategy.makePayment(200.0)).thenReturn("Mock payment of 200€");
        String result = paymentContext.executePayment(200.0);
        assertEquals("Mock payment of 200€", result);
        verify(mockStrategy).makePayment(200.0);
    }

    @Test
    void paymentContextThrowsIfStrategyNull() {
        PaymentContext context = new PaymentContext(null);
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            context.executePayment(100.0);
        });
        assertEquals("Payment method not selected", exception.getMessage());
    }
}