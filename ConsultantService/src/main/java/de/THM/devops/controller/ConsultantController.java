package de.THM.devops.controller;


import de.THM.devops.model.Consultant;
import de.THM.devops.service.Consultantservice;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://devops.westeurope.cloudapp.azure.com", "http://135.236.202.15"})
@RestController
@RequestMapping("/api/consultants")
public class ConsultantController {

    private final Consultantservice consultantService;

    public ConsultantController(Consultantservice consultantService) {
        this.consultantService = consultantService;
    }

    @GetMapping
    public List<Consultant> getAllConsultants() {
        return consultantService.getAllConsultants();
    }

    @GetMapping("/{id}")
    public Consultant getConsultantById(@PathVariable Long id) {
        return consultantService.getConsultantById(id);
    }

    @PostMapping
    public Consultant createConsultant(@RequestBody Consultant consultant) {
        return consultantService.createConsultant(consultant);
    }

    @PutMapping("/{id}")
    public Consultant updateConsultant(@PathVariable Long id, @RequestBody Consultant consultant) {
        return consultantService.updateConsultant(id, consultant);
    }

    @DeleteMapping("/{id}")
    public void deleteConsultant(@PathVariable Long id) {
        consultantService.deleteConsultant(id);
    }
}
