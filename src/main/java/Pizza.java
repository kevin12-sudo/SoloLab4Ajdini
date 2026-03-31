/** Project: Lab 4
 * Purpose Details: Homework
 * Course: IST 242
 * Author: Kevin Ajdini
 * Date Developed: March 30th 2026
 * Last Date Changed: March 31st 2026
 * Rev: Added some extra stuff

 */
import com.fasterxml.jackson.databind.ObjectMapper;
/**  Creates Pizza class*/
public class Pizza {

    /** feilds */
    private String size;
    private String crust;
    private String topping;
    private double price;


    public Pizza() {}

    /** constructors */
    public Pizza(String size, String crust, String topping, double price) {
        this.size = size;
        this.crust = crust;
        this.topping = topping;
        this.price = price;
    }

    /** getters */
    public String getSize() { return size; }
    public String getCrust() { return crust; }
    public String getTopping() { return topping; }
    public double getPrice() { return price; }

    /** setters */
    public void setSize(String size) { this.size = size; }
    public void setCrust(String crust) { this.crust = crust; }
    public void setTopping(String topping) { this.topping = topping; }
    public void setPrice(double price) { this.price = price; }

    /** Flat file formatting*/
    public String toFlatFile() {
        return size + "|" + crust + "|" + topping + "|" + price;
    }

    /** to JSON string */
    public String toJSON() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    /** print */
    @Override
    public String toString() {
        return "Pizza [size=" + size + ", crust=" + crust +
                ", topping=" + topping + ", price=$" + price + "]";
    }
}