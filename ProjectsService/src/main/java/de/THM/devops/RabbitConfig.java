package de.THM.devops;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    //  Queue for customer
    public static final String REQUEST_QUEUE_NAME = "customer-request-queue";
    public static final String RESPONSE_QUEUE_NAME = "customer-response-queue";
    public static final String MESSAGE_QUEUE_NAME = "customer-message-queue";

    // Queue for projects
    public static final String REQUEST_QUEUE_NAME_P = "project-request-queue";
    public static final String RESPONSE_QUEUE_NAME_P = "project-response-queue";
    public static final String MESSAGE_QUEUE_NAME_P = "project-message-queue";

    // Creation of the queues
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

//    Creation of queues for project
    @Bean
    public Queue projectRequestQueue() {
        return new Queue(REQUEST_QUEUE_NAME_P, false);
    }

    @Bean
    public Queue projectResponseQueue() {
        return new Queue(RESPONSE_QUEUE_NAME_P, false);
    }

    @Bean
    public Queue projectMessageQueue() {
        return new Queue(MESSAGE_QUEUE_NAME_P, false);
    }


}