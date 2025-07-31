package de.THM.devops;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.TopicExchange;

@Configuration
public class RabbitConfig {
    //  Queue for consultant
    public static final String REQUEST_QUEUE_NAME_Co = "consultant-request-queue";
    public static final String RESPONSE_QUEUE_NAME_Co = "consultant-response-queue";
    public static final String MESSAGE_QUEUE_NAME_Co = "consultant-message-queue";

    // Queue for consultantsproject
    public static final String REQUEST_QUEUE_NAME_C = "consultantsproject-request-queue";
    public static final String RESPONSE_QUEUE_NAME_C= "consultantsproject-response-queue";
    public static final String MESSAGE_QUEUE_NAME_C= "consultantsproject-message-queue";

    //Queue for Project
    public static final String REQUEST_QUEUE_NAME_P = "project-request-queue";
    public static final String RESPONSE_QUEUE_NAME_P = "project-response-queue";
    public static final String MESSAGE_QUEUE_NAME_P = "project-message-queue";

    // Creation of the queues Consultant
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

//    Creation of queues for consultantsproject
    @Bean
    public Queue consultantsprojectRequestQueue() {
        return new Queue(REQUEST_QUEUE_NAME_C, false);
    }

    @Bean
    public Queue consultantsprojectResponseQueue() {
        return new Queue(RESPONSE_QUEUE_NAME_C, false);
    }

    @Bean
    public Queue consultantsprojectMessageQueue() {
        return new Queue(MESSAGE_QUEUE_NAME_C, false);
    }

// creation of queues for  project

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