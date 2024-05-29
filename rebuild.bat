@echo off

:: Empaqueta los microservicios
echo Empaquetando ms-customer...
cd ms-customer
mvn clean package
cd ..

echo Empaquetando ms-account...
cd ms-account
mvn clean package
cd ..

:: Reconstruye y levanta los contenedores Docker
docker-compose up --build
