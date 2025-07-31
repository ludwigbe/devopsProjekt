package de.THM.devops.service;

import de.THM.devops.dto.ConsultantsDto;
import de.THM.devops.dto.EntriesDto;
import de.THM.devops.dto.ProjectsDto;
import de.THM.devops.model.Entries;
import de.THM.devops.RabbitConfig;
import de.THM.devops.repository.EntriesRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EntriesService {

    private final EntriesRepository entriesRepository;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Value("${consultant.service.url}")
    private String consultantsServiceUrl;

    @Value("${projects.service.url}")
    private String projectsServiceUrl;

    // Map to hold responses by correlationId
    private final Map<String, Boolean> responseMap = new ConcurrentHashMap<>();
    private final CountDownLatch latch = new CountDownLatch(1);

    public EntriesService(EntriesRepository entriesRepository, RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.entriesRepository = entriesRepository;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public EntriesDto createEntry(EntriesDto entriesDto) {
        Long consultantId = entriesDto.getConsultantsId();
        Long projectId = entriesDto.getProjectId();

        // Vérifiez si l'ID est valide avant de continuer0
        if (consultantId == null || projectId == null) {
            throw new IllegalArgumentException("Consultant ID or Project ID cannot be null");
        }

        // Send request to RabbitMQ to validate consultant
        //validateWithRabbitMQ(consultantId, "consultant-request-queue");

        // Send request to RabbitMQ to validate project
        //validateWithRabbitMQ(projectId, "project-request-queue");

        // Get consultant and project details
        ConsultantsDto consultant = restTemplate.getForObject(consultantsServiceUrl + consultantId, ConsultantsDto.class);
        ProjectsDto project = restTemplate.getForObject(projectsServiceUrl + projectId, ProjectsDto.class);

        if (consultant == null || project == null) {
            throw new IllegalArgumentException("Consultant or Project not found");
        }

        // Create the entry
        Entries entry = new Entries();
        entry.setDate(entriesDto.getDate());
        entry.setHours(entriesDto.getHours());
        entry.setConsultantsId(consultantId);
        entry.setConsultantName(consultant.getName());
        entry.setProjectsId(projectId);
        entry.setProjectName(project.getName());

        Entries savedEntry = entriesRepository.save(entry);

        // Notify via RabbitMQ
        //rabbitTemplate.convertAndSend("entrie-message-queue", "Entry " + savedEntry.getId() + " created.");

        return convertToDto(savedEntry);
    }

    public EntriesDto getEntryById(Long entryId) {
        Entries entry = entriesRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("Entry not found with id: " + entryId));
        return convertToDto(entry);
    }

    public List<EntriesDto> getAllEntries() {
        List<Entries> entriesList = entriesRepository.findAll();
        return entriesList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public EntriesDto updateEntry(Long entryId, EntriesDto updatedEntry) {
        Entries entry = entriesRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("Entry not found with id: " + entryId));

        Long consultantId = updatedEntry.getConsultantsId();
        Long projectId = updatedEntry.getProjectId();

        // Vérifiez si l'ID est valide avant de continuer
        if (consultantId == null || projectId == null) {
            throw new IllegalArgumentException("Consultant ID or Project ID cannot be null");
        }

        // Send request to RabbitMQ to validate consultant
        //validateWithRabbitMQ(consultantId, "consultant-request-queue");

        // Send request to RabbitMQ to validate project
        //validateWithRabbitMQ(projectId, "project-request-queue");

        entry.setDate(updatedEntry.getDate());
        entry.setHours(updatedEntry.getHours());
        entry.setConsultantsId(consultantId);
        entry.setConsultantName(updatedEntry.getConsultantName());
        entry.setProjectsId(updatedEntry.getProjectId());
        entry.setProjectName(updatedEntry.getProjectName());

        Entries updatedEntryObj = entriesRepository.save(entry);

        // Notify via RabbitMQ
        rabbitTemplate.convertAndSend("entrie-message-queue", "Entry " + updatedEntryObj.getId() + " updated.");

        return convertToDto(updatedEntryObj);
    }

    public void deleteEntry(Long entryId) {
        Entries entry = entriesRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("Entry not found with id: " + entryId));

        entriesRepository.deleteById(entryId);

        // Notify via RabbitMQ
        rabbitTemplate.convertAndSend("entrie-message-queue", "Entry " + entryId + " deleted.");
    }

    private void validateWithRabbitMQ(Long id, String queueName) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        String correlationId = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(queueName, id, message -> {
            message.getMessageProperties().setCorrelationId(correlationId);
            return message;
        });

        try {
            if (!latch.await(5, TimeUnit.SECONDS)) {  // Increased timeout for better response handling
                throw new RuntimeException("Timeout: No response received.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while waiting for the response", e);
        }

        // Check the response for the correlationId
        if (responseMap.get(correlationId) == null || !responseMap.get(correlationId)) {
            throw new RuntimeException("Invalid response or not found");
        }
    }

    private EntriesDto convertToDto(Entries entries) {
        EntriesDto entriesDto = new EntriesDto();
        entriesDto.setEntryId(entries.getId());
        entriesDto.setDate(entries.getDate());
        entriesDto.setHours(entries.getHours());
        entriesDto.setConsultantsId(entries.getConsultantsId());
        entriesDto.setConsultantName(entries.getConsultantName());
        entriesDto.setProjectId(entries.getProjectsId());
        entriesDto.setProjectName(entries.getProjectName());

        return entriesDto;
    }

    @RabbitListener(queues = "consultant-response-queue")
    public void receiveConsultantResponse(@Payload Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (correlationId != null) {
            String messageBody = new String(message.getBody());
            if (messageBody == null || messageBody.isEmpty()) {
                throw new RuntimeException("Received empty message from RabbitMQ");
            }
            responseMap.put(correlationId, Boolean.parseBoolean(messageBody));
            latch.countDown();  // Ensure latch counts down after response is processed
        }
    }

    @RabbitListener(queues = "project-response-queue")
    public void receiveProjectResponse(@Payload Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (correlationId != null) {
            String messageBody = new String(message.getBody());
            if (messageBody == null || messageBody.isEmpty()) {
                throw new RuntimeException("Received empty message from RabbitMQ");
            }
            responseMap.put(correlationId, Boolean.parseBoolean(messageBody));
            latch.countDown();  // Ensure latch counts down after response is processed
        }
    }
}
