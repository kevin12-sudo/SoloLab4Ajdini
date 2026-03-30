/**
 * Name: Kevin Ajdini
 * Date: March 30, 2025
 * Description: Receives a Pizza object as flat file and JSON via RabbitMQ
 */
import com.rabbitmq.client.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RabbitMQReceiver {

    private final static String QUEUE_NAME = "pizzaQueue";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("Waiting for messages...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received raw: " + message);

            // Try to deserialize as JSON
            try {
                ObjectMapper mapper = new ObjectMapper();
                Pizza pizza = mapper.readValue(message, Pizza.class);
                System.out.println("Pizza object: " + pizza.toString());
            } catch (Exception e) {
                // Its a flat file
                System.out.println("Flat file received: " + message);
                String[] parts = message.split("\\|");
                Pizza pizza = new Pizza(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
                System.out.println("Pizza object: " + pizza.toString());
            }
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}