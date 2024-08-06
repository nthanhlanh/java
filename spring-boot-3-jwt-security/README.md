# Spring Boot 3.0 Security with JWT Implementation
This project demonstrates the implementation of security using Spring Boot 3.0 and JSON Web Tokens (JWT). It includes the following features:

## Features
* User registration and login with JWT authentication
* Password encryption using BCrypt
* Role-based authorization with Spring Security
* Customized access denied handling
* Customized JwtAuthenticationFilter,CustomFilter
* Logout mechanism
* Refresh token

## Technologies
* Spring Boot 3.0
* Spring Security
* JSON Web Tokens (JWT)
* BCrypt
* Maven
 
## Getting Started
To get started with this project, you will need to have the following installed on your local machine:

* JDK 17+
* Maven 3+
* Lombok


To build and run the project, follow these steps:

* Clone the repository: `git clone https://github.com/ali-bouali/spring-boot-3-jwt-security.git`
* Navigate to the project directory: cd spring-boot-security-jwt
* Add database "jwt_security" to postgres 
* Build the project: mvn clean install
* Run the project: mvn spring-boot:run 

-> The application will be available at http://localhost:8080.

## kubernetes
hướng dẫn kubernetes
cài đặt kompose để convert file docker-compose sang file deployment,service
deployment =>file này để chạy trên kubernetes
service ==> để host ra ngoài chạy trên trình duyệt
lệnh chạy 
kubectl apply -f persistent-volume.yaml
kubectl apply -f persistent-volume-claim.yaml
kubectl apply -f deployment-db.yaml
kubectl apply -f service-db.yaml
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml

kubectl get deployment => nếu xóa kubectl delete deployment eployment.yaml
kubectl get service => nếu xóa kubectl delete service eployment.yaml
