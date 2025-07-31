package de.THM.devops;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.TopicExchange;

@Configuration
public class RabbitConfig {
    public static final String REQUEST_QUEUE_NAME_Co = "consultant-request-queue";
    public static final String RESPONSE_QUEUE_NAME_Co = "consultant-response-queue";
    public static final String MESSAGE_QUEUE_NAME_Co = "consultant-message-queue";

    @Bean
    public Queue consultantRequestQueue() {
        return new Queue(REQUEST_QUEUE_NAME_Co, false);
    }

    @Bean
    public Queue consultantResponseQueue() {
        return new Queue(RESPONSE_QUEUE_NAME_Co, false);
    }

    @Bean
    public Queue consultantMessageQueue() {
        return new Queue(MESSAGE_QUEUE_NAME_Co, false);
    }
}