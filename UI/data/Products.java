package UI.data;

public class Products {

    private String name;
    private double price;
    private int stocks;

    public static Products[] loadProducts() {
        return new Products[] {
                new Products("Keyboard", 1500, 5),
                new Products("Mouse", 800, 5),
                new Products("Monitor", 7500, 3),
                new Products("Webcam", 2000, 2),
                new Products("Headset", 1200, 4)
        };
    }

    public Products(String name, double price, int stocks) {
        this.name = name;
        this.price = price;
        this.stocks = stocks;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStocks() {
        return stocks;
    }
}