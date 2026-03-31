/** Project: Lab 4
 * Purpose Details: Homework
 * Course: IST 242
 * Author: Kevin Ajdini
 * Date Developed: March 30th 2026
 * Last Date Changed: March 31st 2026
 * Rev: Added some extra stuff

 */

/** imports the rabbit library*/
import com.rabbitmq.client.*;
import com.fasterxml.jackson.databind.ObjectMapper;
/** creates class receiver*/
public class RabbitMQReceiver {
    /** stores queue name*/
    private final static String QUEUE_NAME = "pizzaQueue";

    public static void main(String[] args) throws Exception {
/** connects a factory that builds connections*/
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
/** like opening a door and the channel is the path*/
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
/** declares the queue so it exists*/
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("Waiting for messages...");
/** fires automatically when a message arrives*/
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received raw: " + message);

            /** tries to pass it as JSON*/
            try {
                ObjectMapper mapper = new ObjectMapper();
                Pizza pizza = mapper.readValue(message, Pizza.class);
                System.out.println("Pizza object: " + pizza.toString());
            } catch (Exception e) {
                /** flat file*/
                System.out.println("Flat file received: " + message);
                String[] parts = message.split("\\|");
                Pizza pizza = new Pizza(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
                System.out.println("Pizza object: " + pizza.toString());
            }
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}