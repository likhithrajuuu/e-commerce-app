package com.ecommerce.customer.customer;

import jakarta.validation.Valid;
import org.springframework.util.StringUtils;

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
    }
}
