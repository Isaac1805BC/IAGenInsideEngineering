package eci.edu.byteProgramming.ejercicio.paper.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class auxiliaryTest {

    private Inventory inventory;
    private Facturation facturation;
    private Notification notification;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        facturation = new Facturation();
        notification = new Notification();
    }

    // ===== Abstract Factory - CreditCard =====

    @Test
    void creditCardFactoryCreatesPaymentMethod() {
        PaymentFactory factory = new CreditCardPaymentFactory(
                "4111111111111111", "John Doe", "12/26", "123", "Calle 1");
        PaymentMethod payment = factory.createPaymentMethod(100.0, "CUST001", "Test purchase");
        assertNotNull(payment);
        assertEquals("CREDIT_CARD", payment.getPaymentMethod());
        assertEquals(100.0, payment.getAmount());
    }

    @Test
    void creditCardValidationPasses() {
        PaymentFactory factory = new CreditCardPaymentFactory(
                "4111111111111111", "John Doe", "12/26", "123", "Calle 1");
        assertTrue(factory.createPaymentMethod(100.0, "C1", "Test").validatePaymentMethod());
    }

    @Test
    void creditCardValidationFailsOnInvalidCVV() {
        PaymentFactory factory = new CreditCardPaymentFactory(
                "4111111111111111", "John Doe", "12/26", "12", "Calle 1");
        assertFalse(factory.createPaymentMethod(100.0, "C1", "Test").validatePaymentMethod());
    }

    @Test
    void creditCardValidationFailsOnInvalidExpiry() {
        PaymentFactory factory = new CreditCardPaymentFactory(
                "4111111111111111", "John Doe", "1226", "123", "Calle 1");
        assertFalse(factory.createPaymentMethod(100.0, "C1", "Test").validatePaymentMethod());
    }

    @Test
    void creditCardValidationFailsOnShortCardNumber() {
        PaymentFactory factory = new CreditCardPaymentFactory(
                "411111", "John Doe", "12/26", "123", "Calle 1");
        assertFalse(factory.createPaymentMethod(100.0, "C1", "Test").validatePaymentMethod());
    }

    @Test
    void creditCardMaskCardNumber() {
        CreditCardFactory cc = new CreditCardFactory(100.0, "C1", "Test",
                "4111111111111111", "John", "12/26", "123", "Addr");
        assertEquals("**** **** **** 1111", cc.maskCardNumber());
    }

    @Test
    void creditCardGetCardHolderAndType() {
        CreditCardFactory cc = new CreditCardFactory(100.0, "C1", "Test",
                "5500000000000004", "Maria", "12/26", "321", "Addr");
        assertEquals("Maria", cc.getCardHolderName());
        assertEquals("MASTERCARD", cc.getCardType());
    }

    // ===== Abstract Factory - PayPal =====

    @Test
    void paypalFactoryCreatesPaymentMethod() {
        PaymentFactory factory = new PaypalPaymentFactory("user@example.com", "token123456789");
        PaymentMethod payment = factory.createPaymentMethod(200.0, "CUST002", "PayPal purchase");
        assertNotNull(payment);
        assertEquals("PAYPAL", payment.getPaymentMethod());
    }

    @Test
    void paypalValidationPasses() {
        PaymentFactory factory = new PaypalPaymentFactory("user@example.com", "token123456789");
        assertTrue(factory.createPaymentMethod(200.0, "C2", "Test").validatePaymentMethod());
    }

    @Test
    void paypalValidationFailsOnInvalidEmail() {
        PaymentFactory factory = new PaypalPaymentFactory("invalidemail", "token123456789");
        assertFalse(factory.createPaymentMethod(200.0, "C2", "Test").validatePaymentMethod());
    }

    @Test
    void paypalValidationFailsOnShortToken() {
        PaymentFactory factory = new PaypalPaymentFactory("user@example.com", "short");
        assertFalse(factory.createPaymentMethod(200.0, "C2", "Test").validatePaymentMethod());
    }

    @Test
    void paypalGetEmail() {
        PaypalFactory pp = new PaypalFactory(200.0, "C2", "Test",
                "user@example.com", "tokenXYZ123456789");
        assertEquals("user@example.com", pp.getEmail());
        assertNull(pp.getPaypalTransactionId());
    }

    // ===== Abstract Factory - Crypto =====

    @Test
    void cryptoFactoryCreatesPaymentMethod() {
        PaymentFactory factory = new CryptoPaymentFactory(
                "1A2B3C4D5E6F7G8H9I0J1K2L3M", "BTC", 5000.0);
        PaymentMethod payment = factory.createPaymentMethod(100.0, "CUST003", "Crypto purchase");
        assertNotNull(payment);
        assertEquals("CRYPTOCURRENCY", payment.getPaymentMethod());
    }

    @Test
    void cryptoValidationPasses() {
        PaymentFactory factory = new CryptoPaymentFactory(
                "1A2B3C4D5E6F7G8H9I0J1K2L3M", "BTC", 5000.0);
        assertTrue(factory.createPaymentMethod(100.0, "C3", "Test").validatePaymentMethod());
    }

    @Test
    void cryptoValidationFailsOnInsufficientBalance() {
        PaymentFactory factory = new CryptoPaymentFactory(
                "1A2B3C4D5E6F7G8H9I0J1K2L3M", "BTC", 50.0);
        assertFalse(factory.createPaymentMethod(100.0, "C3", "Test").validatePaymentMethod());
    }

    @Test
    void cryptoValidationFailsOnShortWalletAddress() {
        PaymentFactory factory = new CryptoPaymentFactory("SHORT", "BTC", 5000.0);
        assertFalse(factory.createPaymentMethod(100.0, "C3", "Test").validatePaymentMethod());
    }

    @Test
    void cryptoGetWalletAndType() {
        CryptoFactory cr = new CryptoFactory(100.0, "C3", "Test",
                "1A2B3C4D5E6F7G8H9I0J1K2L3M", "ETH", 5000.0);
        assertEquals("1A2B3C4D5E6F7G8H9I0J1K2L3M", cr.getWalletAddress());
        assertEquals("ETH", cr.getCryptoType());
        assertNull(cr.getBlockchainHash());
    }

    // ===== Observer Pattern =====

    @Test
    void eciPaymentAddAndRemoveObserver() {
        ECIPayment eciPayment = new ECIPayment();
        PaymentObserver observer = new PaymentEventObserver(inventory, facturation, notification);
        eciPayment.addObserver(observer);
        eciPayment.removeObserver(observer);
    }

    @Test
    void paymentEventObserverHandlesSuccessWithKnownProduct() {
        PaymentEventObserver observer = new PaymentEventObserver(inventory, facturation, notification);
        PaymentMethod payment = new CreditCardPaymentFactory(
                "4111111111111111", "Jane Smith", "12/26", "456", "Av. 1")
                .createPaymentMethod(1200.0, "CUST001", "Laptop purchase");
        payment.setStatus(PaymentStatus.COMPLETED);
        observer.onPaymentSuccess(payment, "Jane Smith", "jane@example.com", "LAPTOP001");
        assertEquals(4, inventory.getStock("LAPTOP001"));
    }

    @Test
    void paymentEventObserverHandlesSuccessWithUnknownProduct() {
        PaymentEventObserver observer = new PaymentEventObserver(inventory, facturation, notification);
        PaymentMethod payment = new PaypalPaymentFactory("jane@example.com", "tokenXYZ123456")
                .createPaymentMethod(50.0, "CUST002", "Book purchase");
        payment.setStatus(PaymentStatus.COMPLETED);
        observer.onPaymentSuccess(payment, "Jane", "jane@example.com", "UNKNOWN_PRODUCT");
    }

    @Test
    void paymentEventObserverHandlesFailed() {
        PaymentEventObserver observer = new PaymentEventObserver(inventory, facturation, notification);
        PaymentMethod payment = new CreditCardPaymentFactory(
                "5500000000000004", "Bob", "01/25", "321", "Calle 2")
                .createPaymentMethod(100.0, "CUST003", "Failed payment");
        payment.setStatus(PaymentStatus.FAILED);
        observer.onPaymentFailed(payment, "bob@example.com");
    }

    // ===== Inventory =====

    @Test
    void inventoryGetExistingProduct() {
        Product product = inventory.getProduct("LAPTOP001");
        assertNotNull(product);
        assertEquals("Gaming Laptop", product.getName());
        assertEquals(1200.00, product.getPrice());
    }

    @Test
    void inventoryGetNonExistingProduct() {
        assertNull(inventory.getProduct("NONEXISTENT"));
    }

    @Test
    void inventoryDiscountReducesStock() {
        inventory.discountProduct("LAPTOP001", 2);
        assertEquals(3, inventory.getStock("LAPTOP001"));
    }

    @Test
    void inventoryDiscountInsufficientStock() {
        assertFalse(inventory.discountProduct("LAPTOP001", 100));
    }

    @Test
    void inventoryGetStockNonExisting() {
        assertEquals(0, inventory.getStock("NONEXISTENT"));
    }

    // ===== Product =====

    @Test
    void productGetters() {
        Product product = new Product("PROD001", "Test Product", 49.99, "Books");
        assertEquals("PROD001", product.getProductId());
        assertEquals("Test Product", product.getName());
        assertEquals(49.99, product.getPrice());
        assertEquals("Books", product.getCategory());
    }

    // ===== Facturation =====

    @Test
    void facturationCalculatesTax() {
        assertEquals(19.0, facturation.calculateTax(100.0), 0.001);
    }

    @Test
    void facturationCalculatesTotal() {
        assertEquals(119.0, facturation.calculateTotal(100.0), 0.001);
    }

    @Test
    void facturationGettersAndSetters() {
        facturation.setTaxRate(0.10);
        assertEquals(0.10, facturation.getTaxRate(), 0.001);
        facturation.setCurrency("EUR");
        assertEquals("EUR", facturation.getCurrency());
        assertNotNull(facturation.getCompanyName());
        assertNotNull(facturation.getTaxId());
        assertNotNull(facturation.getNextInvoiceNumber());
    }

    // ===== Notification =====

    @Test
    void notificationGetters() {
        assertNotNull(notification.getCompanyName());
        assertNotNull(notification.getFromEmail());
    }

    @Test
    void notificationSendConfirmation() {
        PaymentMethod payment = new PaypalPaymentFactory("test@test.com", "token123456789")
                .createPaymentMethod(100.0, "C9", "Test");
        notification.sendConfirmationEmail("test@test.com", "Test User", payment);
    }

    @Test
    void notificationSendFailure() {
        PaymentMethod payment = new CryptoPaymentFactory(
                "1A2B3C4D5E6F7G8H9I0J1K2L3M", "BTC", 5000.0)
                .createPaymentMethod(100.0, "C10", "Test");
        notification.sendFailureNotification(payment, "fail@test.com");
    }

    // ===== PaymentStatus =====

    @Test
    void paymentStatusValues() {
        assertEquals("Pendiente", PaymentStatus.PENDING.getName());
        assertEquals("Procesando", PaymentStatus.PROCESSING.getName());
        assertEquals("Completado", PaymentStatus.COMPLETED.getName());
        assertEquals("Fallido", PaymentStatus.FAILED.getName());
        assertEquals("Cancelado", PaymentStatus.CANCELED.getName());
    }

    // ===== PaymentMethod base behavior =====

    @Test
    void paymentMethodGettersAndSetters() {
        PaymentMethod payment = new CreditCardPaymentFactory(
                "4111111111111111", "Test User", "12/26", "123", "Addr")
                .createPaymentMethod(500.0, "CUST_TEST", "Test");
        assertEquals(500.0, payment.getAmount());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        assertNotNull(payment.getTransactionId());
        assertNotNull(payment.getTimestamp());
        assertEquals("Test", payment.getDescription());

        payment.setAmount(750.0);
        assertEquals(750.0, payment.getAmount());

        payment.setStatus(PaymentStatus.PROCESSING);
        assertEquals(PaymentStatus.PROCESSING, payment.getStatus());
    }

    @Test
    void paymentMethodCustomerIdIsSet() {
        PaymentMethod payment = new PaypalPaymentFactory("x@x.com", "tokenXYZ123456")
                .createPaymentMethod(10.0, "MY_CUSTOMER", "desc");
        assertEquals("MY_CUSTOMER", payment.getCustomerId());
    }
}
