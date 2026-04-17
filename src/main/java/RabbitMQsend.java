/** Project: Lab 4
 * Purpose Details: Homework
 * Course: IST 242
 * Author: Kevin Ajdini
 * Date Developed: March 30th 2026
 * Last Date Changed: April 17th 2026
 * Rev: Added HMAC integrity check
 */
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class RabbitMQsend {
    /** Name of RabbitMQ queue */
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
        /** creates the pizza object */
        Pizza pizza = new Pizza("Small", "Stuffed", "Mushrooms", 20.95);
        /** converting flat file and JSON */
        String flatFile = pizza.toFlatFile();
        String json = pizza.toJSON();

        /** compute HMAC for both */
        String flatFileHMAC = computeHMAC(flatFile);
        String jsonHMAC = computeHMAC(json);

        /** combine message and HMAC separated by || */
        String flatFilePayload = flatFile + "||" + flatFileHMAC;
        String jsonPayload = json + "||" + jsonHMAC;

        /** connects to rabbitmq */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            /** sends flat file with HMAC */
            channel.basicPublish("", QUEUE_NAME, null, flatFilePayload.getBytes("UTF-8"));
            System.out.println("Sent message : " + flatFile);
            System.out.println("Sent HMAC    : " + flatFileHMAC);
            System.out.println();

            /** sends JSON with HMAC */
            channel.basicPublish("", QUEUE_NAME, null, jsonPayload.getBytes("UTF-8"));
            System.out.println("Sent message : " + json);
            System.out.println("Sent HMAC    : " + jsonHMAC);
        }
    }
}