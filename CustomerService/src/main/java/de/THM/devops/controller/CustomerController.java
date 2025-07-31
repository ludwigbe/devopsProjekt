package de.THM.devops.controller;


import de.THM.devops.dto.CustomersDto;
import de.THM.devops.service.CustomersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.io.IOException;


import java.util.List;


@RequestMapping("api/customers")
@CrossOrigin(origins = {"http://localhost:3000", "https://devops.westeurope.cloudapp.azure.com", "http://135.236.202.15"})
@AllArgsConstructor
@RestController
public class CustomerController {

    private final CustomersService customersService;

    @PostMapping
    public ResponseEntity<CustomersDto> createCustomer(@RequestBody CustomersDto customersDto) {
        CustomersDto savedCustomer = customersService.createCustomer(customersDto);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }
    
    @GetMapping("{id}")
    public ResponseEntity<CustomersDto> getCustomersById(@PathVariable("id") Long customersId) {
        CustomersDto customersDto = customersService.getConsumerById(customersId);
        return ResponseEntity.ok(customersDto);
    }

    // Build Get All Customers REST API
    @GetMapping
    public ResponseEntity<List<CustomersDto>> getAllCustomers() {
        List<CustomersDto> customersDtos = customersService.getAllCustomers();
        return ResponseEntity.ok(customersDtos);
    }

    // Build Update Customers REST API
    @PutMapping("{id}")
    public ResponseEntity<CustomersDto> updateCustomers(@PathVariable Long id,
                                                        @RequestBody CustomersDto updatedCustomers) {
        CustomersDto customersDto = customersService.updateCustomer(id, updatedCustomers);
        return ResponseEntity.ok(customersDto);
    }

    // Build Delete Customers REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCustomers(@PathVariable("id") Long customersId) {
        customersService.deleteCustomers(customersId);
        return ResponseEntity.ok("Customers deleted successfully!");
    }
}
