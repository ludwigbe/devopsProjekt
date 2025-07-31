package de.THM.devops.service;

import de.THM.devops.model.Consultant;
import de.THM.devops.RabbitConfig;
import de.THM.devops.repository.ConsultantRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class Consultantservice {

    private final ConsultantRepository consultantRepository;
    private final RabbitTemplate rabbitTemplate;
    private Boolean responseReceived = null;
    private final CountDownLatch latch = new CountDownLatch(1);

    public Consultantservice(ConsultantRepository consultantRepository, RabbitTemplate rabbitTemplate) {
        this.consultantRepository = consultantRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Get all consultants from the database.
     *
     * @return A list of all consultants in the database.
     */
    public List<Consultant> getAllConsultants() {
        return consultantRepository.findAll();
    }

    /**
     * Retrieve a consultant by their unique ID, with RabbitMQ validation.
     *
     * @param id The ID of the consultant to be retrieved.
     * @return The consultant with the specified ID.
     */

    public Consultant getConsultantById(Long id) {
        return consultantRepository.findById(id).orElseThrow();
    }

    /**
     * Create and save a new consultant in the database.
     *
     * @param consultant The Consultant object to be saved.
     * @return The saved Consultant object.
     */
    public Consultant createConsultant(Consultant consultant) {
        return consultantRepository.save(consultant);
    }

    /**
     * Update a consultant in the database.
     *
     * @param id         The ID of the consultant to be updated.
     * @param consultant The Consultant object to be updated.
     * @return The updated Consultant object.
     */
    public Consultant updateConsultant(Long id, Consultant consultant) {
        consultant.setId(id);
        Consultant updatedConsultant = consultantRepository.save(consultant);

        // Notify RabbitMQ of the update
        rabbitTemplate.convertAndSend(RabbitConfig.MESSAGE_QUEUE_NAME_Co, "Consultant " + id + " updated.");
        return updatedConsultant;
    }

    /**
     * Delete a consultant from the database and notify RabbitMQ.
     *
     * @param id The ID of the consultant to be deleted.
     */
    public void deleteConsultant(Long id) {
        consultantRepository.deleteById(id);
        rabbitTemplate.convertAndSend(RabbitConfig.MESSAGE_QUEUE_NAME_Co, "Consultant " + id + " deleted.");
    }

    @RabbitListener(queues = RabbitConfig.RESPONSE_QUEUE_NAME_Co)
    public void receiveResponse(@Payload Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        responseReceived = Boolean.parseBoolean(new String(message.getBody()));
        latch.countDown();
    }

    @RabbitListener(queues = RabbitConfig.MESSAGE_QUEUE_NAME_Co)
    public void receiveMessage(@Payload Message message) {
        System.out.println("Message reçu dans Consultantservice: " + new String(message.getBody()));
    }
}
