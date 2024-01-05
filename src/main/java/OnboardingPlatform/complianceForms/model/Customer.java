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
    private String streetNumber;
    private String plzNumber;
    private String city;
    private String country;
    private String taxCountry;
    private String taxIdentificationNumber;
    private String phoneNumber;
    private String emailAddress;
    private String jobTitle;

    // changed some int to String because it was easier to implement some methods in the Service class with non-primitive datatype

}
