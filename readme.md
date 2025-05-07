```markdown
# 3-Tier Web Application with React, Spring Boot, and Terraform

This repository contains the code for a simple 3-tier web application built using:

- **Frontend**: React.js
- **Backend**: Spring Boot
- **Infrastructure**: Terraform (for setting up AWS resources)

The application is designed with a frontend and backend architecture hosted on AWS, utilizing ECS (Elastic Container Service), ALB (Application Load Balancer), API Gateway, and Cognito for authentication.

---

## Application Overview

This is a **3-tier web application**:

1. **Frontend** (React.js)
   - Deployed on **Amazon ECS** in a **public subnet**.
   - Exposed to the internet through an **Application Load Balancer (ALB)**.
   - Accessible via the domain: [https://indras.adobe-project.online/](https://indras.adobe-project.online/).
   
2. **Backend** (Spring Boot)
   - Deployed on **Amazon ECS** in a **private subnet**.
   - Accessible only through the **API Gateway** and **ALB**.
   
3. **Authentication**
   - Both frontend and backend services are secured by **AWS Cognito** for user authentication.
   - All communication between frontend and backend requires authentication via Cognito's OAuth integration.

---

## Directory Structure

This repository contains the following main directories:

```

* /springboot-code: Backend code (Spring Boot)
* /ui-reactjs: Frontend code (React.js)
* /terraform: Terraform code for provisioning AWS infrastructure

````

### Spring Boot Code

This directory contains the backend logic of the application built with **Spring Boot**.

- The backend is deployed to **AWS ECS** and is responsible for handling requests from the frontend.
- It is integrated with **AWS Cognito** for authentication.
- The backend is exposed through **API Gateway** and **Application Load Balancer** (ALB).

### UI ReactJS

This directory contains the frontend code built with **React.js**.

- The frontend is deployed to **AWS ECS** and serves the user interface.
- The application is hosted behind an **Application Load Balancer (ALB)**, which is in a **public subnet**.

### Terraform

This directory contains the **Terraform** configuration files used to provision the AWS infrastructure:

- **ECS (Frontend and Backend)** setup
- **API Gateway** configuration
- **Cognito** setup for user authentication
- **VPC**, **ALB**, and **Subnet** configuration

---

## Authentication with AWS Cognito

Both the frontend and backend of this application use **AWS Cognito** for user authentication. The authentication flow is implemented using **OAuth**.

1. **Frontend Authentication**:
   - The frontend (React app) uses **Cognito User Pools** for user registration and login.
   - The frontend passes the authentication token to the backend API for validation.

2. **Backend Authentication**:
   - The backend (Spring Boot app) validates the **JWT token** received from the frontend via **AWS Cognito**.
   - **API Gateway** intercepts the request and validates the token before forwarding it to the backend.

---

## Architecture Diagram

Below is the **OAuth architecture** with AWS Cognito integration:

```plaintext
+---------------------+
|   React Frontend     | <---> (Cognito User Pool) <--> AWS Cognito
|   (ECS + ALB)        |  
+---------------------+                                      
         |                                                
         |                                                
         v                                                
+---------------------+                                  
| API Gateway         | <---> (OAuth Token Validation) --> AWS Cognito
+---------------------+                                  
         |                                                
         |                                                
         v                                                
+---------------------+                                  
| Spring Boot Backend | <---> (Cognito Identity Pool) --> AWS Cognito
|   (ECS + ALB)       |
+---------------------+  
````

1. **User Authentication**: The user logs into the frontend (React.js), which interacts with **AWS Cognito** for authentication.
2. **Token Passing**: After a successful login, the frontend sends the **JWT token** to the backend through **API Gateway**.
3. **Token Validation**: The backend validates the token using **AWS Cognito** before processing the request.

---

## Running the Application

### Prerequisites

* **AWS account**: Ensure you have access to your AWS account to deploy resources.
* **Terraform**: You need to have Terraform installed to provision the infrastructure.
* **AWS CLI**: Make sure the AWS CLI is configured with the appropriate credentials.
* **Docker**: Docker is required to build and deploy the containers for both the frontend and backend.

