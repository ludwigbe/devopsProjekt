package de.THM.devops.controller;


import de.THM.devops.dto.ConsultantsProjectsDto;
import de.THM.devops.service.ConsultantsProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/consultantsProjects")
@CrossOrigin(origins = {"http://localhost:3000", "https://devops.westeurope.cloudapp.azure.com", "http://135.236.202.15"})
@RestController
public class ConsultantsProjectController {

    private final ConsultantsProjectService consultantsProjectService;

    public ConsultantsProjectController(ConsultantsProjectService consultantsProjectService) {
        this.consultantsProjectService = consultantsProjectService;
    }

    /**
     * Creates a new consultant-project assignment.
     *
     * @param consultantsProjectsDto The data transfer object containing details of the consultant-project assignment to be created.
     * @return ResponseEntity containing the saved consultant-project assignment details and HTTP status CREATED.
     */
    @PostMapping
    public ResponseEntity<ConsultantsProjectsDto> createConsultantProject(@RequestBody ConsultantsProjectsDto consultantsProjectsDto) {
        ConsultantsProjectsDto consultantsProject = consultantsProjectService.createConsultantProject(consultantsProjectsDto);
        return ResponseEntity.ok(consultantsProject);
    }

    /**
     * Handles the retrieval of an existing consultant-project assignment by ID.
     *
     * @param id The ID of the consultant-project assignment to be retrieved.
     * @return ResponseEntity containing the retrieved consultant-project assignment details and HTTP status OK.
     */
    @GetMapping("{id}")
    public ResponseEntity<ConsultantsProjectsDto> getConsultantProjectById(@PathVariable("id") Long id) {
        ConsultantsProjectsDto consultantsProject = consultantsProjectService.getConsultantProjectById(id);
        return ResponseEntity.ok(consultantsProject);
    }

    /**
     * Returns a list of all consultant-project assignments.
     *
     * @return ResponseEntity containing a list of all consultant-project assignments and HTTP status OK.
     */
    @GetMapping
    public ResponseEntity<List<ConsultantsProjectsDto>> getAllConsultantProjects() {
        List<ConsultantsProjectsDto> consultantsProjects = consultantsProjectService.getAllConsultantProjects();
        return ResponseEntity.ok(consultantsProjects);
    }

    @PutMapping("{id}")
    public ResponseEntity<ConsultantsProjectsDto> updateConsultantProject(@PathVariable("id") Long id, @RequestBody ConsultantsProjectsDto updatedConsultantProject) {
        ConsultantsProjectsDto consultantsProject = consultantsProjectService.updateConsultantProject(id, updatedConsultantProject);
        return ResponseEntity.ok(consultantsProject);
    }

    @DeleteMapping("{id}")
    public void deleteConsultantProject(@PathVariable("id") Long id) {
        consultantsProjectService.deleteConsultantProject(id);
    }

    @GetMapping("/consultants/{consultantsId}")
    public ResponseEntity<List<ConsultantsProjectsDto>> findByConsultantsId(@PathVariable("consultantsId") Long consultantsId) {
        List<ConsultantsProjectsDto> consultantsProjects = consultantsProjectService.getConsultantProjectsByConsultantId(consultantsId);
        return ResponseEntity.ok(consultantsProjects);
    }

    @GetMapping("/projects/{projectsId}")
    public ResponseEntity<List<ConsultantsProjectsDto>> findByProjectsId(@PathVariable("projectsId") Long projectsId) {
        List<ConsultantsProjectsDto> consultantsProjects = consultantsProjectService.getConsultantProjectsByProjectId(projectsId);
        return ResponseEntity.ok(consultantsProjects);
    }
}
