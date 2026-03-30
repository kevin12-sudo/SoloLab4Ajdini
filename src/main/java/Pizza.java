/**
 * Name: Kevin Ajdini
 * Date: March 30, 2025
 * Description: Pizza class with fields, constructor, getters and setters
 */
import com.fasterxml.jackson.databind.ObjectMapper;

public class Pizza {

    // Fields
    private String size;
    private String crust;
    private String topping;
    private double price;

    // Default constructor (required for Jackson)
    public Pizza() {}

    // Constructor
    public Pizza(String size, String crust, String topping, double price) {
        this.size = size;
        this.crust = crust;
        this.topping = topping;
        this.price = price;
    }

    // Getters
    public String getSize() { return size; }
    public String getCrust() { return crust; }
    public String getTopping() { return topping; }
    public double getPrice() { return price; }

    // Setters
    public void setSize(String size) { this.size = size; }
    public void setCrust(String crust) { this.crust = crust; }
    public void setTopping(String topping) { this.topping = topping; }
    public void setPrice(double price) { this.price = price; }

    // Flat file format
    public String toFlatFile() {
        return size + "|" + crust + "|" + topping + "|" + price;
    }

    // Serialize to JSON string
    public String toJSON() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    // Print to console
    @Override
    public String toString() {
        return "Pizza [size=" + size + ", crust=" + crust +
                ", topping=" + topping + ", price=$" + price + "]";
    }
}