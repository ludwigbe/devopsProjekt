package de.THM.devops.service;

import de.THM.devops.dto.ConsultantsDto;
import de.THM.devops.RabbitConfig;
import de.THM.devops.dto.ConsultantsProjectsDto;
import de.THM.devops.dto.ProjectsDto;
import de.THM.devops.model.ConsultantsProject;
import de.THM.devops.repository.ConsultantsProjectRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ConsultantsProjectService {

    private final ConsultantsProjectRepository consultantsProjectRepository;
    private final RabbitTemplate rabbitTemplate;
    private Boolean responseReceived = null;
    private final CountDownLatch latch = new CountDownLatch(1);

    @Value("${consultant.service.url}")
    private String consultantsServiceUrl;

    @Value("${projects.service.url}")
    private String projectsServiceUrl;

    public ConsultantsProjectService(ConsultantsProjectRepository consultantsProjectRepository, RabbitTemplate rabbitTemplate) {
        this.consultantsProjectRepository = consultantsProjectRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Create a new Consultant-Project association.
     */
    public ConsultantsProjectsDto createConsultantProject(ConsultantsProjectsDto consultantsProjectsDto) {
        //validateWithRabbitMQ(consultantsProjectsDto.getConsultantsId(), RabbitConfig.REQUEST_QUEUE_NAME_C);

        ConsultantsProject consultantsProject = new ConsultantsProject();
        consultantsProject.setConsultantsId(consultantsProjectsDto.getConsultantsId());
        consultantsProject.setProjectsId(consultantsProjectsDto.getProjectsId());
        consultantsProject.setConsultantsName(consultantsProjectsDto.getConsultantsName());
        consultantsProject.setProjectsName(consultantsProjectsDto.getProjectsName());

        ConsultantsProject savedConsultantsProject = consultantsProjectRepository.save(consultantsProject);

        // Notify RabbitMQ of the creation
        //rabbitTemplate.convertAndSend(RabbitConfig.MESSAGE_QUEUE_NAME_C, "ConsultantsProject " + savedConsultantsProject.getId() + " created.");

        return convertToDto(savedConsultantsProject);
    }

    /**
     * Get all Consultant-Project associations.
     */
    public List<ConsultantsProjectsDto> getAllConsultantProjects() {
        List<ConsultantsProject> consultantsProjects = consultantsProjectRepository.findAll();
        return consultantsProjects.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Get a Consultant-Project association by ID.
     */
    public ConsultantsProjectsDto getConsultantProjectById(Long id) {
        ConsultantsProject consultantsProject = consultantsProjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ConsultantProject not found with ID: " + id));
        return convertToDto(consultantsProject);
    }

    /**
     * Update a Consultant-Project association.
     */
    public ConsultantsProjectsDto updateConsultantProject(Long id, ConsultantsProjectsDto updatedConsultantProject) {
        validateWithRabbitMQ(updatedConsultantProject.getConsultantsId(), RabbitConfig.REQUEST_QUEUE_NAME_C);

        ConsultantsProject consultantsProject = consultantsProjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ConsultantProject not found with ID: " + id));

        consultantsProject.setConsultantsId(updatedConsultantProject.getConsultantsId());
        consultantsProject.setProjectsId(updatedConsultantProject.getProjectsId());
        consultantsProject.setConsultantsName(updatedConsultantProject.getConsultantsName());
        consultantsProject.setProjectsName(updatedConsultantProject.getProjectsName());

        ConsultantsProject savedConsultantProject = consultantsProjectRepository.save(consultantsProject);

        // Notify RabbitMQ of the update
        rabbitTemplate.convertAndSend(RabbitConfig.MESSAGE_QUEUE_NAME_C, "ConsultantsProject " + savedConsultantProject.getId() + " updated.");

        return convertToDto(savedConsultantProject);
    }

    /**
     * Delete a Consultant-Project association.
     */
    public void deleteConsultantProject(Long id) {
        consultantsProjectRepository.deleteById(id);
        rabbitTemplate.convertAndSend(RabbitConfig.MESSAGE_QUEUE_NAME_C, "ConsultantsProject " + id + " deleted.");
    }

    /**
     * Listener to handle responses from RabbitMQ.
     */
    @RabbitListener(queues = RabbitConfig.RESPONSE_QUEUE_NAME_C)
    public void receiveResponse(@Payload Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        responseReceived = Boolean.parseBoolean(new String(message.getBody()));
        latch.countDown();
    }

    /**
     * Listener for general messages in RabbitMQ.
     */
    @RabbitListener(queues = RabbitConfig.MESSAGE_QUEUE_NAME_C)
    public void receiveMessage(@Payload Message message) {
        System.out.println("Message reçu dans ConsultantsProjectService: " + new String(message.getBody()));
    }

    /**
     * Validate Consultant existence via RabbitMQ.
     */
    private void validateWithRabbitMQ(Long id, String queueName) {
        String correlationId = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(queueName, id, message -> {
            message.getMessageProperties().setCorrelationId(correlationId);
            return message;
        });

        try {
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timeout: Pas de réponse reçue du service consultants.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Erreur lors de l'attente de la réponse", e);
        }

        if (responseReceived == null || !responseReceived) {
            throw new RuntimeException("Consultant ou Projet non trouvé ou réponse invalide");
        }
    }

    /**
     * Convert a ConsultantsProject entity to DTO.
     */
    private ConsultantsProjectsDto convertToDto(ConsultantsProject consultantsProject) {
        ConsultantsProjectsDto dto = new ConsultantsProjectsDto();
        dto.setId(consultantsProject.getId());
        dto.setConsultantsId(consultantsProject.getConsultantsId());
        dto.setConsultantsName(consultantsProject.getConsultantsName());
        dto.setProjectsId(consultantsProject.getProjectsId());
        dto.setProjectsName(consultantsProject.getProjectsName());
        return dto;
    }

    // Add the missing methods
    public List<ConsultantsProjectsDto> getConsultantProjectsByConsultantId(Long consultantId) {
        List<ConsultantsProject> consultantsProjects = consultantsProjectRepository.findByConsultantsId(consultantId);
        return consultantsProjects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ConsultantsProjectsDto> getConsultantProjectsByProjectId(Long projectId) {
        List<ConsultantsProject> consultantsProjects = consultantsProjectRepository.findByProjectsId(projectId);
        return consultantsProjects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
