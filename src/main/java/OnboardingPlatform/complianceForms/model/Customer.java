package OnboardingPlatform.complianceForms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // instead of manually creating getters and setters - Lombok dependency
@AllArgsConstructor // all of them included in constructor
@NoArgsConstructor // no arguments
@Entity
@Table(name = "CUSTOMER_TBL")
public class Customer {

    @Id // refer id to this attribute for jpa compatibility
    @GeneratedValue // instead of manually creating id's let it be generated
    private int customerId;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String nationality;
        private String streetName;

    // changed to string because I needed non-primitive data type for the service class method update
        private String streetNumber;
    // changed to string because I needed non-primitive data type for the service class method update
    private String plzNumber;

    private String city;
    private String country;
    private String taxCountry;
    private String taxIdentificationNumber;

    // changed to string because I needed non-primitive data type for the service class method update
    private String phoneNumber;
    private String emailAddress;
    private String jobTitle;

}
