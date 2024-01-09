# Compliance Forms Web Application
## Overview
The Compliance Forms Web Application represents the third phase of digitizing the onboarding process for customers. The primary aim was to transition from a manual, paper-based compliance forms submission process to a streamlined web-based application. The system allows users to input required compliance information, digitally sign forms, and automate data population across multiple documents.

## Demo Video
A demo video can be found under:
XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

## Technical Details
### Purpose
**Objective**: Digitize the compliance form submission process.

**Goal**: Create a user-friendly web application for customers to input compliance information and automate document generation.

**How to start the application**: You need MySQL Workbench and an open connection to the database. Specifications for that are defined in the properties file. After that you can just run the main class and go on http://localhost:9191/.

### Implementation
**Frontend**: HTML templates, CSS styling, and JavaScript functions render the user interface and pass it to the backend.

**Backend**: Developed using Spring Boot with RestControllers, Services, Models, and Repositories.

**Database**: Information stored in a database under the 'customer','EconomicBeneficiary' and 'SelfDisclosure' object with attributes essential for compliance forms.

**PDF Generation**: Utilizes PDF generation service classes triggered via RestControllers for document creation.

**Data Handling:** Models connected to Repositories manage data structures, getters, setters, and foreign key associations. There is also data handled only in the application for example the Signature that is being passed from the frontend to the backend as a picture with a restcontroller. 

**Flow**: Customers start by inputting general details, proceed to fill compliance forms (4 implemented forms for the prototypes), saving data triggers PDF generation, and auto-redirects to subsequent forms. Then after the 4 forms he gets to the ending page where this part of the onboarding process is finished.
