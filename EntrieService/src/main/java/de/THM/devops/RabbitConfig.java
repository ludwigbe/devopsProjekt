package de.THM.devops;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.TopicExchange;

@Configuration
public class RabbitConfig {

    //  Queue for Entries
    public static final String REQUEST_QUEUE_NAME_E = "entrie-request-queue";
    public static final String RESPONSE_QUEUE_NAME_E = "entrie-response-queue";
    public static final String MESSAGE_QUEUE_NAME_E= "entrie-message-queue";

    // Queue for projects
    public static final String REQUEST_QUEUE_NAME_P = "project-request-queue";
    public static final String RESPONSE_QUEUE_NAME_P = "project-response-queue";
    public static final String MESSAGE_QUEUE_NAME_P = "project-message-queue";

    // Queue for Consultant
    public static final String REQUEST_QUEUE_NAME_Co = "consultant-request-queue";
    public static final String RESPONSE_QUEUE_NAME_Co = "consultant-response-queue";
    public static final String MESSAGE_QUEUE_NAME_Co = "consultant-message-queue";

    // Creation of the queues Entrie
    @Bean
    public Queue entrieRequestQueue() {
        return new Queue(REQUEST_QUEUE_NAME_E, false);
    }

    @Bean
    public Queue entrieResponseQueue() {
        return new Queue(RESPONSE_QUEUE_NAME_E, false);
    }

    @Bean
    public Queue entrieMessageQueue() {
        return new Queue(MESSAGE_QUEUE_NAME_E, false);
    }

    //   Creation of queues for project
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

    //   Creation of queues for project

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