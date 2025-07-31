package de.THM.devops.service;

import de.THM.devops.RabbitConfig;
import de.THM.devops.dto.CustomersDto;
import de.THM.devops.model.Customer;
import de.THM.devops.repository.CustomersRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomersService {

    private final CustomersRepository customersRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CustomersService(CustomersRepository customersRepository, RabbitTemplate rabbitTemplate) {
        this.customersRepository = customersRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public CustomersDto createCustomer(CustomersDto customersDto) {
        Customer customer = new Customer(customersDto.getId(), customersDto.getName(), customersDto.getCity());
        customersRepository.save(customer);
        return new CustomersDto(customer.getId(), customer.getName(), customer.getCity());
    }

    public CustomersDto getConsumerById(Long id) {
        Customer customer = customersRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Customer not found: " + id));
        return new CustomersDto(customer.getId(), customer.getName(), customer.getCity());
    }

    public void deleteCustomers(Long id) {
        Customer customer = customersRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Customers is not exists with given id: " + id));
        customersRepository.deleteById(customer.getId());
    }

    public CustomersDto updateCustomer(Long id, CustomersDto customersDto) {
        Customer customer = customersRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Customer not found: " + customersDto.getId()));
        customer.setName(customersDto.getName());
        customer.setCity(customersDto.getCity());
        customersRepository.save(customer);
        return new CustomersDto(customer.getId(), customer.getName(), customer.getCity());
    }

    public List<CustomersDto> getAllCustomers() {
        return customersRepository.findAll().stream()
                .map(customer -> new CustomersDto(customer.getId(), customer.getName(), customer.getCity()))
                .toList();
    }

    @RabbitListener(queues = RabbitConfig.REQUEST_QUEUE_NAME)
    public void receiveCustomerId(Long customerId, String correlationId) {
        CustomersDto customer;
        try {
            customer = getConsumerById(customerId);
            boolean clientExists = customer != null;

            System.out.println("Correlation id : " + correlationId);
            String responseQueueName = RabbitConfig.RESPONSE_QUEUE_NAME;
            String responseMessage = Boolean.toString(clientExists);

            // Envoyer la réponse à la queue de réponse
            rabbitTemplate.convertAndSend(responseQueueName, responseMessage, message -> {
                message.getMessageProperties().setCorrelationId(correlationId); // Set correlationId
                return message;
            });

        } catch (RuntimeException e) {
            // Gérer le cas où le client n'existe pas
            System.out.println("Customer not found for ID: " + customerId);
            // Peut-être envoyer un message d'erreur à la queue de réponse
            String responseQueueName = RabbitConfig.RESPONSE_QUEUE_NAME;
            String errorMessage = "Customer not found for ID: " + customerId;
            rabbitTemplate.convertAndSend(responseQueueName, errorMessage, message -> {
                message.getMessageProperties().setCorrelationId(correlationId); // Set correlationId
                return message;
            });
        }
    }
}
