package OnboardingPlatform.complianceForms.service;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private Customer lastCreatedCustomer; // To store the last created customer

    // dependency injection
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public int saveCustomer(Customer customer) {
        lastCreatedCustomer = customerRepository.save(customer); // Store the last created customer
        return lastCreatedCustomer.getCustomerId();
    }

    public Customer getLastCreatedCustomer() {
        return lastCreatedCustomer;
    }

    public Customer getCustomerById(int id) {
        return customerRepository.findById(id).orElse(null); // return object or if id not found null
    }

    public String deleteCustomerById(int id) {
        customerRepository.deleteById(id);
        return "Customer deleted successfully " + id;
    }

    public Customer updateCustomerById(Customer customer) {
        // get customer by id - immutable attribute
          Customer existingCustomer = customerRepository.findById(customer.getCustomerId()).orElse(null);

        if (existingCustomer != null) {
            if (customer.getStreetName() != null) {
                existingCustomer.setStreetName(customer.getStreetName());
            }
            if (customer.getStreetNumber() != null) {
                existingCustomer.setStreetNumber(customer.getStreetNumber());
            }
            if (customer.getPlzNumber() != null) {
                existingCustomer.setPlzNumber(customer.getPlzNumber());
            }
            if (customer.getCity() != null) {
                existingCustomer.setCity(customer.getCity());
            }
            if (customer.getCountry() != null) {
                existingCustomer.setCountry(customer.getCountry());
            }
            if (customer.getTaxCountry() != null) {
                existingCustomer.setTaxCountry(customer.getTaxCountry());
            }
            if (customer.getTaxIdentificationNumber() != null) {
                existingCustomer.setTaxIdentificationNumber(customer.getTaxIdentificationNumber());
            }
            if (customer.getPhoneNumber() != null) {
                existingCustomer.setPhoneNumber(customer.getPhoneNumber());
            }
            if (customer.getEmailAddress() != null) {
                existingCustomer.setEmailAddress(customer.getEmailAddress());
            }
            if (customer.getJobTitle() != null) {
                existingCustomer.setJobTitle(customer.getJobTitle());
            }
            return customerRepository.save(existingCustomer);
        } else {
            // Handle case where customer with given ID is not found
            return null; // Or throw an exception, return a default value, etc.
        }
    }

}
