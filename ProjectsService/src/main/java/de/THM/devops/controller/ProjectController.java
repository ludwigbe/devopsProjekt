package de.THM.devops.controller;


import de.THM.devops.dto.ProjectsDto;
import de.THM.devops.service.ProjectsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/projects")
@CrossOrigin(origins = {"http://localhost:3000", "https://devops.westeurope.cloudapp.azure.com", "http://135.236.202.15"})
@RestController
public class ProjectController {

    private final ProjectsService projectsService;

    public ProjectController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    /**
     * Creates a new project with the provided details.
     *
     * @param projectsDto The project data transfer object containing details of the project to be created.
     * @return ResponseEntity containing the saved project details and an HTTP status code.
     *         Returns HTTP 400 BAD REQUEST if the project start or end date is null.
     */
    @PostMapping
    public ResponseEntity<ProjectsDto> createProject(@Valid @RequestBody ProjectsDto projectsDto) {

        System.out.println("Received project: " + projectsDto);

        if (projectsDto.getName() == null || projectsDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }

        ProjectsDto savedProject = projectsService.createProject(projectsDto);
        return ResponseEntity.ok(savedProject);
    }


    /**
     * Returns the project with the specified ID.
     *
     * @param id The ID of the project to be retrieved.
     * @return ResponseEntity containing the project details and an HTTP status code.
     *         Returns HTTP 404 NOT FOUND if the project is not found.
     */
    @GetMapping("{id}")
    public ResponseEntity<ProjectsDto> getProjectById(@PathVariable("id") Long id) {
        ProjectsDto projectsDto = projectsService.getProjectById(id);
        return new ResponseEntity<>(projectsDto, HttpStatus.OK);
    }


    /**
     * Returns a list of all projects.
     *
     * @return ResponseEntity containing a list of all project details and an HTTP status code.
     *         Returns HTTP 200 OK if the list of projects is successfully retrieved.
     */
    @GetMapping
    public ResponseEntity<List<ProjectsDto>> getAllProjects() {
        List<ProjectsDto> projectsDtos = projectsService.getAllProjects();
        return ResponseEntity.ok(projectsDtos);
    }

    /**
     * Updates the project with the specified ID with the provided details.
     *
     * @param projectId The ID of the project to be updated.
     * @param updatedProject The project data transfer object containing the updated project details.
     * @return ResponseEntity containing the updated project details and an HTTP status code.
     *         Returns HTTP 404 NOT FOUND if the project is not found.
     */
    @PutMapping("{id}")
    public ResponseEntity<ProjectsDto> updateProject(@PathVariable("id") Long projectId,
                                                     @RequestBody ProjectsDto updatedProject) {
        ProjectsDto projectsDto = projectsService.updateProject(projectId, updatedProject);
        return ResponseEntity.ok(projectsDto);
    }

    /**
     * Deletes the project with the specified ID.
     *
     * @param projectId The ID of the project to be deleted.
     * @return ResponseEntity containing a success message and an HTTP status code.
     *         Returns HTTP 200 OK if the project is successfully deleted.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProject(@PathVariable("id") Long projectId) {
        projectsService.deleteProject(projectId);
        return ResponseEntity.ok("Project deleted successfully!");
    }


/*
    @PostMapping("/{projectId}/consultants/{consultantId}")
    public ResponseEntity<ProjectsDto> addConsultantsToProject(@PathVariable Long projectId, @PathVariable Long consultantId) {
        ProjectsDto updatedProject = projectsService.addConsultantToProject(projectId, consultantId);
        return ResponseEntity.ok(updatedProject);
    }*/

}
