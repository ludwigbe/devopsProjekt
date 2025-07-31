package de.THM.devops;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.TopicExchange;

@Configuration
public class RabbitConfig {
    public static final String REQUEST_QUEUE_NAME = "customer-request-queue";
    public static final String RESPONSE_QUEUE_NAME = "customer-response-queue";
    public static final String MESSAGE_QUEUE_NAME = "customer-message-queue";

    @Bean
    public Queue customerRequestQueue() {
        return new Queue(REQUEST_QUEUE_NAME, false);
    }

    @Bean
    public Queue customerResponseQueue() {
        return new Queue(RESPONSE_QUEUE_NAME, false);
    }

    @Bean
    public Queue customerMessageQueue() {
        return new Queue(MESSAGE_QUEUE_NAME, false);
    }
}