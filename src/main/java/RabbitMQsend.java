/**
 * Name: Kevin Ajdini
 * Date: March 30, 2025
 * Description: Sends a Pizza object as flat file and JSON via RabbitMQ
 */
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class RabbitMQsend {

    private final static String QUEUE_NAME = "pizzaQueue";

    public static void main(String[] args) throws Exception {

        // Create a pizza object
        Pizza pizza = new Pizza("large", "thin", "pepperoni", 12.99);

        // Convert to flat file and JSON
        String flatFile = pizza.toFlatFile();
        String json = pizza.toJSON();

        // Connect to RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // Send flat file
            channel.basicPublish("", QUEUE_NAME, null, flatFile.getBytes());
            System.out.println("Sent flat file: " + flatFile);

            // Send JSON
            channel.basicPublish("", QUEUE_NAME, null, json.getBytes());
            System.out.println("Sent JSON: " + json);
        }
    }
}