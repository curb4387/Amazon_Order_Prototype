package com.amazon.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AmazonOrdersMain {

    // These act as our "Database" for the lab
    private List<Product> productCatalog = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();

    public void populateSystemData() {
        // Add Products (Tech, Home, etc.)
        // Kitchen Category
        productCatalog.add(new Product("KITCH-358",
                "Owala 24 oz Water Bottle - Beach House",
                29.99,
                Product.Condition.New,
                "owala_bottle.jpg"));
                // if image isn't working, try images/owala_bottle.jpg

        // Tech Category
        productCatalog.add(new Product("TECH-021",
                "Keyboard Case for iPad Pro - White",
                44.99,
                Product.Condition.Used,
                "ipad_keyboard.jpg"));

        // Gaming Category
        productCatalog.add(new Product("GAME-456",
                "ASUS Gaming Laptop RTX 5060, 1TB - Black",
                1203.99,
                Product.Condition.Refurbished,
                "gaming_laptop.jpg"));

        // Home Office
        productCatalog.add(new Product("OFFICE-980",
                "Height Adjustable 55 Inch Desk - Classical Brown",
                149.98,
                Product.Condition.New,
                "adjustable_desk.jpg"));

        // Outdoor
        productCatalog.add(new Product("OUT-644",
                "Folding Emergency Snow Shovel - Red",
                44.79,
                Product.Condition.New,
                "snow_shovel.jpg"));

        // Add Customers (Remember to create Address objects first!)
        // Al Albert: billing and shipping are the same
        // Prime member
        Address albertBilling = new Address("70 Davis Avenue", "Oakland", "CA", "94607", "United States");
        customers.add(new Customer("CUS-001", "Al Albert", "albertal@gmail.com", "707-555-3243", albertBilling, albertBilling, true));

        // Elena Potts: billing and shipping are different
        // Prime member
        Address pottsBilling = new Address("1079 Rollins Road", "Grand Island", "NE", "68803", "United States");
        Address pottsShipping = new Address("1889 Cunningham Court", "Troy", "MI", "48083", "United States");
        customers.add(new Customer("CUS-293", "Elena Potts", "epotts91@gmail.com", "248-555-1928", pottsBilling, pottsShipping, true));

        // Moira Banks: billing and shipping the same
        // Not Prime member
        Address banksBilling = new Address("2499 Hardesty Street", "Albany", "NY", "12207", "United States");
        customers.add(new Customer("CUS-147", "Moira Banks", "moira533@gmail.com", "518-555-6031", banksBilling, banksBilling, false));

        // Harvey Powell: billing and shipping the same
        // Not Prime member
        Address powellBilling = new Address("2608 Palmer Road", "Columbus", "OH", "43085", "United States");
        customers.add(new Customer("CUS-089", "Harvey Powell", "powellh@gmail.com", "614-555-9686", powellBilling, powellBilling, false));

        // Richard Walker: billing and shipping different
        // Not Prime member
        Address walkerBilling = new Address("593 Abner Road", "Wausau", "WI", "54401", "United States");
        Address walkerShipping = new Address("1224 Bobcat Drive", "Washington", "MD", "20200", "United States");
        customers.add(new Customer("CUS-758", "Richard Walker", "richwalker10@gmail.com", "240-555-5703", walkerBilling, walkerShipping, false));

        System.out.println("Customer database populated: " + customers.size() + " records loaded.");
        System.out.println("Successfully loaded " + productCatalog.size() + " products into the catalog.");
        System.out.println("System data loaded.");
    }

    // Method to find customer by name
    // try to find exact and partial match
    public Customer findCustomerByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        String input = name.toLowerCase().trim();
        Customer partial = null;

        for (Customer c : customers) {
            // if c is null, continue to next Customer
            if (c.getCustomerName() == null) {
                continue;
            }

            String cust = c.getCustomerName().toLowerCase().trim();

            // use indexOf to return customer or error if not there
            // try exact match first
            if (cust.indexOf(input) == 0 && cust.length() == input.length()) {
                return c;
            }

            // if not exact match, find partial
            if (cust.indexOf(input) != -1) {
                partial = c;
            }
        }

        return partial;
    }

    // Method to get customer login
    public Customer customerLogin() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your login: ");
            String login = scanner.nextLine().trim(); // .trim() gets rid of any leading/trailing whitespaces
            Customer search = findCustomerByName(login);

            if (search != null) {
                System.out.println("Login successful for customer: " + search.getCustomerName());
                return search;

            }
            System.out.println("Customer not found. Please try again.");
        }
    }

    public Product findProductByName(String partialName) {
        // Hint: Use a for-each loop and .toLowerCase().contains()
        if (partialName == null || partialName.trim().isEmpty()) {
            return null;
        }

        String input = partialName.toLowerCase().trim();
        Product partial = null;

        for (Product p : productCatalog) {
            if (p.getDescription() == null) {
                continue;
            }

            String product = p.getDescription().toLowerCase().trim();

            // find exact match first
            if (product.contains(input) && product.length() == input.length()) {
                return p;
            }

            // find partial match
            if (product.contains(input)) {
                partial = p;
            }
        }

        if (partial == null) {
            System.out.println("Item not found. Please try again.");
        }

        return partial;
    }

    // Method to delay output to console by 1.5 second
    public void delayOutput() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * THE LIFECYCLE SIMULATION
     * This is the heart of the lab. Students must follow the story.
     */
    public void simulateOrderLifecycle() {
        ReceiptService service = new ReceiptService();

        // Log customer in and create cart
        Customer buyer = customerLogin();

        List<OrderItem> cart = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        // Search for product and add to cart
        String prompt = "";
        do {
            System.out.println("Search for item or check out (c): ");
            prompt = scanner.nextLine().toLowerCase().trim();
            Product search = findProductByName(prompt);

            if (prompt.equals("c")) {
                break;
            }

            if (search != null) {
                System.out.println("Found: " + search + "\nAdd to cart? Y/N ");
                String addToCart = scanner.nextLine().toUpperCase().trim();

                if (addToCart.equals("Y")) {
                    System.out.println("Quantity to add: ");
                    int itemQuantity = scanner.nextInt();
                    // add in to stop double prompt
                    scanner.nextLine();
                    cart.add(new OrderItem(search, itemQuantity));
                    System.out.println(search.getDescription() + " (" + itemQuantity + ") added to cart!");
                }
            }
        } while (!prompt.equals("c"));

        // Create order
        Order order = new Order("111-7228445", buyer, cart, 5.00);
        service.sendOrderConfirmationEmail(order);
//        service.generatePdfInvoice(order);

        delayOutput();

        // Change order status to PROCESSING
        order.setOrderStatus(Order.OrderStatus.PROCESSING);
        System.out.println("Status Updated: " + order.getOrderStatus());

        delayOutput();

        // Create shipment
        Shipment shipment = new Shipment("AB123C45D67", "UPS", Shipment.ShipmentSpeed.TWO_DAY, order.getEstimatedDeliveryDate());
        order.setShipment(shipment);

        // Create payment
        Payment payment = new Payment(Payment.PaymentType.CREDIT_CARD, "123456789011", "Visa");
        order.setPayment(payment);

        // Change order status to SHIPPED
        shipment.setStatus(Shipment.Status.SHIPPED);
        order.setOrderStatus(Order.OrderStatus.SHIPPED);
        service.sendShipmentUpdate(order);
        // send invoice email
        service.sendInvoice(order, cart);

        delayOutput();

        System.out.println("Status Updated: " + order.getOrderStatus());

        delayOutput();

        // Change status to DELAYED and change expected arrival date
        shipment.setStatus(Shipment.Status.DELAYED);
        service.sendShipmentUpdate(order);

        delayOutput();

        // Change statuses to DELIVERED
        shipment.setStatus(Shipment.Status.DELIVERED);
        order.setOrderStatus(Order.OrderStatus.DELIVERED);
        service.sendShipmentUpdate(order);

        delayOutput();

        System.out.println("Status Updated: " + order.getOrderStatus());
    }

    public static void main(String[] args) {
        AmazonOrdersMain app = new AmazonOrdersMain();
        app.populateSystemData();  // customers and products see methods above

        // Run the simulation
        app.simulateOrderLifecycle();
    }
}