/** Project: Lab 4
 * Purpose Details: Homework
 * Course: IST 242
 * Author: Kevin Ajdini
 * Date Developed: March 30th 2026
 * Last Date Changed: April 17th 2026
 * Rev: Added HMAC integrity check
 */
import com.rabbitmq.client.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class RabbitMQReceiver {
    /** stores queue name */
    private final static String QUEUE_NAME = "pizzaQueue";
    /** Shared secret key for HMAC */
    private final static String SECRET_KEY = "secret_key";

    /** Computes HMAC-SHA256 for a given message */
    public static String computeHMAC(String message) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "HmacSHA256");
        mac.init(keySpec);
        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(rawHmac);
    }

    public static void main(String[] args) throws Exception {
        /** connects a factory that builds connections */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        /** like opening a door and the channel is the path */
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        /** declares the queue so it exists */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("Waiting for messages...\n");

        /** fires automatically when a message arrives */
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String fullPayload = new String(delivery.getBody(), "UTF-8");

                /** split message and HMAC by || */
                String[] parts = fullPayload.split("\\|\\|");
                String receivedMessage = parts[0];
                String receivedHMAC = parts[1];

                /** compute HMAC from received message */
                String computedHMAC = computeHMAC(receivedMessage);

                System.out.println("Received message  : " + receivedMessage);
                System.out.println("Received HMAC     : " + receivedHMAC);
                System.out.println("Generated HMAC    : " + computedHMAC);

                /** verify HMAC */
                if (computedHMAC.equals(receivedHMAC)) {
                    System.out.println("HMAC is VALID — payload integrity confirmed!");
                } else {
                    System.out.println("HMAC is INVALID — message may have been tampered with!");
                }
                System.out.println();

                /** tries to parse as JSON */
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Pizza pizza = mapper.readValue(receivedMessage, Pizza.class);
                    System.out.println("Pizza object: " + pizza.toString());
                } catch (Exception e) {
                    /** flat file fallback */
                    String[] pizzaParts = receivedMessage.split("\\|");
                    Pizza pizza = new Pizza(pizzaParts[0], pizzaParts[1], pizzaParts[2], Double.parseDouble(pizzaParts[3]));
                    System.out.println("Pizza object: " + pizza.toString());
                }
                System.out.println();

            } catch (Exception e) {
                System.out.println("Error processing message: " + e.getMessage());
            }
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}