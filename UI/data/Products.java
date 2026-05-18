package UI.data;

public class Products {

    private String name;
    private double price;

    public static Products[] loadProducts() {
        return new Products[]{
                new Products("Keyboard", 1500),
                new Products("Mouse", 800),
                new Products("Monitor", 7500),
                new Products("Webcam", 2000),
                new Products("Headset", 1200)
        };
    }

    public Products(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}