package com.ecommerce.customer.customer;

import org.apache.commons.lang3.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public String createCustomer(@Valid CustomerRequest request) {
        var customer =  customerRepository.save(customerMapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(@Valid CustomerRequest request) {
        var updatedCustomer = customerRepository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                    String.format("Customer with id %s not found", request.id())
                ));
        mergerCustomer(updatedCustomer, request);
        customerRepository.save(updatedCustomer);
        
    }

    private void mergerCustomer(Customer updatedCustomer, @Valid CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstName())){
            updatedCustomer.setFirstName(request.firstName());
        }
        if(StringUtils.isNotBlank(request.lastName())){
            updatedCustomer.setLastName(request.lastName());
        }
        if(request.address() != null){
            updatedCustomer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::fromCustomter)
                .collect(Collectors.toList());
    }

    public Boolean existsById(String customerId) {
        return customerRepository.findById(customerId)
                .isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::fromCustomter)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("No customer found with id: %s", customerId)
                ));
    }

    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }
}
