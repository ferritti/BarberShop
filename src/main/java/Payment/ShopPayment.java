package Payment;

public class ShopPayment implements PaymentStrategy {
    @Override
    public String makePayment(double amount) {
        return ("Payment will be made at the shop: " + amount + "€");
    }
}

