import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
    private final static String QUEUE = "FRODO";
    private final Timer timer;
    private Channel channel;
    private Logger logger = LoggerFactory.getLogger(Client.class);

    Client(){
        timer = new Timer();
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try{
            Connection conn = connectionFactory.newConnection();
            channel = conn.createChannel();
            channel.queueDeclare(QUEUE,false,false,false,null);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public void start (){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                receiveMessageFromQueue();
            }
        };

        timer.schedule(timerTask,6000,5000);

    }

    private void receiveMessageFromQueue(){
        // To buffer the messages. Receives all the accumulated messages at once.

//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//            System.out.println(" [x] Received '" + message + "'");
//        };
//
//        try {
//            channel.basicConsume(QUEUE,true,deliverCallback, cancelCallBack -> {});
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }

        // Receives one message at a time.
        try {
            GetResponse response = channel.basicGet(QUEUE,true);

            if (response != null) {
                String message = new String(response.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
