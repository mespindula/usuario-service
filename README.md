# usuario-service
API Spring Boot JWT

# Para obter Token
	POST api/sign-up Content-Type: application/json
    {
     username = cliente
     password = 123
    }

# Para realizar Build/Deploy Maven
	mvn clean install
	mvn dockerfile:push
	
# Swagger
	http://localhost:8080/api/swagger-ui.html
	
# Docker
	https://hub.docker.com/r/mespindula/usuario-service
