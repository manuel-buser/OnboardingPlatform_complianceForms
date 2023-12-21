package OnboardingPlatform.complianceForms.controller;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerRestController {
    private final CustomerService customerService; // Assuming you have CustomerService

    @Autowired
    public CustomerRestController(CustomerService customerService) {

        this.customerService = customerService;
    }

    @PostMapping("/addCustomer")
    @ResponseBody
    public ResponseEntity<Integer> addCustomer(@RequestBody Customer customer) {
        int customerId = customerService.saveCustomer(customer);
        return ResponseEntity.ok(customerId);
    }

    @GetMapping("/getCustomer/{id}")
    public Customer getCustomerById(@PathVariable int id) { // also possible with RequestBody annotation
        // but like this we pass the id as parameter value in the URL
        return customerService.getCustomerById(id);
    }

    @PutMapping("/updateCustomer") // Put Mapping for update CRUD operation
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomerById(customer);
    }

    @DeleteMapping("/deleteCustomer/{id}") // Delete Mapping
    public String deleteCustomer(@PathVariable int id) {
        return customerService.deleteCustomerById(id);
    }

    /*
    @GetMapping("/getCustomerById")
    @ResponseBody
    public ResponseEntity<Customer> getCustomerById(@RequestParam int customerId) {
    // Customer customer = customerService.getCustomerById(customerId);
    // if (customer != null) {
    //     return ResponseEntity.ok(customer);
    // } else {
    //     return ResponseEntity.notFound().build();
    // }
    // Commented out the entire method to disable its functionality.
    }
*/

}



