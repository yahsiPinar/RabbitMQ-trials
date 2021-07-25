import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class Server {

    private final static String QUEUE = "FRODO";
    private final Timer timer;
    private Channel channel;
    private Logger logger = LoggerFactory.getLogger(Server.class);

    Server(){
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
                sendMessageToQueue();
            }
        };

        timer.schedule(timerTask,0,5000);

    }

    private void sendMessageToQueue(){
        String message = LocalDateTime.now().toString();
        try {
            channel.basicPublish("",QUEUE,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
