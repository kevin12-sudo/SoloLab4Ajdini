/** Project: Lab 4
 * Purpose Details: Homework
 * Course: IST 242
 * Author: Kevin Ajdini
 * Date Developed: March 30th 2026
 * Last Date Changed: March 31st 2026
 * Rev: Added some extra stuff

 */
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class RabbitMQsend {
/** Name of RabbitMQ queue */
    private final static String QUEUE_NAME = "pizzaQueue";

    public static void main(String[] args) throws Exception {

        /** creates the pizza object*/
        Pizza pizza = new Pizza("Small", "Stuffed", "Mushrooms", 20.95);

        /** converting flat file and JSON*/
        String flatFile = pizza.toFlatFile();
        String json = pizza.toJSON();

        /** connects to rabbitmq*/
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            /** sends flat file*/
            channel.basicPublish("", QUEUE_NAME, null, flatFile.getBytes());
            System.out.println("Sent flat file: " + flatFile);

            /** sends JSON */
            channel.basicPublish("", QUEUE_NAME, null, json.getBytes());
            System.out.println("Sent JSON: " + json);
        }
    }
}