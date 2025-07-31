package de.THM.devops.service;

import de.THM.devops.dto.CustomersDto;
import de.THM.devops.dto.ProjectsDto;
import de.THM.devops.model.Project;
import de.THM.devops.RabbitConfig;
import de.THM.devops.repository.ProjectsRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.amqp.core.Message;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final String customersServiceUrl = "http://localhost:8081/customers";
    private Boolean responseReceived = null;
    private final CountDownLatch latch = new CountDownLatch(1);

    public ProjectsService(ProjectsRepository projectsRepository, RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.projectsRepository = projectsRepository;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public ProjectsDto createProject(ProjectsDto projectsDto) {
    /*    String correlationId = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(RabbitConfig.REQUEST_QUEUE_NAME, projectsDto.getCustomersId(), message -> {
            message.getMessageProperties().setCorrelationId(correlationId);
            return message;
        });

        try {
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timeout: Pas de réponse reçue du service customer.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Erreur lors de l'attente de la réponse", e);
        }

        if (responseReceived == null || !responseReceived) {
            throw new RuntimeException("Client non trouvé ou réponse invalide");
        }
    */

        Project project = new Project();
        project.setName(projectsDto.getName());
        project.setStartDate(projectsDto.getStartDate());
        project.setEndDate(projectsDto.getEndDate());
        project.setStatus(projectsDto.getStatus());
        project.setCustomersId(projectsDto.getCustomersId());
        project.setCustomerName(projectsDto.getCustomerName());

        Project savedProject = projectsRepository.save(project);

        // Conversion manuelle de Project vers ProjectsDto
        return convertToProjectsDto(savedProject);
    }

    public List<ProjectsDto> getAllProjects() {
        List<Project> projects = projectsRepository.findAll();
        // Conversion manuelle pour chaque projet
        return projects.stream()
                .map(this::convertToProjectsDto)
                .toList();
    }

    public ProjectsDto getProjectById(Long id) {
        Project project = projectsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        // Conversion manuelle
        return convertToProjectsDto(project);
    }

    public void deleteProject(Long id) {
        projectsRepository.deleteById(id);
        rabbitTemplate.convertAndSend(RabbitConfig.MESSAGE_QUEUE_NAME_P, "Project " + id + " deleted");
    }

    public ProjectsDto updateProject(Long id, ProjectsDto projectsDto) {
        Project project = projectsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        CustomersDto customer = restTemplate.getForObject(customersServiceUrl + "/" + projectsDto.getCustomersId(), CustomersDto.class);
        if (customer == null) {
            throw new RuntimeException("Client non trouvé");
        }

        project.setName(projectsDto.getName());
        project.setStartDate(projectsDto.getStartDate());
        project.setEndDate(projectsDto.getEndDate());
        project.setStatus(projectsDto.getStatus());
        project.setCustomersId(projectsDto.getCustomersId());

        Project updatedProject = projectsRepository.save(project);

        // Conversion manuelle
        return convertToProjectsDto(updatedProject);
    }

    @RabbitListener(queues = RabbitConfig.RESPONSE_QUEUE_NAME)
    public void receiveResponse(@Payload Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        responseReceived = Boolean.parseBoolean(new String(message.getBody()));
        latch.countDown();
    }

    @RabbitListener(queues = RabbitConfig.MESSAGE_QUEUE_NAME)
    public void receivedDelete(@Payload Message message) {
        System.out.println(message);
    }

    // Méthode utilitaire pour convertir un Project en ProjectsDto
    private ProjectsDto convertToProjectsDto(Project project) {
        return new ProjectsDto(
                project.getId(),
                project.getName(),
                project.getStartDate(),
                project.getEndDate(),
                project.getStatus(),
                project.getCustomersId(),
                project.getCustomerName()
        );
    }
}
